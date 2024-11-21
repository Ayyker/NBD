package repository;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import model.Purchase;
import org.bson.Document;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class PurchaseRepository extends AbstractMongoRepository {
    private final MongoCollection<Purchase> purchaseCollection;

    public PurchaseRepository() {
        initDbConnection();
        MongoDatabase database = getDatabase();
        CodecProvider pojoCodecProvider = PojoCodecProvider.builder().automatic(true).build();
        CodecRegistry pojoCodecRegistry = CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                CodecRegistries.fromProviders(pojoCodecProvider));
        purchaseCollection = database.getCollection("purchases", Purchase.class).withCodecRegistry(pojoCodecRegistry);
    }

    public void save(Purchase purchase) {
        purchaseCollection.insertOne(purchase);
    }

    public void update(Purchase purchase) {
        purchaseCollection.updateOne(Filters.eq("_id", purchase.getId()),
                new Document("$set", purchase));
    }

    public List<Purchase> findAll() {
        return purchaseCollection.find().into(new ArrayList<>());
    }

    public Purchase findById(ObjectId id) {
        return purchaseCollection.find(Filters.eq("_id", id)).first();
    }

    public void delete(ObjectId id) {
        purchaseCollection.deleteOne(Filters.eq("_id", id));
    }

    @Override
    public void close() throws Exception {
        closeConnection();
    }
}