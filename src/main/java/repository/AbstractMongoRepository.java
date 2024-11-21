package repository;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.CreateCollectionOptions;
import mappers.ClientCodec;
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

    private MongoClient mongoClient;
    private MongoDatabase onlineShopDB;

    protected void initDbConnection() {
        CodecRegistry pojoCodecRegistry = CodecRegistries.fromProviders(
                PojoCodecProvider.builder()
                        .automatic(true)
                        .conventions(List.of(Conventions.ANNOTATION_CONVENTION))
                        .build());

        CodecRegistry codecRegistry = CodecRegistries.fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(),
                CodecRegistries.fromCodecs(new ClientCodec()),  // Dodajemy niestandardowy koder
                pojoCodecRegistry
        );

        MongoClientSettings settings = MongoClientSettings.builder()
                .credential(credential)
                .applyConnectionString(connectionString)
                .uuidRepresentation(UuidRepresentation.STANDARD)
                .codecRegistry(codecRegistry)
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