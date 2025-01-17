package manager;

import model.BusinessClient;
import repository.BusinessClientRepository;

import java.util.List;

public class BusinessClientManager {

    private final BusinessClientRepository clientRepository;

    public BusinessClientManager(BusinessClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public BusinessClient getClientByItemID(int id) {
        return clientRepository.getBusinessClientById(id);
    }

    public void register(BusinessClient client) {
        clientRepository.registerBusinessClient(client);
    }

    public void remove(int id) {
        clientRepository.removeBusinessClient(id);
    }

    public void update(BusinessClient client) {
        clientRepository.updateBusinessClient(client);
    }

    public List<BusinessClient> getAll() {
        return clientRepository.getAllBusinessClients();
    }

}
