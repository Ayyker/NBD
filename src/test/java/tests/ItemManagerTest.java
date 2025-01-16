//package tests;
//
//import manager.ItemManager;
//import model.Item;
//import org.bson.types.ObjectId;
//import org.junit.jupiter.api.*;
//import repository.ItemRepository;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class ItemManagerTest {
//
//    private static ItemManager itemManager;
//
//    @BeforeAll
//    static void setup() {
//        ItemRepository itemRepository = new ItemRepository();
//        itemManager = new ItemManager(itemRepository);
//    }
//
//    @BeforeEach
//    void init() {
//        itemManager.getDatabase().drop(); // Drop the database to reset the state between tests
//    }
//
//    @Test
//    void testRegisterItem() {
//        ObjectId id = new ObjectId();
//        String name = "New Item";
//        double cost = 50.0;
//        String itemID = "67890";
//        boolean available = true;
//
//        Item result = itemManager.registerItem(id, name, cost, itemID, available);
//
//        List<Item> retrieved = itemManager.getAllItems();
//        assertEquals(1, retrieved.size());
//        assertEquals(result.getItemName(), retrieved.get(0).getItemName());
//    }
//
//    @Test
//    void testGetAvailableItems() {
//        Item item1 = new Item(new ObjectId(), "123", "Item 1", 20.0, true);
//        Item item2 = new Item(new ObjectId(), "124", "Item 2", 30.0, false);
//        Item item3 = new Item(new ObjectId(), "125", "Item 3", 40.0, true);
//
//        itemManager.registerItem(item1);
//        itemManager.registerItem(item2);
//        itemManager.registerItem(item3);
//
//        List<Item> availableItems = itemManager.getAvailableItems(true);
//
//        assertEquals(2, availableItems.size());
//        assertEquals("Item 1", availableItems.get(0).getItemName());
//        assertEquals("Item 3", availableItems.get(1).getItemName());
//    }
//
//    @Test
//    void testUpdateItemCost() {
//        Item item = new Item(new ObjectId(), "123", "Test Item", 100.0, true);
//        itemManager.registerItem(item);
//        List<Item> retrieved = itemManager.getAllItems();
//        itemManager.updateItemCost(retrieved.get(0), 50.0);
//        retrieved = itemManager.getAllItems();
//
//        assertEquals(50.0, retrieved.get(0).getItemCost());
//    }
//
//    @Test
//    void testRemoveItem() {
//        Item item = new Item(new ObjectId(), "123", "Test Item", 100.0, true);
//        itemManager.registerItem(item);
//        List<Item> retrieved = itemManager.getAllItems();
//        itemManager.removeItem(retrieved.get(0));
//
//        retrieved = itemManager.getAllItems();
//        assertEquals(0, retrieved.size());
//    }
//
//    @Test
//    void testBuyItem() {
//        Item item = new Item(new ObjectId(), "123", "Test Item", 100.0, true);
//        itemManager.registerItem(item);
//        List<Item> retrieved = itemManager.getAllItems();
//        itemManager.buyItem(retrieved.get(0));
//        retrieved = itemManager.getAllItems();
//
//        assertFalse(retrieved.get(0).isAvailable());
//    }
//
//    @Test
//    void testUpdateItem() {
//        Item item = new Item(new ObjectId(), "123", "Test Item", 100.0, true);
//        itemManager.registerItem(item);
//        List<Item> retrieved = itemManager.getAllItems();
//        String newName = "New Item Name";
//        double newCost = 50.0;
//        boolean newAvailability = false;
//        itemManager.updateItem(retrieved.get(0), newName, newCost, newAvailability);
//        retrieved = itemManager.getAllItems();
//
//        assertEquals(newName, retrieved.get(0).getItemName());
//        assertEquals(newCost, retrieved.get(0).getItemCost());
//        assertEquals(newAvailability, retrieved.get(0).isAvailable());
//    }
//
//    @Test
//    void testGetItemByItemID() {
//        Item item = new Item(new ObjectId(), "123", "Test Item", 100.0, true);
//        itemManager.registerItem(item);
//        List<Item> retrieved = itemManager.getAllItems();
//
//        assertEquals("123", retrieved.get(0).getItemID());
//    }
//}
