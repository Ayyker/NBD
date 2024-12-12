package manager;

import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoDatabase;
import model.Purchase;
import org.bson.types.ObjectId;
import model.PurchaseJsonb;
import repository.PurchaseRepoJsonb;
import repository.PurchaseRepository;

import java.util.List;

public class PurchaseManager {

    private final PurchaseRepository purchaseRepository; // MongoDB Repository
    private final PurchaseRepoJsonb purchaseRepositoryJsonb; // Redis Repository
    private final ItemManager itemManager;

    public PurchaseManager(PurchaseRepository purchaseRepository,
                           PurchaseRepoJsonb purchaseRepositoryJsonb,
                           ItemManager itemManager) {
        this.purchaseRepository = purchaseRepository;
        this.purchaseRepositoryJsonb = purchaseRepositoryJsonb;
        this.itemManager = itemManager;
    }

    public void registerPurchase(ObjectId id, ObjectId clientId, ObjectId itemId, int amount, double totalCost) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Purchase amount must be greater than zero.");
        }
        if (totalCost <= 0) {
            throw new IllegalArgumentException("Total cost must be greater than zero.");
        }

        if (!itemManager.isItemAvailable(itemId)) {
            throw new IllegalArgumentException("Item must be available for purchase.");
        }

        Purchase newPurchase = new Purchase(id, clientId, itemId, amount, totalCost);

        if (purchaseRepositoryJsonb.checkConnection()) {
            // Save in Redis if Redis is available
            PurchaseJsonb jsonbPurchase = new PurchaseJsonb(
                    id.toString(),
                    clientId.toString(),
                    itemId.toString(),
                    amount,
                    totalCost
            );
            purchaseRepositoryJsonb.saveWithTimeout(jsonbPurchase, 300);
        }
        // Then Save to mongoDB
        ClientSession clientSession = purchaseRepository.getClient().startSession();
        try {
            clientSession.startTransaction();
            purchaseRepository.save(newPurchase);
            clientSession.commitTransaction();
        } catch (Exception e) {
            clientSession.abortTransaction();
            throw e;
        } finally {
            clientSession.close();
        }
    }

    public List<Purchase> getAllPurchases() {
        // Najpierw spróbuj pobrać z Redisa
        if (purchaseRepositoryJsonb.checkConnection()) {
            try {
                List<PurchaseJsonb> redisPurchases = purchaseRepositoryJsonb.findAll();
                if (!redisPurchases.isEmpty()) {
                    return redisPurchases.stream()
                            .map(PurchaseJsonb::toPurchase) // Konwersja z JSONB na Purchase
                            .toList();
                }
            } catch (Exception e) {
                System.err.println("Error retrieving purchases from Redis: " + e.getMessage());
            }
        }

        // Jeśli Redis zawiódł, użyj MongoDB
        return purchaseRepository.findAll();
    }

    public Purchase getPurchaseById(ObjectId purchaseId) {
        // Najpierw sprawdź w Redis
        if (purchaseRepositoryJsonb.checkConnection()) {
            try {
                PurchaseJsonb redisPurchase = purchaseRepositoryJsonb.findById(purchaseId);
                if (redisPurchase != null) {
                    return redisPurchase.toPurchase(); // Konwersja z JSONB na Purchase
                }
            } catch (Exception e) {
                System.err.println("Error retrieving purchase from Redis: " + e.getMessage());
            }
        }

        // Jeśli Redis zawiódł, użyj MongoDB
        return purchaseRepository.findById(purchaseId);
    }



    public void deletePurchase(ObjectId purchaseId) {
        if (purchaseRepositoryJsonb.checkConnection()) {
            // Delete from Redis
            purchaseRepositoryJsonb.delete(purchaseId);
        }

        // Always ensure it's deleted from MongoDB
        ClientSession clientSession = purchaseRepository.getClient().startSession();
        try {
            clientSession.startTransaction();
            purchaseRepository.delete(purchaseId);
            clientSession.commitTransaction();
        } catch (Exception e) {
            clientSession.abortTransaction();
            throw e;
        } finally {
            clientSession.close();
        }
    }

    public void updatePurchase(Purchase updatedPurchase) {
        if (purchaseRepositoryJsonb.checkConnection()) {
            // Update Redis if Redis is available
            PurchaseJsonb jsonbPurchase = new PurchaseJsonb(
                    updatedPurchase.getId().toString(),
                    updatedPurchase.getClientId().toString(),
                    updatedPurchase.getItemId().toString(),
                    updatedPurchase.getAmount(),
                    updatedPurchase.getTotalCost()
            );
            purchaseRepositoryJsonb.update(jsonbPurchase);
        }

        // Always update MongoDB
        ClientSession clientSession = purchaseRepository.getClient().startSession();
        try {
            clientSession.startTransaction();
            purchaseRepository.update(updatedPurchase);
            clientSession.commitTransaction();
        } catch (Exception e) {
            clientSession.abortTransaction();
            throw e;
        } finally {
            clientSession.close();
        }
    }

    public MongoDatabase getDatabase() {
        return purchaseRepository.getDatabase();
    }
}
