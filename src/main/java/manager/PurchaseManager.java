package manager;

import model.Purchase;
import repository.PurchaseRepository;

import java.util.List;

public class PurchaseManager {

    private final PurchaseRepository purchaseRepository;
    private final ItemManager itemManager;

    public PurchaseManager(PurchaseRepository purchaseRepository, ItemManager itemManager) {
        this.purchaseRepository = purchaseRepository;
        this.itemManager = itemManager;
    }

    public void registerPurchase(int id, int clientId, int itemId, int amount, double totalCost) {
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

        purchaseRepository.registerPurchase(newPurchase);
    }

    public List<Purchase> getAllPurchases() {
        return purchaseRepository.getAllIPurchases();
    }

    public Purchase getPurchaseById(int purchaseId) {
        return purchaseRepository.getPurchaseById(purchaseId);
    }

    public void deletePurchase(int purchaseId) {
        purchaseRepository.removePurchase(purchaseId);
    }

    public void updatePurchase(Purchase updatedPurchase) {
        purchaseRepository.updatePurchase(updatedPurchase);
    }
}
