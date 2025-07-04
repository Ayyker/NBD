package manager;

import jakarta.persistence.EntityManager;
import model.BusinessClient;
import model.Client;
import model.IndividualClient;
import repository.ClientRepository;

import java.util.List;

public class ClientManager {

    private final ClientRepository clientsRepository;

    public ClientManager(EntityManager em) {
        this.clientsRepository = new ClientRepository(em);
    }

    // Znalezienie klienta po ID (BusinessClient lub IndividualClient)
    public Client getClient(Long id) {
        return clientsRepository.findById(id);
    }

    // Rejestracja nowego klienta indywidualnego
    public IndividualClient registerIndividualClient(String firstName, String lastName, String pesel, String address) {
        IndividualClient individualClient = new IndividualClient(firstName, lastName, pesel, address);
        clientsRepository.saveOrUpdate(individualClient);
        return individualClient;  // Zwracamy zarejestrowanego klienta indywidualnego
    }

    // Rejestracja nowego klienta biznesowego
    public BusinessClient registerBusinessClient(String companyName, String nip, String address, double discount) {
        BusinessClient businessClient = new BusinessClient(companyName, nip, address, discount);
        clientsRepository.saveOrUpdate(businessClient);
        return businessClient;  // Zwracamy zarejestrowanego klienta biznesowego
    }

    // Znalezienie wszystkich klientów (BusinessClient i IndividualClient)
    public List<Client> getAllClients() {
        return clientsRepository.findAll();
    }

    // Usunięcie klienta
    public void removeClient(Long id) {
        clientsRepository.deleteById(id);
    }

    // Nowa metoda do aktualizacji klienta
    public void updateClient(Long id, String newAddress, String newNameOrFirstName, String newNipOrLastName) {
        Client client = clientsRepository.findById(id);

        if (client == null) {
            throw new IllegalArgumentException("Client with id " + id + " not found.");
        }

        if (client instanceof IndividualClient individualClient) {
            individualClient.setAddress(newAddress);
            individualClient.setFirstName(newNameOrFirstName);
            individualClient.setLastName(newNipOrLastName);
        } else if (client instanceof BusinessClient businessClient) {
            businessClient.setAddress(newAddress);
            businessClient.setCompanyName(newNameOrFirstName);
            businessClient.setNipID(newNipOrLastName);
        }

        // Zapisujemy zaktualizowanego klienta
        clientsRepository.saveOrUpdate(client);
    }
}
