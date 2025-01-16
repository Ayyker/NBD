package repository;

import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder;
import daos.BusinessClientDao;
import daos.BusinessClientDaoImp;
import mappers.BusinessClientMapper;
import model.ids.BusinessClientId;

public class BusinessClientRepository extends AbstractCassandraRepository {

    BusinessClientDao clientDao;
    BusinessClientMapper mapper = new BusinessClientMapper() {
        @Override
        public BusinessClientDao businessClientDao() {
            return null;
        }
    };

    public BusinessClientRepository() {
        super();
        this.createTable();
        this.clientDao = new BusinessClientDaoImp(this.session);
    }


    private void createTable() {
        SimpleStatement createTableIfNotExist =
            SchemaBuilder.createTable("onlineStore", "BusinessClients")
                .ifNotExists()
                .withPartitionKey(BusinessClientId.ID, DataTypes.INT)
                .withColumn(BusinessClientId.ADDRESS, DataTypes.ASCII)
                .withColumn(BusinessClientId.COMPANY_NAME, DataTypes.ASCII)
                .withColumn(BusinessClientId.NIP_ID, DataTypes.ASCII)
                .withColumn(BusinessClientId.DISCOUNT, DataTypes.DOUBLE)
                .build();
        session.execute(createTableIfNotExist);
    }
}
