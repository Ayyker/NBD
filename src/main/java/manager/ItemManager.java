package manager;

import model.Item;
import repository.ItemRepository;

import java.util.List;

public class ItemManager {

    private final ItemRepository itemRepository;

    public ItemManager(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public Item getItemByItemID(int itemID) {
        return itemRepository.getItemById(itemID);
    }

    public void registerItem(Item item) {
        itemRepository.registerItem(item);
    }

    public void removeItem(int id) {
        itemRepository.removeItem(id);
    }

    public List<Item> getAllItems() {
        return itemRepository.getAllItems();
    }

    public void buyItem(Item item) {
        item.setAvailable(false);
        itemRepository.updateItem(item);
    }

    public boolean isItemAvailable(int id) {
        return getItemByItemID(id).isAvailable();
    }
}
