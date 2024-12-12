package tests;

import manager.ItemManager;
import manager.PurchaseManager;
import model.Purchase;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.*;
import repository.PurchaseRepoJsonb;
import repository.ItemRepository;
import repository.PurchaseRepository;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PurchaseManagerTest {

    private PurchaseManager purchaseManager;
    private ItemManager itemManager;
    private PurchaseRepoJsonb purchaseRepoJsonb;

    @BeforeEach
    public void setUp() {
        PurchaseRepository purchaseRepository = new PurchaseRepository(); // MongoDB Repository
        purchaseRepoJsonb = new PurchaseRepoJsonb(); // Redis Repository
        itemManager = new ItemManager(new ItemRepository());
        purchaseManager = new PurchaseManager(purchaseRepository, purchaseRepoJsonb, itemManager);
    }

    @AfterEach
    public void cleanDatabase() {
        purchaseManager.getDatabase().drop(); // Clean MongoDB
        itemManager.getDatabase().drop();
        try {
            purchaseRepoJsonb.clearCache();
        } catch (Exception e) {
            System.err.println("Redis connection failed: " + e.getMessage());
        }
    }

    @Test
    public void testSavePurchaseNegative() {
        ObjectId id1 = new ObjectId();
        ObjectId id2 = new ObjectId();
        ObjectId clientId = new ObjectId();
        ObjectId itemId = new ObjectId();
        assertThrows(IllegalArgumentException.class, () -> purchaseManager.registerPurchase(id1, clientId, itemId, -10, 100.50));
        assertThrows(IllegalArgumentException.class, () -> purchaseManager.registerPurchase(id2, clientId, itemId, 10, -100.50));
    }

    @Test
    public void testFindById() {
        ObjectId id = new ObjectId();
        ObjectId clientId = new ObjectId();
        ObjectId itemId = new ObjectId();
        itemManager.registerItem(itemId, "Test Item", 50, "what", true);
        purchaseManager.registerPurchase(id, clientId, itemId, 5, 50.25);


        Purchase retrievedPurchase = purchaseManager.getPurchaseById(id);
        assertNotNull(retrievedPurchase);
        assertEquals(clientId, retrievedPurchase.getClientId());
        assertEquals(itemId, retrievedPurchase.getItemId());
        assertEquals(5, retrievedPurchase.getAmount());
        assertEquals(50.25, retrievedPurchase.getTotalCost());

        // Verify in Redis
        assertNotNull(purchaseManager.getPurchaseById(id));
    }

    @Test
    public void testDeletePurchase() {
        ObjectId id = new ObjectId();
        ObjectId clientId = new ObjectId();
        ObjectId itemId = new ObjectId();
        itemManager.registerItem(itemId, "Test Item", 50, "what", true);
        purchaseManager.registerPurchase(id, clientId, itemId, 3, 30.99);

        // Delete from both MongoDB and Redis
        purchaseManager.deletePurchase(id);

        // Verify deletion in MongoDB
        Purchase deletedPurchase = purchaseManager.getPurchaseById(id);
        assertNull(deletedPurchase);

        // Verify deletion in Redis
        assertNull(purchaseManager.getPurchaseById(id));
    }

    @Test
    public void testFindAllPurchases() {
        ObjectId id1 = new ObjectId();
        ObjectId id2 = new ObjectId();
        ObjectId clientId = new ObjectId();
        ObjectId itemId1 = new ObjectId();
        ObjectId itemId2 = new ObjectId();
        itemManager.registerItem(itemId1, "Test Item1", 50, "what", true);
        itemManager.registerItem(itemId2, "Test Item2", 50, "what", true);

        purchaseManager.registerPurchase(id1, clientId, itemId1, 2, 20.99);
        purchaseManager.registerPurchase(id2, clientId, itemId2, 4, 40.99);

        // Verify in MongoDB
        List<Purchase> purchases = purchaseManager.getAllPurchases();
        assertTrue(purchases.size() >= 2);

        // Verify in Redis
        assertNotNull(purchaseManager.getPurchaseById(id1));
        assertNotNull(purchaseManager.getPurchaseById(id2));
    }

    @Test
    public void testRegisterPurchaseWithUnavailableItem() {
        ObjectId id = new ObjectId();
        ObjectId clientId = new ObjectId();
        ObjectId itemId = new ObjectId();
        itemManager.registerItem(itemId, "Unavailable Item", 50, "what", false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> purchaseManager.registerPurchase(id, clientId, itemId, 1, 100.0));

        assertEquals("Item must be available for purchase.", exception.getMessage());
    }

    @Test
    public void testUpdatePurchaseAndRefreshCache() {
        ObjectId id = new ObjectId();
        ObjectId clientId = new ObjectId();
        ObjectId itemId = new ObjectId();
        itemManager.registerItem(itemId, "Test Item", 50, "what", true);

        purchaseManager.registerPurchase(id, clientId, itemId, 5, 50.0);

        // Update purchase
        Purchase updatedPurchase = new Purchase(id, clientId, itemId, 10, 100.0);
        purchaseManager.updatePurchase(updatedPurchase);

        // Verify updated data in Redis
        Purchase refreshedPurchase = purchaseManager.getPurchaseById(id);
        assertNotNull(refreshedPurchase);
        assertEquals(10, refreshedPurchase.getAmount());
        assertEquals(100.0, refreshedPurchase.getTotalCost());
    }

//    @Test
//    public void testRegisterPurchaseWithTimeout() throws InterruptedException {
//        ObjectId id = new ObjectId();
//        ObjectId clientId = new ObjectId();
//        ObjectId itemId = new ObjectId();
//        itemManager.registerItem(itemId, "Test Item", 50, "what", true);
//
//        // Register purchase with a 2-second timeout
//        purchaseManager.registerPurchase(id, clientId, itemId, 2, 20.99);
//
//        // Verify in Redis immediately
//        Purchase retrievedPurchase = purchaseManager.getPurchaseById(id);
//        assertNotNull(retrievedPurchase);
//
//        // Wait for timeout and verify it has been removed from Redis
//        Thread.sleep(30000); // Wait 3 seconds to ensure timeout
//        Purchase afterTimeoutPurchase = purchaseManager.getPurchaseById(id);
//        assertNull(afterTimeoutPurchase, "Purchase should expire from Redis after the timeout.");
//    }

}
