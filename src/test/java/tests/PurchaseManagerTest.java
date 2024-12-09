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

    @BeforeAll
    public void setUp() {
        PurchaseRepository purchaseRepository = new PurchaseRepository(); // MongoDB Repository
        PurchaseRepoJsonb purchaseRepositoryJsonb = new PurchaseRepoJsonb(); // Redis Repository
        itemManager = new ItemManager(new ItemRepository());
        purchaseManager = new PurchaseManager(purchaseRepository, purchaseRepositoryJsonb, itemManager);
    }

    @BeforeEach
    public void cleanDatabase() {
        purchaseManager.getDatabase().drop(); // Clean MongoDB
        //purchaseManager.getPurchaseByIdRedis(null); // Clean Redis (Optional)
    }

    @Test
    public void testSavePurchaseMongoAndRedis() {
        ObjectId id = new ObjectId();
        ObjectId clientId = new ObjectId();
        ObjectId itemId = new ObjectId();
        itemManager.registerItem(itemId, "Test Item", 50, "what", true);

        purchaseManager.registerPurchase(id, clientId, itemId, 10, 100.50);

        // Verify in MongoDB
        List<Purchase> purchases = purchaseManager.getAllPurchases();
        assertFalse(purchases.isEmpty(), "The purchase collection should not be empty.");
        Purchase retrievedPurchase = purchases.get(0);
        assertEquals(clientId, retrievedPurchase.getClientId());
        assertEquals(itemId, retrievedPurchase.getItemId());
        assertEquals(10, retrievedPurchase.getAmount());
        assertEquals(100.50, retrievedPurchase.getTotalCost());

        // Verify in Redis
        assertNotNull(purchaseManager.getPurchaseByIdRedis(id), "Purchase should exist in Redis.");
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

        // Verify in MongoDB
        List<Purchase> purchases = purchaseManager.getAllPurchases();
        assertEquals(1, purchases.size());

        Purchase retrievedPurchase = purchaseManager.getPurchaseById(id);
        assertNotNull(retrievedPurchase);
        assertEquals(clientId, retrievedPurchase.getClientId());
        assertEquals(itemId, retrievedPurchase.getItemId());
        assertEquals(5, retrievedPurchase.getAmount());
        assertEquals(50.25, retrievedPurchase.getTotalCost());

        // Verify in Redis
        assertNotNull(purchaseManager.getPurchaseByIdRedis(id));
    }

    @Test
    public void testDeletePurchase() {
        ObjectId id = new ObjectId();
        ObjectId clientId = new ObjectId();
        ObjectId itemId = new ObjectId();
        itemManager.registerItem(itemId, "Test Item", 50, "what", true);
        purchaseManager.registerPurchase(id, clientId, itemId, 3, 30.99);

        // Verify existence
        List<Purchase> purchases = purchaseManager.getAllPurchases();
        assertFalse(purchases.isEmpty());

        // Delete from both MongoDB and Redis
        purchaseManager.deletePurchase(id);

        // Verify deletion in MongoDB
        Purchase deletedPurchase = purchaseManager.getPurchaseById(id);
        assertNull(deletedPurchase);

        // Verify deletion in Redis
        assertNull(purchaseManager.getPurchaseByIdRedis(id));
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
        assertNotNull(purchaseManager.getPurchaseByIdRedis(id1));
        assertNotNull(purchaseManager.getPurchaseByIdRedis(id2));
    }

    @Test
    public void testRegisterPurchaseWithUnavailableItem() {
        ObjectId id = new ObjectId();
        ObjectId clientId = new ObjectId();
        ObjectId itemId = new ObjectId();
        itemManager.registerItem(itemId, "Unavailable Item", 50, "what", false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            purchaseManager.registerPurchase(id, clientId, itemId, 1, 100.0);
        });

        assertEquals("Item must be available for purchase", exception.getMessage());
    }
}
