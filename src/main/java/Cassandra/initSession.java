package cassandra;

import com.datastax.oss.driver.api.core.CqlSession;

import java.net.InetSocketAddress;

private static CqlSession session;

public void initSession() {
    session = CqlSession.builder()
            .addContactPoint(new InetSocketAddress("cassandra1", 9042))
            .addContactPoint(new InetSocketAddress("cassandra2", 9043))
            .withLocalDatacenter("dc1")
            .withAuthCredentials("cassandra", "cassandrapassword")
            // .withKeyspace(CqlIdentifier.fromCql("rent_a_car"))
            .build();
}
