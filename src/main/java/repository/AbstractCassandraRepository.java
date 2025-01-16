package repository;

import Cassandra.SessionFactory;
import com.datastax.oss.driver.api.core.CqlSession;

public abstract class AbstractCassandraRepository {
    protected CqlSession session;

    public AbstractCassandraRepository() {
        session = SessionFactory.initCassandraConnection();
    }
}
