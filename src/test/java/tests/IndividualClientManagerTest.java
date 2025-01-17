package tests;

import Cassandra.SessionFactory;
import com.datastax.oss.driver.api.core.CqlSession;
import manager.IndividualClientManager;
import model.IndividualClient;
import org.junit.jupiter.api.*;
import repository.IndividualClientRepository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class IndividualClientManagerTest {

    private static IndividualClientManager individualClientManager;
    CqlSession session = SessionFactory.initCassandraConnection();

    @BeforeAll
    static void setup() {
        IndividualClientRepository individualClientRepository = new IndividualClientRepository();
        individualClientManager = new IndividualClientManager(individualClientRepository);
    }

    @BeforeEach
    void init() {
        // Ensure the database is cleaned before each test
        session.execute("TRUNCATE TABLE onlineStore.IndividualClients;");
    }

    @Test
    void testRegisterIndividualClient() {
        IndividualClient client = new IndividualClient(1, "12345678901", "Warszawa", "Jan", "Kowalski");
        individualClientManager.register(client);

        List<IndividualClient> clients = individualClientManager.getAll();
        assertEquals(1, clients.size());
        assertEquals("Jan", clients.get(0).getFirstName());
        assertEquals("Kowalski", clients.get(0).getLastName());
    }

    @Test
    void testRemoveIndividualClient() {
        IndividualClient client = new IndividualClient(2, "98765432109", "Krakow", "Anna", "Nowak");
        individualClientManager.register(client);

        individualClientManager.remove(client.getId());

        List<IndividualClient> clients = individualClientManager.getAll();
        assertEquals(0, clients.size());
    }

    @Test
    void testGetIndividualClientById() {
        IndividualClient client = new IndividualClient(3, "19283746510", "Poznan", "Adam", "Wisniewski");
        individualClientManager.register(client);

        IndividualClient retrievedClient = individualClientManager.getClientByItemID(client.getId());
        assertNotNull(retrievedClient);
        assertEquals("Adam", retrievedClient.getFirstName());
        assertEquals("Wisniewski", retrievedClient.getLastName());
    }

    @Test
    void testUpdateIndividualClient() {
        IndividualClient client = new IndividualClient(3, "19283746510", "Poznan", "Adam", "Wisniewski");
        individualClientManager.register(client);

        IndividualClient retrievedClient = individualClientManager.getClientByItemID(client.getId());
        assertNotNull(retrievedClient);
        assertEquals("Adam", retrievedClient.getFirstName());
        assertEquals("Wisniewski", retrievedClient.getLastName());

        client.setLastName("Matan");
        individualClientManager.update(client);
        retrievedClient = individualClientManager.getClientByItemID(client.getId());
        assertNotNull(retrievedClient);
        assertEquals("Matan", retrievedClient.getLastName());
    }

    @Test
    void testGetAllIndividualClients() {
        IndividualClient client1 = new IndividualClient(4, "11111111111",  "Gdansk", "Ewa", "Kowalska");
        IndividualClient client2 = new IndividualClient(5, "22222222222", "Lodz", "Piotr", "Zielinski");

        individualClientManager.register(client1);
        individualClientManager.register(client2);

        List<IndividualClient> clients = individualClientManager.getAll();
        assertEquals(2, clients.size());
    }
}
