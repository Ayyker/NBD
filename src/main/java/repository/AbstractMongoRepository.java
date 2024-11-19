package repository;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.*;

import java.util.List;


public abstract class AbstractMongoRepository implements AutoCloseable{

    private ConnectionString connectionString = new ConnectionString("mongodb://mongodb1:27017,mongodb2:27018,mongodb3:27019/" +
            "?replicaSet=replica_set_single");

    private MongoCredential credential = MongoCredential.createCredential("admin", "admin", "adminpassword".toCharArray());

    private CodecRegisty pojoCodecRegistry = CodecRegisters.fromProviders(
            PojoCodecProvider.builder()
                    .automatic(true)
                    .conventions(List.of(Conventions.ANNOTATION_CONVENTION))
                    .build());

    private MongoClient mongoClient;
    private MongoDatabase onlineShopDB;

}
