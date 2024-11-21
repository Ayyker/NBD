package repository;

import com.mongodb.client.MongoCollection;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import model.Client;

import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;


public class ClientRepository extends AbstractMongoRepository {

    private final MongoCollection<Client> clientCollection;

    public ClientRepository() {
        initDbConnection();
        MongoDatabase database = getDatabase();
        clientCollection = database.getCollection("clients", Client.class);
    }

    public void saveOrUpdate(Client client) {
        if (client.getId() == null) {
            client.setId(new ObjectId());
            clientCollection.insertOne(client);
        } else {
            clientCollection.replaceOne(eq("_id", client.getId()), client);
        }
    }

    public List<Client> findAll() {
        List<Client> clients = new ArrayList<>();
        clientCollection.find().into(clients);
        return clients;
    }

    public Client findById(ObjectId id) {
        return clientCollection.find(Filters.eq("_id", id)).first();
    }

    public boolean deleteById(ObjectId id) {
        return clientCollection.deleteOne(eq("_id", id)).wasAcknowledged();
    }

    @Override
    public void close() throws Exception {
        closeConnection();
    }
}
