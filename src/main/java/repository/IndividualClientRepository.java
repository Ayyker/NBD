package repository;

import com.datastax.oss.driver.api.core.PagingIterable;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder;
import daos.IndividualClientDao;
import daos.IndividualClientDaoImp;
import mappers.IndividualClientMapperImp;
import model.Cass.IndividualClientCass;
import model.IndividualClient;
import model.ids.IndividualClientId;


import java.util.ArrayList;
import java.util.List;

public class IndividualClientRepository extends AbstractCassandraRepository implements AutoCloseable {

    IndividualClientDao clientDao;
    IndividualClientMapperImp mapper = new IndividualClientMapperImp();

    public IndividualClientRepository() {
        super();
        this.createTable();
        this.clientDao = new IndividualClientDaoImp(this.session);
    }

    public void registerIndividualClient(IndividualClient individualClient) {
        IndividualClientCass client = this.mapper.toIndividualClientCass(individualClient);
        clientDao.create(client);
    }

    public void updateIndividualClient(IndividualClient individualClient) {
        IndividualClientCass client = this.mapper.toIndividualClientCass(individualClient);
        clientDao.update(client);
    }

    public void removeIndividualClient(int id) {
        clientDao.delete(id);
    }

    public List<IndividualClient> getAllIndividualClients() {
        PagingIterable<IndividualClientCass> clients = this.clientDao.findAll();
        List<IndividualClient> individualClients = new ArrayList<>();
        for (IndividualClientCass client : clients) {
            individualClients.add(this.mapper.toIndividualClient(client));
        }
        return individualClients;
    }

    public IndividualClient getIndividualClientById(int id) {
        IndividualClientCass client = this.clientDao.findById(id);
        return this.mapper.toIndividualClient(client);
    }

    private void createTable() {
        SimpleStatement createTableIfNotExist =
                SchemaBuilder.createTable("onlineStore", "IndividualClients")
                        .ifNotExists()
                        .withPartitionKey(IndividualClientId.ID, DataTypes.INT)
                        .withColumn(IndividualClientId.PESEL, DataTypes.ASCII)
                        .withColumn(IndividualClientId.ADDRESS, DataTypes.ASCII)
                        .withColumn(IndividualClientId.FIRST_NAME, DataTypes.ASCII)
                        .withColumn(IndividualClientId.LAST_NAME, DataTypes.DOUBLE)
                        .build();
        session.execute(createTableIfNotExist);
    }

    @Override
    public void close() throws Exception {
        session.close();
    }
}

