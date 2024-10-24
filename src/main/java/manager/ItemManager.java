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

    // Znalezienie przedmiotu po jego unikalnym ID
    public List<Item> getItemByItemID(String itemID) {
        return itemRepository.findByItemID(itemID);  // Korzystanie z metody findByItemID
    }

    // Rejestracja nowego przedmiotu
    public Item registerItem(String name, double cost, String itemID, boolean available) {// Zapis lub aktualizacja typu przedmiotu
        Item item = new Item(name, cost, itemID, available);
        itemRepository.saveOrUpdate(item);
        return item;
    }

    // Znalezienie dostępnych przedmiotów
    public List<Item> getAvailableItems(boolean available) {
        return itemRepository.findByAvailable(available);  // Korzystanie z metody findByAvailable
    }

    // Aktualizacja kosztu przedmiotu
    public void updateItemCost(Long id, double itemCost) {
        itemRepository.updateItemCost(id, itemCost);  // Korzystanie z metody updateItemCost
    }

    // Usunięcie przedmiotu
    public void removeItem(Item item) {
        itemRepository.delete(item);  // Korzystanie z metody delete
    }

    public void buyItem(Long id) {
        itemRepository.buyItem(id);
    }

    public void updateItem(Long id, String newName, double newCost, boolean newAvailability) {
        Item item = itemRepository.findById(id);  // Sprawdzenie, czy przedmiot istnieje

        if (item == null) {
            throw new IllegalArgumentException("Item with ID " + id + " does not exist.");
        }

        // Aktualizacja właściwości
        item.setItemName(newName);
        item.setItemCost(newCost);
        item.setAvailable(newAvailability);

        // Zapisanie zaktualizowanego przedmiotu
        itemRepository.saveOrUpdate(item);
    }

}
