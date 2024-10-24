package repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import model.Client;
import model.Purchase;
import model.Item;

import java.util.List;

public class PurchaseRepository {
    @PersistenceContext
    private final EntityManager entityManager;

    public PurchaseRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Purchase findById(Long id) {
        return entityManager.find(Purchase.class, id);
    }


    // Znalezienie zakupów, które obejmują określony przedmiot
    public List<Purchase> findByItems(Item item) {
        return entityManager.createQuery(
                        "SELECT p FROM Purchase p JOIN p.items i WHERE i = :item", Purchase.class)
                .setParameter("item", item)
                .getResultList();
    }

    // Znalezienie zakupów, które zawierają jeden z podanych przedmiotów
    public List<Purchase> findByItemsIn(List<Item> items) {
        return entityManager.createQuery(
                        "SELECT p FROM Purchase p JOIN p.items i WHERE i IN :items", Purchase.class)
                .setParameter("items", items)
                .getResultList();
    }


    // Znalezienie zakupów przypisanych do konkretnego klienta
    public List<Purchase> findByCustomer(Client client) {
        return entityManager.createQuery(
                        "SELECT p FROM Purchase p WHERE p.client = :client", Purchase.class)
                .setParameter("client", client)
                .getResultList();
    }

    // Zapis lub aktualizacja zakupu
    @Transactional
    public void saveOrUpdate(Purchase purchase) {
        if (purchase.getId() == null) {
            entityManager.persist(purchase);
        } else {
            entityManager.find(Purchase.class, purchase.getId(), LockModeType.OPTIMISTIC); // Dodana blokada optymistyczna
            entityManager.merge(purchase);
        }
    }

    // Usunięcie zakupu
    @Transactional
    public void delete(Purchase purchase) {
        Purchase attachedPurchase = entityManager.find(Purchase.class, purchase.getId());
        if (attachedPurchase != null) {
            entityManager.remove(attachedPurchase);
        }
    }

}