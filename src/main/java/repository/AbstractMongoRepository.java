package repository;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.*;
import com.mongodb.client.model.CreateCollectionOptions;
import model.ClientValidation;
import org.bson.UuidRepresentation;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.codecs.pojo.Conventions;

import java.util.List;


public abstract class AbstractMongoRepository implements AutoCloseable{

    private ConnectionString connectionString = new ConnectionString("mongodb://mongodb1:27017,mongodb2:27018,mongodb3:27019/" +
            "?replicaSet=replica_set_single");

    private MongoCredential credential = MongoCredential.createCredential("admin", "admin", "adminpassword".toCharArray());

    private CodecRegistry pojoCodecRegistry = CodecRegistries.fromProviders(
            PojoCodecProvider.builder()
                    .automatic(true)
                    .conventions(List.of(Conventions.ANNOTATION_CONVENTION))
                    .build());

    private MongoClient mongoClient;
    private MongoDatabase onlineShopDB;

    protected void initDbConnection() {
        MongoClientSettings settings = MongoClientSettings.builder()
                .credential(credential)
                .applyConnectionString(connectionString)
                .uuidRepresentation(UuidRepresentation.STANDARD)
                .codecRegistry(CodecRegistries.fromRegistries(
                        MongoClientSettings.getDefaultCodecRegistry(),
                        pojoCodecRegistry
                ))
                .build();

        mongoClient = MongoClients.create(settings);
        onlineShopDB = mongoClient.getDatabase("onlineShop");

        if (!collectionExist("clients")) {
            onlineShopDB.createCollection("clients", new CreateCollectionOptions().validationOptions(ClientValidation.options));
        }
    }
    public boolean collectionExist(String collectionName) {
        for (String existingCollectionName : onlineShopDB.listCollectionNames()) {
            if (existingCollectionName.equals(collectionName))
                return true;
        }
        return false;
    }

    public MongoDatabase getDatabase() {
        return onlineShopDB;
    }

    public MongoClient getClient() {
        return mongoClient;
    }

    protected void closeConnection() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }
}
