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

    public void save(Item item) {
        itemCollection.insertOne(item);
    }

    public void update(Item item) {
        itemCollection.replaceOne(eq("_id", item.getId()), item);
    }

    public List<Item> findAll() {
        List<Item> items = new ArrayList<>();
        for (Item item : itemCollection.find()) {
            items.add(item);
        }
        return items;
    }

    public Item findByItemId(String item_id) {
        return itemCollection.find(Filters.eq("item_id", item_id)).first();
    }

    public void delete(ObjectId id) {
        itemCollection.deleteOne(Filters.eq("_id", id));
    }

    @Override
    public void close() throws Exception {
        closeConnection();
    }
}
