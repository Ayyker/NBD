package repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import model.Client;

import java.util.List;

public class ClientRepository {
    @PersistenceContext
    private final EntityManager entityManager;

    public ClientRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public Client saveOrUpdate(Client client) {
        if (client.getId() == null) {
            entityManager.persist(client);
        } else {
            entityManager.merge(client);
        }
        return client;
    }

    public Client findById(Long id) {
        return entityManager.find(Client.class, id);
    }

    public List<Client> findAll() {
        return entityManager.createQuery("SELECT c FROM Client c", Client.class).getResultList();
    }

    @Transactional
    public void deleteById(Long id) {
        Client client = findById(id);
        if (client != null) {
            entityManager.remove(client);
        }
    }
}
