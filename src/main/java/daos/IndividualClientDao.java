package daos;

import com.datastax.oss.driver.api.core.PagingIterable;
import com.datastax.oss.driver.api.mapper.annotations.*;
import model.Cass.IndividualClientCass;

@Dao
public interface IndividualClientDao {
    @Insert
    @StatementAttributes(consistencyLevel = "LOCAL_QUORUM")
    void create(IndividualClientCass client);

    @Delete
    @StatementAttributes(consistencyLevel = "LOCAL_QUORUM")
    void delete(int id);

    @Update
    @StatementAttributes(consistencyLevel = "LOCAL_QUORUM")
    void update(IndividualClientCass client);

    @Select
    @StatementAttributes(consistencyLevel = "LOCAL_QUORUM")
    IndividualClientCass findById(int id);

    @Select
    @StatementAttributes(consistencyLevel = "LOCAL_QUORUM")
    PagingIterable<IndividualClientCass> findAll();
}
