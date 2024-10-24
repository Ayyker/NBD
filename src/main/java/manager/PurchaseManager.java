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

    // Rejestracja nowego zakupu
    public Purchase registerPurchase(Client client, Set<Item> items, boolean pending) {
        Purchase purchase = new Purchase();
        purchase.setClient(client);
        purchase.setItems(items);
        purchase.setPending(pending);
        purchaseRepository.saveOrUpdate(purchase); // Zapisujemy zakup do bazy
        return purchase;
    }

    // Aktualizacja istniejącego zakupu
    public void updatePurchase(Long purchaseId, Set<Item> newItems, boolean newPendingStatus) {
        // Znalezienie zakupu po ID
        Purchase purchase = purchaseRepository.findById(purchaseId);
        if (purchase == null) {
            throw new IllegalArgumentException("Purchase with ID " + purchaseId + " not found.");
        }

        // Aktualizacja właściwości zakupu
        purchase.setItems(newItems);  // Zaktualizowanie listy przedmiotów
        purchase.setPending(newPendingStatus);  // Zaktualizowanie statusu "pending"

        // Zapis zaktualizowanego zakupu
        purchaseRepository.saveOrUpdate(purchase);
    }

    // Pobranie zakupów przypisanych do klienta
    public List<Purchase> getPurchasesByClient(Client client) {
        return purchaseRepository.findByCustomer(client);
    }

    // Pobranie zakupów zawierających dany przedmiot
    public List<Purchase> getPurchasesByItem(Item item) {
        return purchaseRepository.findByItems(item);
    }

    // Usunięcie zakupu
    public void removePurchase(Long purchaseId) {
        // Znalezienie zakupu po ID
        Purchase purchase = purchaseRepository.findById(purchaseId);
        if (purchase == null) {
            throw new IllegalArgumentException("Purchase with ID " + purchaseId + " not found.");
        }

        // Usunięcie zakupu
        purchaseRepository.delete(purchase);
    }

}
