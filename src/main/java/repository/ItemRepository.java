package repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import model.Item;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class ItemRepository extends AbstractMongoRepository{

    private final MongoCollection<Item> itemCollection;

    public ItemRepository() {
        initDbConnection();
        MongoDatabase database = getDatabase();
        itemCollection = database.getCollection("items", Item.class);
    }

    public void saveOrUpdate(Item item) {
        itemCollection.insertOne(item);
    }

    public List<Item> findAll() {
        List<Item> items = new ArrayList<>();
        for (Item item : itemCollection.find()) {
            items.add(item);
        }
        return items;
    }

    public Item findById(ObjectId id) {
        return itemCollection.find(Filters.eq("_id", id)).first();
    }

    public void delete(ObjectId id) {
        itemCollection.deleteOne(Filters.eq("_id", id));
    }

    @Override
    public void close() throws Exception {
        closeConnection();
    }
}
