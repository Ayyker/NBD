package daos;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.PagingIterable;
import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import model.Cass.IndividualClientCass;
import repository.AbstractCassandraRepository;

public class IndividualClientDaoImp extends AbstractCassandraRepository implements IndividualClientDao {

    private final CqlSession session;

    public IndividualClientDaoImp(CqlSession session) {
        this.session = session;
    }


    @Override
    public void create(IndividualClientCass client) {
        String query = "INSERT INTO IndividualClients (id, pesel, address, firstName, lastName) VALUES (?, ?, ?, ?, ?);";
        PreparedStatement preparedStatement = session.prepare(query);
        BoundStatement boundStatement = preparedStatement.bind(
                client.getId(),
                client.getPesel(),
                client.getAddress(),
                client.getFirstName(),
                client.getLastName()
        );
        session.execute(boundStatement);
    }

    @Override
    public void delete(IndividualClientCass client) {
        String query = "DELETE FROM IndividualClients WHERE id = ?";
        PreparedStatement preparedStatement = session.prepare(query);
        BoundStatement boundStatement = preparedStatement.bind(
                client.getId()
        );
        session.execute(boundStatement);
    }

    @Override
    public void update(IndividualClientCass client) {
        String query = "UPDATE IndividualClients SET pesel = ?, address = ?, firstName = ?, lastName = ?) WHERE id = ?";
        PreparedStatement preparedStatement = session.prepare(query);
        BoundStatement boundStatement = preparedStatement.bind(
                client.getPesel(),
                client.getAddress(),
                client.getFirstName(),
                client.getLastName(),
                client.getId()
        );
        session.execute(boundStatement);
    }

    @Override
    public IndividualClientCass findById(IndividualClientCass client) {
        String query = "SELECT * FROM IndividualClients WHERE id = ?";
        PreparedStatement preparedStatement = session.prepare(query);
        BoundStatement boundStatement = preparedStatement.bind(
                client.getId()
        );
        Row row = session.execute(boundStatement).one();

        if(row != null) {
            return mapR(row);
        }else {
            return null;
        }
    }

    @Override
    public PagingIterable<IndividualClientCass> findAll() {
        SimpleStatement statement = SimpleStatement.builder("SELECT * FROM IndividualClients").build();
        return session.execute(statement).map(this::mapR);
    }

    //int id, String pesel, String address, String firstName, String lastName
    private IndividualClientCass mapR(Row row) {
        return new IndividualClientCass(
                row.getInt("id"),
                row.getString("pesel"),
                row.getString("address"),
                row.getString("firstName"),
                row.getString("lastName")
        );
    }
}
