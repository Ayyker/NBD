package repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.types.ObjectId;
import model.Purchase;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class PurchaseRepository extends AbstractMongoRepository {

    private final MongoCollection<Purchase> purchaseCollection;

    public PurchaseRepository() {
        initDbConnection();
        MongoDatabase database = getDatabase();
        purchaseCollection = database.getCollection("purchase", Purchase.class);
    }

    public void saveOrUpdate(Purchase purchase) {
        if (purchase.getId() == null) {
            purchase.setId(new ObjectId());
            purchaseCollection.insertOne(purchase);
        } else {
            purchaseCollection.replaceOne(eq("_id", purchase.getId()), purchase);
        }
    }

    public List<Purchase> findAll() {
        List<Purchase> purchases = new ArrayList<>();
        for (Purchase purchase : purchaseCollection.find()) {
            purchases.add(purchase);
        }
        return purchases;
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