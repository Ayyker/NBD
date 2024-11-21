package tests;

import manager.ClientManager;
import model.BusinessClient;
import model.Client;
import model.IndividualClient;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.*;
import repository.ClientRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ClientManagerTest {

    private static ClientManager clientManager;
    private static ClientRepository clientRepository;

    @BeforeAll
    static void setup() {
        clientRepository = new ClientRepository();
        clientManager = new ClientManager(clientRepository);
    }

    @BeforeEach
    void init() {
        clientManager.getDatabase().drop(); // Drop the database to reset the state between tests
    }

    @Test
    void testRegisterIndividualClient() {
        ObjectId id = new ObjectId();

        // Rejestracja nowego klienta indywidualnego
        IndividualClient individualClient = clientManager.registerIndividualClient(id, "Jan", "Kowalski", "12345678910", "Warszawa");

        assertNotNull(individualClient.getId());
        assertEquals("Jan", individualClient.getFirstName());
        assertEquals("Kowalski", individualClient.getLastName());

        List<Client> allClients = clientManager.getAllClients();
        assertEquals(1, allClients.size());

        Client retrieved = allClients.get(0);
        assertInstanceOf(IndividualClient.class, retrieved);
        assertEquals(id, retrieved.getId());
        assertEquals("Warszawa", retrieved.getAddress());
    }

    @Test
    void testRegisterBusinessClient() {
        ObjectId id = new ObjectId();

        // Rejestracja nowego klienta biznesowego
        BusinessClient businessClient = clientManager.registerBusinessClient(id, "Tech Corp", "987654321101112", "Kraków", 10.0);

        assertNotNull(businessClient.getId());
        assertEquals("Tech Corp", businessClient.getCompanyName());
        assertEquals("987654321101112", businessClient.getNipID());

        // Sprawdzamy, czy klient został zapisany do bazy
        Client foundClient = clientRepository.findById(businessClient.getId());
        assertNotNull(foundClient);
        assertInstanceOf(BusinessClient.class, foundClient);
    }

    @Test
    void testGetAllClients() {
        // Rejestracja dwóch klientów
        clientManager.registerIndividualClient(new ObjectId(), "Jan", "Kowalski", "12345678910", "Warszawa");

        clientManager.registerBusinessClient(new ObjectId(), "Tech Corp", "987654321101112", "Kraków", 10.0);

        // Pobranie wszystkich klientów
        List<Client> allClients = clientManager.getAllClients();
        assertEquals(2, allClients.size());
    }

//    @Test
//    void testUpdateClient() {
//        ObjectId id = new ObjectId();
//
//        // Rejestracja nowego klienta indywidualnego
//        IndividualClient individualClient = clientManager.registerIndividualClient(id, "Jan", "Kowalski", "12345678910", "Warszawa");
//
//        // Aktualizacja danych klienta
//        clientManager.updateClient(individualClient.getId(), "Kraków", "Janusz", "Nowak");
//
//        // Sprawdzamy, czy dane klienta zostały zaktualizowane
//        IndividualClient updatedClient = (IndividualClient) clientRepository.findById(individualClient.getId());
//        assertEquals("Kraków", updatedClient.getAddress());
//        assertEquals("Janusz", updatedClient.getFirstName());
//        assertEquals("Nowak", updatedClient.getLastName());
//    }

    @Test
    void testRemoveClient() {
        ObjectId id = new ObjectId();

        // Rejestracja nowego klienta indywidualnego
        IndividualClient individualClient = clientManager.registerIndividualClient(id, "Jan", "Kowalski", "12345678910", "Warszawa");

        // Usunięcie klienta
        clientManager.removeClient(individualClient.getId());

        // Sprawdzamy, czy klient został usunięty
        Client removedClient = clientRepository.findById(individualClient.getId());
        assertNull(removedClient);
    }

    @AfterEach
    void cleanUp() {
        clientManager.getDatabase().drop(); // Usuwanie wszystkich danych z bazy (reset przed każdym testem)
    }

    @AfterAll
    static void tearDown() {
        // any clean-up routines if needed, for example, closing a MongoDB connection
    }
}