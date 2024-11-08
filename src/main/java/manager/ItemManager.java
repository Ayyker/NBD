package manager;

import jakarta.persistence.EntityManager;
import model.Item;
import repository.ItemRepository;

import java.util.List;

public class ItemManager {

    private final ItemRepository itemRepository;

    public ItemManager(EntityManager em) {
        this.itemRepository = new ItemRepository(em);
    }

    public List<Item> getItemByItemID(String itemID) {
        return itemRepository.findByItemID(itemID);  // Korzystanie z metody findByItemID
    }

    public Item registerItem(String name, double cost, String itemID, boolean available) {
        Item item = new Item(name, cost, itemID, available);
        itemRepository.saveOrUpdate(item);
        return item;
    }

    public List<Item> getAvailableItems(boolean available) {
        return itemRepository.findByAvailable(available);
    }

    public void updateItemCost(Long id, double itemCost) {
        itemRepository.updateItemCost(id, itemCost);
    }

    public void removeItem(Item item) {
        itemRepository.delete(item);
    }

    public void buyItem(Long id) {
        itemRepository.buyItem(id);
    }

    public void updateItem(Long id, String newName, double newCost, boolean newAvailability) {
        Item item = itemRepository.findById(id);

        if (item == null) {
            throw new IllegalArgumentException("Item with ID " + id + " does not exist.");
        }

        item.setItemName(newName);
        item.setItemCost(newCost);
        item.setAvailable(newAvailability);

        itemRepository.saveOrUpdate(item);
    }

}
