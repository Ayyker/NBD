package tests;

import Cassandra.SessionFactory;
import com.datastax.oss.driver.api.core.CqlSession;
import manager.BusinessClientManager;
import model.BusinessClient;
import org.junit.jupiter.api.*;
import repository.BusinessClientRepository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BusinessClientManagerTest {

    private static BusinessClientManager businessClientManager;
    CqlSession session = SessionFactory.initCassandraConnection();

    @BeforeAll
    static void setup() {
        BusinessClientRepository businessClientRepository = new BusinessClientRepository();
        businessClientManager = new BusinessClientManager(businessClientRepository);
    }

    @BeforeEach
    void init() {
        // Ensure the database is cleaned before each test
        session.execute("TRUNCATE TABLE onlineStore.BusinessClients;");
    }

    @Test
    void testRegisterBusinessClient() {
        BusinessClient client = new BusinessClient(1, "Krakow", "TechCorp", "1234567890", 10.0);
        businessClientManager.register(client);

        List<BusinessClient> clients = businessClientManager.getAll();
        assertEquals(1, clients.size());
        assertEquals("TechCorp", clients.get(0).getCompanyName());
        assertEquals("Krakow", clients.get(0).getAddress());
    }

    @Test
    void testRemoveBusinessClient() {
        BusinessClient client = new BusinessClient(2, "Warszawa", "SoftServe", "9876543210", 15.0);
        businessClientManager.register(client);

        businessClientManager.remove(client.getId());

        List<BusinessClient> clients = businessClientManager.getAll();
        assertEquals(0, clients.size());
    }

    @Test
    void testUpdateBusinessClient() {
        BusinessClient client = new BusinessClient(2, "Warszawa", "SoftServe", "9876543210", 15.0);
        businessClientManager.register(client);

        BusinessClient retrieved = businessClientManager.getClientByItemID(2);
        assertEquals("SoftServe", retrieved.getCompanyName());
        client.setCompanyName("HardServe");
        businessClientManager.update(client);

        BusinessClient retrieved2 = businessClientManager.getClientByItemID(2);
        assertEquals("HardServe", retrieved2.getCompanyName());
    }

    @Test
    void testGetBusinessClientById() {
        BusinessClient client = new BusinessClient(3, "Poznan", "NetCompany", "1928374650", 20.0);
        businessClientManager.register(client);

        BusinessClient retrievedClient = businessClientManager.getClientByItemID(client.getId());
        assertNotNull(retrievedClient);
        assertEquals("NetCompany", retrievedClient.getCompanyName());
        assertEquals("Poznan", retrievedClient.getAddress());
    }

    @Test
    void testGetAllBusinessClients() {
        BusinessClient client1 = new BusinessClient(4, "Gdansk", "AlphaTech", "1111111111", 5.0);
        BusinessClient client2 = new BusinessClient(5, "Lodz", "BetaSoft", "2222222222", 7.5);

        businessClientManager.register(client1);
        businessClientManager.register(client2);

        List<BusinessClient> clients = businessClientManager.getAll();
        assertEquals(2, clients.size());
    }
}
