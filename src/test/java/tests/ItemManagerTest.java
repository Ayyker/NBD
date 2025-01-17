package tests;

import Cassandra.SessionFactory;
import com.datastax.oss.driver.api.core.CqlSession;
import manager.ItemManager;
import model.Item;
import org.junit.jupiter.api.*;
import repository.ItemRepository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ItemManagerTest {

    private static ItemManager itemManager;
    CqlSession session = SessionFactory.initCassandraConnection();

    @BeforeAll
    static void setup() {
        ItemRepository itemRepository = new ItemRepository();
        itemManager = new ItemManager(itemRepository);
    }

    @BeforeEach
    void init() {
        // Ensure the database is cleaned before each test
        session.execute("TRUNCATE TABLE onlineStore.Items;");
    }

    @Test
    void testRegisterItem() {
        Item item = new Item(1, "12345", "Laptop", 3000.0, true);
        itemManager.registerItem(item);

        List<Item> items = itemManager.getAllItems();
        assertEquals(1, items.size());
        assertEquals("Laptop", items.get(0).getItemName());
    }

    @Test
    void testRemoveItem() {
        Item item = new Item(2, "54321", "Tablet", 1500.0, true);
        itemManager.registerItem(item);

        itemManager.removeItem(item.getId());

        List<Item> items = itemManager.getAllItems();
        assertEquals(0, items.size());
    }

    @Test
    void testGetItemByItemID() {
        Item item = new Item(3, "67890", "Smartphone", 2000.0, true);
        itemManager.registerItem(item);

        Item retrievedItem = itemManager.getItemByItemID(item.getId());
        assertNotNull(retrievedItem);
        assertEquals("Smartphone", retrievedItem.getItemName());
    }

    @Test
    void testGetAllItems() {
        Item item1 = new Item(4, "11111", "Monitor", 800.0, true);
        Item item2 = new Item(5, "22222", "Keyboard", 150.0, true);

        itemManager.registerItem(item1);
        itemManager.registerItem(item2);

        List<Item> items = itemManager.getAllItems();
        assertEquals(2, items.size());
    }

    @Test
    void testBuyItem() {
        Item item = new Item(6, "33333", "Mouse", 50.0, true);
        itemManager.registerItem(item);

        itemManager.buyItem(item);

        Item retrievedItem = itemManager.getItemByItemID(item.getId());
        assertFalse(retrievedItem.isAvailable());
    }
}
