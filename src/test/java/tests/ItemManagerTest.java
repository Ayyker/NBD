package tests;

import manager.ItemManager;
import model.Item;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.*;
import repository.ItemRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ItemManagerTest {

    private ItemRepository itemRepository;
    private ItemManager itemManager;

    @BeforeEach
    void setUp() {
        itemRepository = new ItemRepository();
        itemManager = new ItemManager(itemRepository);
        itemManager.getDatabase().drop();
    }

    @Test
    void testRegisterItem() {
        ObjectId id = new ObjectId();
        String name = "New Item";
        double cost = 50.0;
        String itemID = "67890";
        boolean available = true;

        Item result = itemManager.registerItem(id, name, cost, itemID, available);

        List<Item> retrieved = itemManager.getAllItems();
        assertEquals(1, retrieved.size());
        assertEquals(result.getItemName(), retrieved.getFirst().getItemName());
    }

    @Test
    void testGetAvailableItems()
    {
        Item item1 = new Item(new ObjectId(), "123", "Item 1", 20.0, true);
        Item item2 = new Item(new ObjectId(), "124", "Item 2", 30.0, false);
        Item item3 = new Item(new ObjectId(), "125", "Item 3", 40.0, true);

        itemManager.registerItem(item1);
        itemManager.registerItem(item2);
        itemManager.registerItem(item3);

        List<Item> availableItems = itemManager.getAvailableItems(true);

        assertEquals(2, availableItems.size());
        assertEquals("Item 1", availableItems.get(0).getItemName());
        assertEquals("Item 3", availableItems.get(1).getItemName());
    }

//    @Test
//    void testGetItemByItemID() {
//        em.getTransaction().begin();
//
//        itemManager.registerItem(new ObjectId(),"Smartphone", 800.0, "ITEM002", true);
//
//        List<Item> foundItems = itemManager.getItemByItemID();
//        assertFalse(foundItems.isEmpty());
//        assertEquals("Smartphone", foundItems.getFirst().getItemName());
//
//        em.getTransaction().commit();
//    }
//
//    @Test
//    void testUpdateItemCost() {
//        em.getTransaction().begin();
//
//        // Korzystamy z ItemType jako enum
//        Item item = itemManager.registerItem("Tablet", 500.0, "ITEM003", true);
//
//        itemManager.updateItemCost(item, 600.0);
//        List<Item> updatedItems = itemManager.getItemByItemID("ITEM003");
//        assertEquals(600.0, updatedItems.getFirst().getItemCost());
//
//        em.getTransaction().commit();
//    }
//
//    @Test
//    void testRemoveItem() {
//        em.getTransaction().begin();
//
//        // Korzystamy z ItemType jako enum
//        Item item = itemManager.registerItem("Monitor", 300.0, "ITEM004", true);
//
//        itemManager.removeItem(item);
//        List<Item> removedItems = itemManager.getItemByItemID("ITEM004");
//        assertTrue(removedItems.isEmpty());
//
//        em.getTransaction().commit();
//    }
//
//    @Test
//    void testBuyItem_Success() {
//        em.getTransaction().begin();
//
//        // Rejestracja przedmiotu
//        Item item = itemManager.registerItem("Laptop", 1000.0, "ITEM001", true);
//
//        // Kupowanie przedmiotu (ustawiamy dostępność na false)
//        itemManager.buyItem(item);
//        em.getTransaction().commit();
//
//        em.getTransaction().begin();
//        // Pobieramy przedmiot ponownie, aby sprawdzić, czy jest niedostępny
//        Item updatedItem = itemRepository.findByItemID("ITEM001").getFirst();
//        assertFalse(updatedItem.isAvailable()); // Sprawdzamy, czy jest niedostępny
//        em.getTransaction().commit();
//    }
//
//    @Test
//    void testBuyItem_AlreadyUnavailable() {
//        em.getTransaction().begin();
//
//        // Rejestracja przedmiotu
//        Item item = itemManager.registerItem("Laptop", 1000.0, "ITEM002", false); // Item już niedostępny
//        em.getTransaction().commit();
//
//        // Próba kupienia przedmiotu, który jest już niedostępny
//        em.getTransaction().begin();
//        assertThrows(IllegalStateException.class, () -> {
//            itemManager.buyItem(item);
//
//        });
//        em.getTransaction().rollback();
//    }
//
//    @Test
//    void testRegisterItemWithDuplicateID() {
//        em.getTransaction().begin();
//
//        // Rejestracja pierwszego przedmiotu
//        Item item1 = itemManager.registerItem("Laptop", 1000.0, "ITEM001", true);
//        assertNotNull(item1.getId());
//
//        // Próba rejestracji drugiego przedmiotu z tym samym ID
//        Exception exception = assertThrows(Exception.class, () -> {
//            itemManager.registerItem("Smartphone", 800.0, "ITEM001", true);
//        });
//
//        em.getTransaction().commit();
//
//        assertNotNull(exception);
//    }
//
//
//    @AfterEach
//    void cleanUp() {
//        em.getTransaction().begin();
//        em.createQuery("DELETE FROM Item").executeUpdate();  // Usuwanie tylko z tabeli 'Item'
//        em.getTransaction().commit();
//    }
//
//    @AfterAll
//    static void tearDown() {
//        if (emf != null) {
//            emf.close();
//        }
//    }
}
