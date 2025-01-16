package daos;

import com.datastax.oss.driver.api.core.PagingIterable;
import com.datastax.oss.driver.api.mapper.annotations.*;
import model.Cass.ItemCass;

@Dao
public interface ItemDao {
    @Insert
    @StatementAttributes(consistencyLevel = "LOCAL_QUORUM")
    void create(ItemCass item);

    @Delete
    @StatementAttributes(consistencyLevel = "LOCAL_QUORUM")
    void delete(ItemCass item);

    @Update
    @StatementAttributes(consistencyLevel = "LOCAL_QUORUM")
    void update(ItemCass item);

    @Select
    @StatementAttributes(consistencyLevel = "LOCAL_QUORUM")
    ItemCass findById(ItemCass item);

    @Select
    @StatementAttributes(consistencyLevel = "LOCAL_QUORUM")
    PagingIterable<ItemCass> findAll();

}
