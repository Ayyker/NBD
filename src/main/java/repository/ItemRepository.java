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

    @Transactional
    public List<Item> findByItemID(String itemID) {
        return entityManager.createQuery(
                        "SELECT i FROM Item i WHERE i.itemID = :itemID", Item.class)
                .setParameter("itemID", itemID)
                .setLockMode(LockModeType.OPTIMISTIC)
                .getResultList();
    }

    @Transactional
    public List<Item> findByAvailable(boolean available) {
        return entityManager.createQuery(
                        "SELECT i FROM Item i WHERE i.available = :available", Item.class)
                .setParameter("available", available)
                .setLockMode(LockModeType.OPTIMISTIC)
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

    @Transactional
    public void saveOrUpdate(Item item) {
        if (item.getId() == null) {
            entityManager.persist(item);
        } else {
            entityManager.merge(item);
        }
        entityManager.flush();
    }

    @Transactional
    public void delete(Item item) {
        if (entityManager.contains(item)) {
            entityManager.remove(item);
        } else {
            Item attachedItem = entityManager.find(Item.class, item.getId());
            if (attachedItem != null) {
                entityManager.remove(attachedItem);
            }
        }
        entityManager.flush();
    }

    public void buyItem(Long id) {
        Item item = entityManager.find(Item.class, id, LockModeType.OPTIMISTIC);
        if (item != null) {
            if (!item.isAvailable()) {
                throw new IllegalStateException("Item is not available for purchase.");
            }
            item.setAvailable(false);
            entityManager.merge(item);
        }
    }
}
