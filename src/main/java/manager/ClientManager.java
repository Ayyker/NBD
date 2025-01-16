//package manager;
//
//import com.mongodb.client.MongoDatabase;
//import model.BusinessClient;
//import model.Client;
//import model.IndividualClient;
//import org.bson.types.ObjectId;
//import repository.ClientRepository;
//
//import java.util.List;
//
//public class ClientManager {
//
//    private final ClientRepository clientsRepository;
//
//    public ClientManager(ClientRepository clientsRepository) {
//        this.clientsRepository = clientsRepository;
//    }
//
//    public Client getClient(ObjectId id) {
//        return clientsRepository.findById(id);
//    }
//
//    public IndividualClient registerIndividualClient(ObjectId id,String firstName, String lastName, String pesel, String address) {
//        IndividualClient individualClient = new IndividualClient(id, firstName, lastName, pesel, address);
//        clientsRepository.save(individualClient);
//        return individualClient;
//    }
//
//    public BusinessClient registerBusinessClient(ObjectId id, String companyName, String nip, String address, double discount) {
//        BusinessClient businessClient = new BusinessClient(id, companyName, nip, address, discount);
//        clientsRepository.save(businessClient);
//        return businessClient;
//    }
//
//    public List<Client> getAllClients() {
//        return clientsRepository.findAll();
//    }
//
//    public void removeClient(ObjectId id) {
//        clientsRepository.deleteById(id);
//    }
//
//    public void updateClient(ObjectId id, String newAddress, String newNameOrFirstName, String newNipOrLastName) {
//        Client client = clientsRepository.findById(id);
//
//        if (client == null) {
//            throw new IllegalArgumentException("Client with id " + id + " not found.");
//        }
//
//        if (client instanceof IndividualClient individualClient) {
//            individualClient.setAddress(newAddress);
//            individualClient.setFirstName(newNameOrFirstName);
//            individualClient.setLastName(newNipOrLastName);
//        } else if (client instanceof BusinessClient businessClient) {
//            businessClient.setAddress(newAddress);
//            businessClient.setCompanyName(newNameOrFirstName);
//            businessClient.setNipID(newNipOrLastName);
//        }
//
//        clientsRepository.update(client);
//    }
//
//    public MongoDatabase getDatabase() {
//        return clientsRepository.getDatabase();
//    }
//}
