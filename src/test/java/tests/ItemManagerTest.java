package tests;

import manager.ItemManager;
import model.Item;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.*;
import repository.ItemRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ItemManagerTest {

    private static ItemRepository itemRepository;
    private static ItemManager itemManager;

    @BeforeAll
    static void setup() {
        itemRepository = new ItemRepository();
        itemManager = new ItemManager(itemRepository);
    }

    @BeforeEach
    void init() {
        itemManager.getDatabase().drop(); // Drop the database to reset the state between tests
    }

    @Test
    void testRegisterItem() {
        ObjectId id = new ObjectId();
        String name = "New Item";
        double cost = 50.0;
        String itemID = "67890";
        boolean available = true;

        Item result = itemManager.registerItem(id, name, cost, itemID, available);

        List<Item> retrieved = itemManager.getAllItems();
        assertEquals(1, retrieved.size());
        assertEquals(result.getItemName(), retrieved.getFirst().getItemName());
    }

    @Test
    void testGetAvailableItems() {
        Item item1 = new Item(new ObjectId(), "123", "Item 1", 20.0, true);
        Item item2 = new Item(new ObjectId(), "124", "Item 2", 30.0, false);
        Item item3 = new Item(new ObjectId(), "125", "Item 3", 40.0, true);

        itemManager.registerItem(item1);
        itemManager.registerItem(item2);
        itemManager.registerItem(item3);

        List<Item> availableItems = itemManager.getAvailableItems(true);

        assertEquals(2, availableItems.size());
        assertEquals("Item 1", availableItems.get(0).getItemName());
        assertEquals("Item 3", availableItems.get(1).getItemName());
    }

    @Test
    void testUpdateItemCost() {
        Item item = new Item(new ObjectId(), "123", "Test Item", 100.0, true);
        itemManager.registerItem(item);
        List<Item> retrieved = itemManager.getAllItems();
        itemManager.updateItemCost(retrieved.getFirst(), 50.0);
        retrieved = itemManager.getAllItems();

        assertEquals(50.0, retrieved.getFirst().getItemCost());
    }

    @Test
    void testRemoveItem() {
        Item item = new Item(new ObjectId(), "123", "Test Item", 100.0, true);
        itemManager.registerItem(item);
        List<Item> retrieved = itemManager.getAllItems();
        itemManager.removeItem(retrieved.getFirst());

        retrieved = itemManager.getAllItems();
        assertEquals(0, retrieved.size());
    }

    @Test
    void testBuyItem() {
        Item item = new Item(new ObjectId(), "123", "Test Item", 100.0, true);
        itemManager.registerItem(item);
        List<Item> retrieved = itemManager.getAllItems();
        itemManager.buyItem(retrieved.getFirst());
        retrieved = itemManager.getAllItems();

        assertFalse(retrieved.getFirst().isAvailable());
    }

    @Test
    void testUpdateItem() {
        Item item = new Item(new ObjectId(), "123", "Test Item", 100.0, true);
        itemManager.registerItem(item);
        List<Item> retrieved = itemManager.getAllItems();
        String newName = "New Item Name";
        double newCost = 50.0;
        boolean newAvailability = false;
        itemManager.updateItem(retrieved.getFirst(), newName, newCost, newAvailability);
        retrieved = itemManager.getAllItems();

        assertEquals(newName, retrieved.getFirst().getItemName());
        assertEquals(newCost, retrieved.getFirst().getItemCost());
        assertEquals(newAvailability, retrieved.getFirst().isAvailable());
    }

    @Test
    void testGetItemByItemID() {
        Item item = new Item(new ObjectId(), "123", "Test Item", 100.0, true);
        itemManager.registerItem(item);
        List<Item> retrieved = itemManager.getAllItems();

        assertEquals("123", retrieved.getFirst().getItemID());
    }
}
