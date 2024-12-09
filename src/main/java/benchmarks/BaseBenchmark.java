package benchmarks;

import com.mongodb.client.*;
import org.bson.Document;
import org.openjdk.jmh.annotations.*;
import redis.clients.jedis.Jedis;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput) // Mierzenie operacji na sekundę
@State(Scope.Thread)            // Oddzielny stan dla każdego wątku
@OutputTimeUnit(TimeUnit.MILLISECONDS) // Wyniki w milisekundach
public class BaseBenchmark {

    private Jedis jedis;
    private MongoCollection<Document> mongoCollection;
    private MongoClient mongoClient;


    @Setup(Level.Trial)
    public void setup() {
        // Konfiguracja Redis
        jedis = new Jedis("localhost", 6379);

        // Konfiguracja MongoDB
        mongoClient = MongoClients.create("mongodb://admin:adminpassword@localhost:27017");
        MongoDatabase database = mongoClient.getDatabase("benchmarkdb");
        mongoCollection = database.getCollection("testcollection");

        // Przygotowanie danych w MongoDB
        mongoCollection.drop();
        for (int i = 0; i < 100; i++) {
            Document doc = new Document("key", "key" + i).append("value", "value" + i);
            mongoCollection.insertOne(doc);
        }

        // Przygotowanie danych w Redis (Cache)
        jedis.flushAll(); // Wyczyszczenie Redis przed testami
        for (int i = 0; i < 100; i++) {
            jedis.set("key" + i, "value" + i);
        }
    }

    @TearDown(Level.Trial)
    public void teardown() {
        jedis.close();
        mongoClient.close();
    }

    @Benchmark
    @Fork(value = 2, warmups = 1)
    public String testCacheHit() {
        // Trafienie w cache
        return jedis.get("key500");
    }

    @Benchmark
    @Fork(value = 2, warmups = 1)
    public String testCacheMiss() {
        // Brak trafienia w cache
        String key = "key1001";
        String value = jedis.get(key);

        if (value == null) {
            // Symulacja odczytu z MongoDB
            Document document = mongoCollection.find(new Document("key", key)).first();
            if (document != null) {
                value = document.getString("value");
                jedis.set(key, value); // Aktualizacja cache
            }
        }

        return value;
    }

    @Benchmark
    @Fork(value = 2, warmups = 1)
    public void testInvalidateCache() {
        // Unieważnienie cache
        jedis.del("key500");
    }
}
