package daos;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.PagingIterable;
import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import model.Cass.BusinessClientCass;
import repository.AbstractCassandraRepository;

public class BusinessClientDaoImp extends AbstractCassandraRepository implements BusinessClientDao {

    private final CqlSession session;

    public BusinessClientDaoImp(CqlSession session) {
        this.session = session;
    }

    @Override
    public void create(BusinessClientCass client) {
        String query = "INSERT INTO BusinessClients (id, companyName, nipID, discount, address) VALUES (?, ?, ?, ?, ?);";
        PreparedStatement preparedStatement = session.prepare(query);
        BoundStatement boundStatement = preparedStatement.bind(
                client.getId(),
                client.getCompanyName(),
                client.getNipID(),
                client.getDiscount(),
                client.getAddress()
        );
        session.execute(boundStatement);
    }

    @Override
    public void delete(int id) {
        String query = "DELETE FROM BusinessClients WHERE id = ?";
        PreparedStatement preparedStatement = session.prepare(query);
        BoundStatement boundStatement = preparedStatement.bind(
                id
        );
        session.execute(boundStatement);
    }

    @Override
    public void update(BusinessClientCass client) {
        String query = "UPDATE BusinessClients SET companyName = ?, nipID = ?, discount = ?, address = ? WHERE id = ?";
        PreparedStatement preparedStatement = session.prepare(query);
        BoundStatement boundStatement = preparedStatement.bind(
                client.getCompanyName(),
                client.getNipID(),
                client.getDiscount(),
                client.getAddress(),
                client.getId()
        );
        session.execute(boundStatement);
    }

    @Override
    public BusinessClientCass findById(int id) {
        String query = "SELECT * FROM BusinessClients WHERE id = ?";
        PreparedStatement preparedStatement = session.prepare(query);
        BoundStatement boundStatement = preparedStatement.bind(
                id
        );
        Row row = session.execute(boundStatement).one();

        if(row != null) {
            return mapR(row);
        }else {
            return null;
        }
    }

    @Override
    public PagingIterable<BusinessClientCass> findAll() {
        SimpleStatement statement = SimpleStatement.builder("SELECT * FROM BusinessClients").build();
        return session.execute(statement).map(this::mapR);
    }

    private BusinessClientCass mapR(Row row) {
        return new BusinessClientCass(
                row.getInt("id"),
                row.getString("address"),
                row.getString("companyName"),
                row.getString("nipID"),
                row.getDouble("discount")
        );
    }
}
