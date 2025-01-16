package manager;

import com.mongodb.client.MongoDatabase;
import model.Item;
import org.bson.types.ObjectId;
import repository.ItemRepository;

import java.util.ArrayList;
import java.util.List;

public class ItemManager {

    private final ItemRepository itemRepository;

    public ItemManager(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public Item getItemByItemID(String itemID) {
        return itemRepository.findByItemId(itemID);
    }

    public Item registerItem(ObjectId id, String name, double cost, String itemID, boolean available) {
        Item item = new Item(id, itemID, name, cost, available);
        itemRepository.save(item);
        return item;
    }

    public void registerItem(Item item) {
        itemRepository.save(item);
    }

    public List<Item> getAvailableItems(boolean available) {
        List<Item> items = itemRepository.findAll();
        List<Item> availableItems = new ArrayList<>();
        for (Item item : items) {
            if (item.isAvailable() == available) {
                availableItems.add(item);
            }
        }
        return availableItems;
    }

    public void updateItemCost(Item item, double itemCost) {
       item.setItemCost(itemCost);
        itemRepository.update(item);
    }

    public void removeItem(Item item) {
        itemRepository.delete(item);
    }

    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    public void buyItem(Item item) {
        item.setAvailable(false);
        itemRepository.update(item);
    }

    public void updateItem(Item item, String newName, double newCost, boolean newAvailability) {
        if (item == null) {
            throw new IllegalArgumentException("Item with given ID does not exist.");
        }

        item.setItemName(newName);
        item.setItemCost(newCost);
        item.setAvailable(newAvailability);

        itemRepository.update(item);
    }

    public MongoDatabase getDatabase() {
        return itemRepository.getDatabase();
    }

    public boolean isItemAvailable(ObjectId itemId) {
        List<Item> availableItems = getAvailableItems(true);
        return availableItems.stream().anyMatch(item -> item.getId().equals(itemId));
    }
}
