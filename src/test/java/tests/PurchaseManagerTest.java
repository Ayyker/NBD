package tests;

import Cassandra.SessionFactory;
import com.datastax.oss.driver.api.core.CqlSession;
import manager.ItemManager;
import manager.PurchaseManager;
import model.Item;
import model.Purchase;
import org.junit.jupiter.api.*;
import repository.ItemRepository;
import repository.PurchaseRepository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PurchaseManagerTest {

    private static PurchaseManager purchaseManager;
    private static ItemManager itemManager;
    CqlSession session = SessionFactory.initCassandraConnection();

    @BeforeAll
    static void setup() {
        ItemRepository itemRepository = new ItemRepository();
        PurchaseRepository purchaseRepository = new PurchaseRepository();
        itemManager = new ItemManager(itemRepository);
        purchaseManager = new PurchaseManager(purchaseRepository, itemManager);
    }

    @BeforeEach
    void init() {
        // Ensure the database is cleaned before each test
        session.execute("TRUNCATE TABLE onlineStore.Items;");
        session.execute("TRUNCATE TABLE onlineStore.Purchases;");
    }

    @Test
    void testRegisterPurchase() {
        Item item = new Item(1, "12345", "Laptop", 3000.0, true);
        itemManager.registerItem(item);

        purchaseManager.registerPurchase(1, 1001, 1, 2, 6000.0);

        List<Purchase> purchases = purchaseManager.getAllPurchases();
        assertEquals(1, purchases.size());
        assertEquals(6000.0, purchases.get(0).getTotalCost());
        assertEquals(1001, purchases.get(0).getClientId());
    }

    @Test
    void testRegisterPurchaseInvalidAmount() {
        Item item = new Item(2, "54321", "Tablet", 1500.0, true);
        itemManager.registerItem(item);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                purchaseManager.registerPurchase(2, 1002, 2, -1, 1500.0));
        assertEquals("Purchase amount must be greater than zero.", exception.getMessage());
    }

    @Test
    void testRegisterPurchaseUnavailableItem() {
        Item item = new Item(3, "67890", "Smartphone", 2000.0, false);
        itemManager.registerItem(item);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                purchaseManager.registerPurchase(3, 1003, 3, 1, 2000.0));
        assertEquals("Item must be available for purchase.", exception.getMessage());
    }

    @Test
    void testGetPurchaseById() {
        Item item = new Item(4, "11223", "Monitor", 800.0, true);
        itemManager.registerItem(item);

        purchaseManager.registerPurchase(4, 1004, 4, 3, 2400.0);

        Purchase retrievedPurchase = purchaseManager.getPurchaseById(4);
        assertNotNull(retrievedPurchase);
        assertEquals(3, retrievedPurchase.getAmount());
        assertEquals(2400.0, retrievedPurchase.getTotalCost());
    }

    @Test
    void testDeletePurchase() {
        Item item = new Item(5, "33445", "Keyboard", 100.0, true);
        itemManager.registerItem(item);

        purchaseManager.registerPurchase(5, 1005, 5, 1, 100.0);
        purchaseManager.deletePurchase(5);

        purchaseManager.getAllPurchases();
        assertEquals(0, purchaseManager.getAllPurchases().size());
    }

    @Test
    void testUpdatePurchase() {
        Item item = new Item(6, "55667", "Mouse", 50.0, true);
        itemManager.registerItem(item);

        purchaseManager.registerPurchase(6, 1006, 6, 2, 100.0);

        Purchase updatedPurchase = new Purchase(6, 1006, 6, 4, 200.0);
        purchaseManager.updatePurchase(updatedPurchase);

        Purchase retrievedPurchase = purchaseManager.getPurchaseById(6);
        assertNotNull(retrievedPurchase);
        assertEquals(4, retrievedPurchase.getAmount());
        assertEquals(200.0, retrievedPurchase.getTotalCost());
    }

    @Test
    void testGetAllPurchases() {
        Item item1 = new Item(7, "77889", "Printer", 500.0, true);
        Item item2 = new Item(8, "88990", "Scanner", 300.0, true);
        itemManager.registerItem(item1);
        itemManager.registerItem(item2);

        purchaseManager.registerPurchase(7, 1007, 7, 1, 500.0);
        purchaseManager.registerPurchase(8, 1008, 8, 2, 600.0);

        List<Purchase> purchases = purchaseManager.getAllPurchases();
        assertEquals(2, purchases.size());
    }
}
