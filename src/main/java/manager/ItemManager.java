package manager;

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

    public Item getItemByID(ObjectId id) {
        return itemRepository.findById(id);
    }

    public Item registerItem(ObjectId id, String name, double cost, String itemID, boolean available) {
        Item item = new Item(id, itemID, name, cost, available);
        itemRepository.saveOrUpdate(item);
        return item;
    }

    public List<Item> getAvailableItems(boolean available) {
        List<Item> items = itemRepository.findAll();
        List<Item> avaibleItems = new ArrayList<>();
        for (Item item : items) {
            if (item.isAvailable() != available) {
                avaibleItems.add(item);
            }
        }
        return avaibleItems;
    }

    public void updateItemCost(Item item, double itemCost) {
       item.setItemCost(itemCost);
        itemRepository.saveOrUpdate(item);
    }

    public void removeItem(Item item) {
        itemRepository.delete(item.getId());
    }

    public void buyItem(Item item) {
        item.setAvailable(false);
        itemRepository.saveOrUpdate(item);
    }

    public void updateItem(Item item, String newName, double newCost, boolean newAvailability) {
        if (item == null) {
            throw new IllegalArgumentException("Item with given ID does not exist.");
        }

        item.setItemName(newName);
        item.setItemCost(newCost);
        item.setAvailable(newAvailability);

        itemRepository.saveOrUpdate(item);
    }

}
