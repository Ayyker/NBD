package manager;

import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoDatabase;
import model.Purchase;
import org.bson.types.ObjectId;
import repository.ItemRepository;
import repository.PurchaseRepository;

import java.util.List;

public class PurchaseManager {

    private final PurchaseRepository purchaseRepository;
    private final ItemManager itemManager;

    public PurchaseManager(PurchaseRepository purchaseRepository, ItemManager itemManager) {
        this.purchaseRepository = purchaseRepository;
        this.itemManager = itemManager;
    }

    public void registerPurchase(ObjectId id, ObjectId clientId, ObjectId itemId, int amount, double totalCost) {
        ClientSession clientSession = purchaseRepository.getClient().startSession();
        if (amount <= 0) {
            throw new IllegalArgumentException("Purchase amount must be greater than zero.");
        }
        if (totalCost <= 0) {
            throw new IllegalArgumentException("Total cost must be greater than zero.");
        }

        if (!itemManager.isItemAvailable(itemId)) {
            throw new IllegalArgumentException("Item must be available for purchase");
        }
        Purchase newPurchase = new Purchase(id, clientId, itemId, amount, totalCost);

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
        return purchaseRepository.findAll();
    }

    public Purchase getPurchaseById(ObjectId purchaseId) {
        return purchaseRepository.findById(purchaseId);
    }

    public void deletePurchase(ObjectId purchaseId) {
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