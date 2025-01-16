package repository;

import com.datastax.oss.driver.api.core.PagingIterable;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder;
import daos.BusinessClientDao;
import daos.BusinessClientDaoImp;
import mappers.BusinessClientMapperImp;
import model.BusinessClient;
import model.Cass.BusinessClientCass;
import model.ids.BusinessClientId;

import java.util.ArrayList;
import java.util.List;

public class BusinessClientRepository extends AbstractCassandraRepository implements AutoCloseable {

    BusinessClientDao clientDao;
    BusinessClientMapperImp mapper = new BusinessClientMapperImp();

    public BusinessClientRepository() {
        super();
        this.createTable();
        this.clientDao = new BusinessClientDaoImp(this.session);
    }

    public void registerBusinessClient(BusinessClient businessClient) {
        BusinessClientCass client = this.mapper.toBusinessClientCass(businessClient);
        clientDao.create(client);
    }

    public void updateBusinessClient(BusinessClient businessClient) {
        BusinessClientCass client = this.mapper.toBusinessClientCass(businessClient);
        clientDao.update(client);
    }

    public void removeBusinessClient(int id) {
        clientDao.delete(id);
    }

    public List<BusinessClient> getAllBusinessClients() {
        PagingIterable<BusinessClientCass> clients = this.clientDao.findAll();
        List<BusinessClient> businessClients = new ArrayList<>();
        for (BusinessClientCass client : clients) {
            businessClients.add(this.mapper.toBusinessClient(client));
        }
        return businessClients;
    }

    public BusinessClient getBusinessClientById(int id) {
        BusinessClientCass client = this.clientDao.findById(id);
        return this.mapper.toBusinessClient(client);
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

    @Override
    public void close() throws Exception {
        session.close();
    }
}
