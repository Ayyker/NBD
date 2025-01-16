package Cassandra;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder;

import java.net.InetSocketAddress;

import static model.ids.PurchaseId.KEYSPACE_NAME;

public class SessionFactory {

    public static CqlSession initCassandraConnection() {
        CqlSession session = CqlSession
                .builder()
                .addContactPoint(new InetSocketAddress("cassandra1", 9042))
                .addContactPoint(new InetSocketAddress("cassandra2", 9043))
                .addContactPoint(new InetSocketAddress("cassandra3", 9044))
                .withLocalDatacenter("dc1")
                .withAuthCredentials("cassandra", "cassandra")
                .build();

        session.execute(
                SchemaBuilder.createKeyspace(KEYSPACE_NAME)
                        .ifNotExists()
                        .withSimpleStrategy(3)
                        .withDurableWrites(true)
                        .build()
        );

        session.execute("USE " + KEYSPACE_NAME);

        return session;
    }

}
