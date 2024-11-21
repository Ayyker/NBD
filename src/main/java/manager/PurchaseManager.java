
package manager;

import model.Item;
import model.Purchase;
import org.bson.types.ObjectId;
import repository.PurchaseRepository;

import java.util.List;
import java.util.Set;

public class PurchaseManager {

    private final PurchaseRepository purchaseRepository;

    public PurchaseManager(PurchaseRepository purchaseRepository) {
        this.purchaseRepository = purchaseRepository;
    }

    public Purchase registerPurchase(ObjectId id, ObjectId clientId, List<ObjectId> itemIds, double totalCost) {
        Purchase purchase = new Purchase(id, clientId, itemIds, totalCost);
        try {
            purchaseRepository.saveOrUpdate(purchase);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to register purchase", e);
        }
        return purchase;
    }

    public void updatePurchase(ObjectId purchaseId, Set<Item> newItems, boolean newPendingStatus) {
        Purchase purchase = purchaseRepository.findById(purchaseId);
        if (purchase == null) {
            throw new IllegalArgumentException("Purchase with ID " + purchaseId + " not found.");
        }

        try {
            purchaseRepository.saveOrUpdate(purchase);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to update purchase", e);
        }
    }

    public void removePurchase(ObjectId purchaseId) {
        Purchase purchase = purchaseRepository.findById(purchaseId);
        if (purchase == null) {
            throw new IllegalArgumentException("Purchase with ID " + purchaseId + " not found.");
        }
        try {
            purchaseRepository.delete(purchaseId);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to delete purchase", e);
        }
    }

}
