package benchmarks;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
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

    @Param({"100", "1000", "10000"}) // Liczba kluczy w danych testowych
    private int numberOfKeys;

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
        for (int i = 0; i < numberOfKeys; i++) {
            Document doc = new Document("key", "key" + i).append("value", "value" + i);
            mongoCollection.insertOne(doc);
        }

        // Przygotowanie danych w Redis (Cache)
        jedis.flushAll();
        for (int i = 0; i < numberOfKeys; i++) {
            jedis.set("key" + i, "value" + i);
        }
    }

    @TearDown(Level.Trial)
    public void teardown() {
        jedis.flushAll();
        jedis.close();
        mongoClient.close();
    }

    @Benchmark
    @Fork(value = 2, warmups = 1)
    public String testCacheHit() {
        int randomKey = (int) (Math.random() * numberOfKeys);
        return jedis.get("key" + randomKey);
    }

    @Benchmark
    @Fork(value = 2, warmups = 1)
    public String testCacheMiss() {
        int randomKey = numberOfKeys + (int) (Math.random() * 1000); // Klucze spoza zakresu
        String key = "key" + randomKey;
        String value = jedis.get(key);

        if (value == null) {
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
        int randomKey = (int) (Math.random() * numberOfKeys);
        jedis.del("key" + randomKey);
    }

    @Benchmark
    @Fork(value = 2, warmups = 1)
    public void testInvalidateAllCache() {
        jedis.flushAll();
    }
}
