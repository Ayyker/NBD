package manager;

import jakarta.persistence.EntityManager;
import model.Client;
import model.Item;
import model.Purchase;
import repository.PurchaseRepository;

import java.util.List;
import java.util.Set;

public class PurchaseManager {

    private final PurchaseRepository purchaseRepository;

    public PurchaseManager(EntityManager em) {
        this.purchaseRepository = new PurchaseRepository(em);
    }

    public Purchase registerPurchase(Client client, Set<Item> items, boolean pending) {
        Purchase purchase = new Purchase();
        purchase.setClient(client);
        purchase.setItems(items);
        purchase.setPending(pending);
        purchaseRepository.saveOrUpdate(purchase);
        return purchase;
    }

    public void updatePurchase(Long purchaseId, Set<Item> newItems, boolean newPendingStatus) {
        Purchase purchase = purchaseRepository.findById(purchaseId);
        if (purchase == null) {
            throw new IllegalArgumentException("Purchase with ID " + purchaseId + " not found.");
        }

        purchase.setItems(newItems);
        purchase.setPending(newPendingStatus);

        purchaseRepository.saveOrUpdate(purchase);
    }

    public List<Purchase> getPurchasesByClient(Client client) {
        return purchaseRepository.findByCustomer(client);
    }

    public List<Purchase> getPurchasesByItem(Item item) {
        return purchaseRepository.findByItems(item);
    }

    public void removePurchase(Long purchaseId) {
        Purchase purchase = purchaseRepository.findById(purchaseId);
        if (purchase == null) {
            throw new IllegalArgumentException("Purchase with ID " + purchaseId + " not found.");
        }

        purchaseRepository.delete(purchase);
    }

}
