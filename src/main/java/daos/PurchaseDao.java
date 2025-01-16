package daos;

import com.datastax.oss.driver.api.core.PagingIterable;
import com.datastax.oss.driver.api.mapper.annotations.*;
import model.Cass.PurchaseCass;

@Dao
public interface PurchaseDao {
    @Insert
    @StatementAttributes(consistencyLevel = "LOCAL_QUORUM")
    void create(PurchaseCass purchase);

    @Delete
    @StatementAttributes(consistencyLevel = "LOCAL_QUORUM")
    void delete(int id);

    @Update
    @StatementAttributes(consistencyLevel = "LOCAL_QUORUM")
    void update(PurchaseCass purchase);

    @Select
    @StatementAttributes(consistencyLevel = "LOCAL_QUORUM")
    PurchaseCass findById(int id);

    @Select
    @StatementAttributes(consistencyLevel = "LOCAL_QUORUM")
    PagingIterable<PurchaseCass> findAll();
}
