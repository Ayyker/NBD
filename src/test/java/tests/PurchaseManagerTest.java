package tests;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import manager.PurchaseManager;
import model.Client;
import model.IndividualClient;
import model.Item;
import model.Purchase;
import org.junit.jupiter.api.*;
import repository.PurchaseRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PurchaseManagerTest {

    private static EntityManagerFactory emf;
    private static EntityManager em;
    private PurchaseManager purchaseManager;
    private PurchaseRepository purchaseRepository;

    // Klienci i przedmioty
    private Client client1;
    private Client client2;
    private Item item1;
    private Item item2;

    @BeforeAll
    static void setup() {
        emf = Persistence.createEntityManagerFactory("default");
        em = emf.createEntityManager();
    }

    @BeforeEach
    void init() {
        purchaseRepository = new PurchaseRepository(em);
        purchaseManager = new PurchaseManager(em);

        em.getTransaction().begin();

        // Tworzenie klientów
        client1 = new IndividualClient("Jan", "Kowalski", "12345678910", "Warszawa");
        client2 = new IndividualClient("Anna", "Nowak", "98765432110", "Kraków");
        em.persist(client1);
        em.persist(client2);

        // Tworzenie przedmiotów
        item1 = new Item("Laptop", 3000.0, "ITEM001", true);
        item2 = new Item("Smartphone", 1500.0, "ITEM002", true);
        em.persist(item1);
        em.persist(item2);

        em.getTransaction().commit();
    }

    @Test
    void testRegisterPurchase() {
        em.getTransaction().begin();

        // Rejestracja nowego zakupu dla klienta 1
        Set<Item> items = Set.of(item1, item2);
        Purchase purchase = purchaseManager.registerPurchase(client1, items, true);

        assertNotNull(purchase.getId());
        assertEquals(2, purchase.getItems().size());
        assertTrue(purchase.isPending());

        em.getTransaction().commit();

        // Sprawdzamy, czy zakup został zapisany do bazy danych
        Purchase foundPurchase = purchaseRepository.findById(purchase.getId());
        assertNotNull(foundPurchase);
    }

    @Test
    void testUpdatePurchase() {
        em.getTransaction().begin();

        // Rejestracja nowego zakupu dla klienta 1
        Set<Item> items = Set.of(item1);
        Purchase purchase = purchaseManager.registerPurchase(client1, items, true);

        em.getTransaction().commit();

        // Aktualizacja zakupu
        em.getTransaction().begin();
        Set<Item> newItems = new HashSet<>(purchase.getItems());
        newItems.add(item2); // Dodajemy nowy przedmiot
        purchaseManager.updatePurchase(purchase.getId(), newItems, false);
        em.getTransaction().commit();

        // Sprawdzamy, czy zakup został zaktualizowany
        em.getTransaction().begin();
        Purchase updatedPurchase = purchaseRepository.findById(purchase.getId());
        assertEquals(2, updatedPurchase.getItems().size());
        assertFalse(updatedPurchase.isPending());
        em.getTransaction().commit();
    }


    @Test
    void testRemovePurchase() {
        em.getTransaction().begin();

        // Rejestracja nowego zakupu dla klienta 1
        Set<Item> items = Set.of(item1);
        Purchase purchase = purchaseManager.registerPurchase(client1, items, true);

        em.getTransaction().commit();

        // Usuwamy zakup
        em.getTransaction().begin();
        purchaseManager.removePurchase(purchase.getId());
        em.getTransaction().commit();

        // Sprawdzamy, czy zakup został usunięty
        em.getTransaction().begin();
        Purchase removedPurchase = purchaseRepository.findById(purchase.getId());
        assertNull(removedPurchase);
        em.getTransaction().commit();
    }

    @AfterEach


    @AfterAll
    static void tearDown() {
        if (emf != null) {
            emf.close();
        }
    }
}
