package repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import model.Item;

import java.util.List;

public class ItemRepository {

    @PersistenceContext
    private final EntityManager entityManager;
    public ItemRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Item findById(Long id) {
        return entityManager.find(Item.class, id);
    }

    // Znalezienie przedmiotu po jego unikalnym identyfikatorze z blokadą optymistyczną
    @Transactional
    public List<Item> findByItemID(String itemID) {
        return entityManager.createQuery(
                        "SELECT i FROM Item i WHERE i.itemID = :itemID", Item.class)
                .setParameter("itemID", itemID)
                .setLockMode(LockModeType.OPTIMISTIC)  // Zastosowanie blokady optymistycznej
                .getResultList();
    }

    // Znalezienie przedmiotów, które są dostępne, z blokadą optymistyczną
    @Transactional
    public List<Item> findByAvailable(boolean available) {
        return entityManager.createQuery(
                        "SELECT i FROM Item i WHERE i.available = :available", Item.class)
                .setParameter("available", available)
                .setLockMode(LockModeType.OPTIMISTIC)  // Zastosowanie blokady optymistycznej
                .getResultList();
    }

    // Aktualizacja kosztu przedmiotu z blokadą optymistyczną
    @Transactional
    public void updateItemCost(Long id, double itemCost) {
        Item item = entityManager.find(Item.class, id, LockModeType.OPTIMISTIC);  // Blokada optymistyczna
        if (item != null) {
            item.setItemCost(itemCost);
            entityManager.merge(item);  // Aktualizacja z zarządzaniem wersją
            entityManager.flush();  // Wymuszenie natychmiastowego zapisu
        }
    }

    // Zapis lub aktualizacja przedmiotu z blokadą optymistyczną
    @Transactional
    public void saveOrUpdate(Item item) {
        if (item.getId() == null) {
            entityManager.persist(item);  // Zapis nowego przedmiotu
        } else {
            entityManager.merge(item);  // Aktualizacja istniejącego przedmiotu
        }
        entityManager.flush();  // Wymuszenie natychmiastowego zapisu
    }

    // Usunięcie przedmiotu
    @Transactional
    public void delete(Item item) {
        if (entityManager.contains(item)) {
            entityManager.remove(item);  // Usunięcie przedmiotu
        } else {
            Item attachedItem = entityManager.find(Item.class, item.getId());
            if (attachedItem != null) {
                entityManager.remove(attachedItem);
            }
        }
        entityManager.flush();  // Wymuszenie natychmiastowego zapisu
    }

    public void buyItem(Long id) {
        Item item = entityManager.find(Item.class, id, LockModeType.OPTIMISTIC); // Pobieramy z blokadą optymistyczną
        if (item != null) {
            if (!item.isAvailable()) {
                throw new IllegalStateException("Item is not available for purchase.");
            }
            item.setAvailable(false); // Oznacz jako kupiony (niedostępny)
            entityManager.merge(item);
        }
    }
}
