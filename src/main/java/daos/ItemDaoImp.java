package daos;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.PagingIterable;
import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import model.Cass.ItemCass;
import repository.AbstractCassandraRepository;

public class ItemDaoImp extends AbstractCassandraRepository implements ItemDao {

    private final CqlSession session;

    public ItemDaoImp(CqlSession session) {
        this.session = session;
    }


    @Override
    public void create(ItemCass item) {
        String query = "INSERT INTO Items (id, itemId, itemName, itemCost, available) VALUES (?, ?, ?, ?, ?);";
        PreparedStatement preparedStatement = session.prepare(query);
        BoundStatement boundStatement = preparedStatement.bind(
                item.getId(),
                item.getItemID(),
                item.getItemName(),
                item.getItemCost(),
                item.isAvailable()
        );
        session.execute(boundStatement);
    }

    @Override
    public void delete(int id) {
        String query = "DELETE FROM Items WHERE id = ?";
        PreparedStatement preparedStatement = session.prepare(query);
        BoundStatement boundStatement = preparedStatement.bind(
                id
        );
        session.execute(boundStatement);
    }

    @Override
    public void update(ItemCass item) {
        String query = "UPDATE Items SET itemId = ?, itemName = ?, itemCost = ?, available = ? WHERE id = ?";
        PreparedStatement preparedStatement = session.prepare(query);
        BoundStatement boundStatement = preparedStatement.bind(
                item.getItemID(),
                item.getItemName(),
                item.getItemCost(),
                item.isAvailable(),
                item.getId()
        );
        session.execute(boundStatement);
    }

    @Override
    public ItemCass findById(int id) {
        String query = "SELECT * FROM Items WHERE id = ?";
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
    public PagingIterable<ItemCass> findAll() {
        SimpleStatement statement = SimpleStatement.builder("SELECT * FROM Items").build();
        return session.execute(statement).map(this::mapR);
    }


    private ItemCass mapR(Row row) {
        return new ItemCass(
                row.getInt("id"),
                row.getString("itemId"),
                row.getString("itemName"),
                row.getDouble("itemCost"),
                row.getBoolean("available")
        );
    }
}
