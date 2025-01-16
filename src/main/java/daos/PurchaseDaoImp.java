package daos;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.PagingIterable;
import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import model.Cass.PurchaseCass;
import repository.AbstractCassandraRepository;

public class PurchaseDaoImp extends AbstractCassandraRepository implements PurchaseDao {

    private final CqlSession session;

    public PurchaseDaoImp(CqlSession session) {
        this.session = session;
    }


    @Override
    public void create(PurchaseCass purchase) {
        String query = "INSERT INTO Purchases (id, clientId, itemId, amount, totalCost) VALUES (?, ?, ?, ?, ?);";
        PreparedStatement preparedStatement = session.prepare(query);
        BoundStatement boundStatement = preparedStatement.bind(
                purchase.getId(),
                purchase.getClientId(),
                purchase.getItemId(),
                purchase.getAmount(),
                purchase.getTotalCost()
        );
        session.execute(boundStatement);
    }

    @Override
    public void delete(int id) {
        String query = "DELETE FROM Purchases WHERE id = ?";
        PreparedStatement preparedStatement = session.prepare(query);
        BoundStatement boundStatement = preparedStatement.bind(
                id
        );
        session.execute(boundStatement);
    }

    @Override
    public void update(PurchaseCass purchase) {
        String query = "UPDATE Purchases SET itemId = ?, itemName = ?, itemCost = ?, available = ?) WHERE id = ?";
        PreparedStatement preparedStatement = session.prepare(query);
        BoundStatement boundStatement = preparedStatement.bind(
                purchase.getClientId(),
                purchase.getItemId(),
                purchase.getAmount(),
                purchase.getTotalCost(),
                purchase.getId()
        );
        session.execute(boundStatement);
    }

    @Override
    public PurchaseCass findById(int id) {
        String query = "SELECT * FROM Purchases WHERE id = ?";
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
    public PagingIterable<PurchaseCass> findAll() {
        SimpleStatement statement = SimpleStatement.builder("SELECT * FROM Purchases").build();
        return session.execute(statement).map(this::mapR);
    }


    private PurchaseCass mapR(Row row) {
        return new PurchaseCass(
                row.getInt("id"),
                row.getInt("clientId"),
                row.getInt("itemId"),
                row.getInt("itemCost"),
                row.getDouble("totalCost")
        );
    }
}
