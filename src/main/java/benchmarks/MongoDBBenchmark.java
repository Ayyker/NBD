package benchmarks;

import com.mongodb.client.*;
import org.bson.Document;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput)
@State(Scope.Thread)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class MongoDBBenchmark {

    private MongoClient mongoClient;
    private MongoCollection<Document> collection;

    @Setup(Level.Trial)
    public void setup() {
        // Connect to MongoDB
        mongoClient = MongoClients.create("mongodb://admin:adminpassword@localhost:27017");

        // Specify the database and collection
        MongoDatabase database = mongoClient.getDatabase("admin");
        collection = database.getCollection("testcollection");

        // Ensure the collection is clean
        collection.drop();

        // Pre-load data
        for (int i = 0; i < 100; i++) {
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
        return collection.find(new Document("key", "key500")).first(); // Read a specific document
    }
}
