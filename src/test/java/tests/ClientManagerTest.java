//package tests;
//
//import manager.ClientManager;
//import model.BusinessClient;
//import model.Client;
//import model.IndividualClient;
//import repository.ClientRepository;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//class ClientManagerTest {
//
//    private static ClientManager clientManager;
//    private static ClientRepository clientRepository;
//
//    @BeforeAll
//    static void setup() {
//        clientRepository = new ClientRepository();
//        clientManager = new ClientManager(clientRepository);
//    }
//
//    @BeforeEach
//    void init() {
//        clientManager.getDatabase().drop(); // Drop the database to reset the state between tests
//    }
//
//    @Test
//    void testRegisterIndividualClient() {
//        clientManager.registerIndividualClient(new ObjectId(), "Jan", "Kowalski", "12345678910", "Warszawa");
//        List<Client> allClients = clientManager.getAllClients();
//
//        assertEquals(1, allClients.size());
//        assertInstanceOf(IndividualClient.class, allClients.get(0));
//        IndividualClient individualClient = (IndividualClient) allClients.get(0);
//        assertEquals("Jan", individualClient.getFirstName());
//        assertEquals("Warszawa", allClients.get(0).getAddress());
//    }
//
//    @Test
//    void testRegisterBusinessClient() {
//        clientManager.registerBusinessClient(new ObjectId(), "Tech Corp", "987654321101112", "Kraków", 10.0);
//        List<Client> allClients = clientManager.getAllClients();
//
//        assertEquals(1, allClients.size());
//        assertInstanceOf(BusinessClient.class, allClients.get(0));
//        BusinessClient businessClient = (BusinessClient) allClients.get(0);
//        assertEquals("Tech Corp", businessClient.getCompanyName());
//        assertEquals("Kraków", allClients.get(0).getAddress());
//    }
//
//    @Test
//    void testGetAllClients() {
//        clientManager.registerIndividualClient(new ObjectId(), "Jan", "Kowalski", "12345678910", "Warszawa");
//        clientManager.registerBusinessClient(new ObjectId(), "Tech Corp", "987654321101112", "Kraków", 10.0);
//
//        List<Client> allClients = clientManager.getAllClients();
//        assertEquals(2, allClients.size());
//    }
//
//    @Test
//    void testRemoveClient() {
//        ObjectId id = new ObjectId();
//
//        IndividualClient individualClient = clientManager.registerIndividualClient(id, "Jan", "Kowalski", "12345678910", "Warszawa");
//
//        clientManager.removeClient(individualClient.getId());
//        Client removedClient = clientRepository.findById(individualClient.getId());
//        assertNull(removedClient);
//    }
//
//}