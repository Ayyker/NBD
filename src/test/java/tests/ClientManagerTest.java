package tests;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import manager.ClientManager;
import model.BusinessClient;
import model.Client;
import model.IndividualClient;
import org.junit.jupiter.api.*;
import repository.ClientRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ClientManagerTest {

    private static EntityManagerFactory emf;
    private static EntityManager em;
    private ClientManager clientManager;
    private ClientRepository clientRepository;

    @BeforeAll
    static void setup() {
        emf = Persistence.createEntityManagerFactory("default");
        em = emf.createEntityManager();
    }

    @BeforeEach
    void init() {
        clientRepository = new ClientRepository(em);
        clientManager = new ClientManager(em);
    }

    @Test
    void testRegisterIndividualClient() {
        em.getTransaction().begin();

        // Rejestracja nowego klienta indywidualnego
        IndividualClient individualClient = clientManager.registerIndividualClient("Jan", "Kowalski", "12345678910", "Warszawa");

        assertNotNull(individualClient.getId());
        assertEquals("Jan", individualClient.getFirstName());
        assertEquals("Kowalski", individualClient.getLastName());

        em.getTransaction().commit();

        // Sprawdzamy, czy klient został zapisany do bazy
        Client foundClient = clientRepository.findById(individualClient.getId());
        assertNotNull(foundClient);
        assertTrue(foundClient instanceof IndividualClient);
    }

    @Test
    void testRegisterBusinessClient() {
        em.getTransaction().begin();

        // Rejestracja nowego klienta biznesowego
        BusinessClient businessClient = clientManager.registerBusinessClient("Tech Corp", "987654321101112", "Kraków", 10.0);

        assertNotNull(businessClient.getId());
        assertEquals("Tech Corp", businessClient.getCompanyName());
        assertEquals("987654321101112", businessClient.getNipID());

        em.getTransaction().commit();

        // Sprawdzamy, czy klient został zapisany do bazy
        Client foundClient = clientRepository.findById(businessClient.getId());
        assertNotNull(foundClient);
        assertInstanceOf(BusinessClient.class, foundClient);
    }

    @Test
    void testGetAllClients() {
        em.getTransaction().begin();

        // Rejestracja dwóch klientów
        clientManager.registerIndividualClient("Jan", "Kowalski", "12345678910", "Warszawa");
        clientManager.registerBusinessClient("Tech Corp", "987654321101112", "Kraków", 10.0);

        em.getTransaction().commit();

        // Pobranie wszystkich klientów
        List<Client> allClients = clientManager.getAllClients();
        assertEquals(2, allClients.size());
    }

    @Test
    void testUpdateClient() {
        em.getTransaction().begin();

        // Rejestracja nowego klienta indywidualnego
        IndividualClient individualClient = clientManager.registerIndividualClient("Jan", "Kowalski", "12345678910", "Warszawa");

        em.getTransaction().commit();

        // Aktualizacja danych klienta
        em.getTransaction().begin(); // Musimy zacząć nową transakcję
        clientManager.updateClient(individualClient.getId(), "Kraków", "Janusz", "Nowak");
        em.getTransaction().commit();

        // Sprawdzamy, czy dane klienta zostały zaktualizowane
        em.getTransaction().begin();
        IndividualClient updatedClient = (IndividualClient) clientRepository.findById(individualClient.getId());
        assertEquals("Kraków", updatedClient.getAddress());
        assertEquals("Janusz", updatedClient.getFirstName());
        assertEquals("Nowak", updatedClient.getLastName());
        em.getTransaction().commit();
    }

    @Test
    void testRemoveClient() {
        em.getTransaction().begin();

        // Rejestracja nowego klienta indywidualnego
        IndividualClient individualClient = clientManager.registerIndividualClient("Jan", "Kowalski", "12345678910", "Warszawa");

        em.getTransaction().commit();

        // Usunięcie klienta
        em.getTransaction().begin();
        clientManager.removeClient(individualClient.getId());
        em.getTransaction().commit();

        // Sprawdzamy, czy klient został usunięty
        em.getTransaction().begin();
        Client removedClient = clientRepository.findById(individualClient.getId());
        assertNull(removedClient);
        em.getTransaction().commit();
    }

    @AfterEach
    void cleanUp() {
        em.getTransaction().begin();
        em.createQuery("DELETE FROM Client").executeUpdate();  // Usuwanie tylko z tabeli 'Client'
        em.getTransaction().commit();
    }

    @AfterAll
    static void tearDown() {
        if (emf != null) {
            emf.close();
        }
    }
}
