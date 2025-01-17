package manager;

import model.IndividualClient;
import repository.IndividualClientRepository;

import java.util.List;

public class IndividualClientManager {

    private final IndividualClientRepository clientRepository;

    public IndividualClientManager(IndividualClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public IndividualClient getClientByItemID(int id) {
        return clientRepository.getIndividualClientById(id);
    }

    public void register(IndividualClient client) {
        clientRepository.registerIndividualClient(client);
    }

    public void remove(int id) {
        clientRepository.removeIndividualClient(id);
    }

    public void update(IndividualClient client) {
        clientRepository.updateIndividualClient(client);
    }

    public List<IndividualClient> getAll() {
        return clientRepository.getAllIndividualClients();
    }

}
