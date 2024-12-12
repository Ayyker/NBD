package benchmarks;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput) // Operacje na sekundę
@State(Scope.Thread)            // Oddzielny stan dla każdego wątku
@OutputTimeUnit(TimeUnit.MILLISECONDS) // Wyniki w milisekundach
public class MongoDBBenchmark {

    @Param({"100", "1000", "10000"}) // Liczba dokumentów w danych testowych
    private int numberOfDocuments;

    private MongoClient mongoClient;
    private MongoCollection<Document> collection;

    @Setup(Level.Trial)
    public void setup() {
        mongoClient = MongoClients.create("mongodb://admin:adminpassword@localhost:27017");
        MongoDatabase database = mongoClient.getDatabase("admin");
        collection = database.getCollection("testcollection");

        collection.drop();

        for (int i = 0; i < numberOfDocuments; i++) {
            Document doc = new Document("key", "key" + i).append("value", "value" + i);
            collection.insertOne(doc);
        }
    }

    @TearDown(Level.Trial)
    public void teardown() {
        mongoClient.close();
    }

    @Benchmark
    @Fork(value = 2, warmups = 2)
    public Document testFindOperation() {
        int randomKey = (int) (Math.random() * numberOfDocuments);
        return collection.find(new Document("key", "key" + randomKey)).first();
    }

    @Benchmark
    @Fork(value = 2, warmups = 2)
    public void testInsertOperation() {
        Document doc = new Document("key", "key" + System.nanoTime()).append("value", "value");
        collection.insertOne(doc);
    }

    @Benchmark
    @Fork(value = 2, warmups = 2)
    public void testDeleteOperation() {
        int randomKey = (int) (Math.random() * numberOfDocuments);
        collection.deleteOne(new Document("key", "key" + randomKey));
    }

    @Benchmark
    @Threads(4) // 4 równoległe wątki
    @Fork(value = 2, warmups = 2)
    public Document testConcurrentFindOperation() {
        int randomKey = (int) (Math.random() * numberOfDocuments);
        return collection.find(new Document("key", "key" + randomKey)).first();
    }
}
