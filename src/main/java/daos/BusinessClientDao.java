package daos;

import com.datastax.oss.driver.api.core.PagingIterable;
import com.datastax.oss.driver.api.mapper.annotations.*;
import model.Cass.BusinessClientCass;

@Dao
public interface BusinessClientDao {
    @Insert
    @StatementAttributes(consistencyLevel = "LOCAL_QUORUM")
    void create(BusinessClientCass client);

    @Delete
    @StatementAttributes(consistencyLevel = "LOCAL_QUORUM")
    void delete(BusinessClientCass client);

    @Update
    @StatementAttributes(consistencyLevel = "LOCAL_QUORUM")
    void update(BusinessClientCass client);

    @Select
    @StatementAttributes(consistencyLevel = "LOCAL_QUORUM")
    BusinessClientCass findById(BusinessClientCass client);

    @Select
    @StatementAttributes(consistencyLevel = "LOCAL_QUORUM")
    PagingIterable<BusinessClientCass> findAll();
}
