package repository;

import com.datastax.oss.driver.api.core.PagingIterable;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder;
import daos.PurchaseDao;
import daos.PurchaseDaoImp;
import mappers.PurchaseMapperImp;
import model.Cass.PurchaseCass;
import model.Purchase;
import model.ids.PurchaseId;


import java.util.ArrayList;
import java.util.List;

public class PurchaseRepository extends AbstractCassandraRepository implements AutoCloseable {

    PurchaseDao purchaseDao;
    PurchaseMapperImp mapper = new PurchaseMapperImp();

    public PurchaseRepository() {
        super();
        this.createTable();
        this.purchaseDao = new PurchaseDaoImp(this.session);
    }

    public void registerPurchase(Purchase purchase) {
        PurchaseCass purchaseCass = this.mapper.toPurchaseCass(purchase);
        purchaseDao.create(purchaseCass);
    }

    public void updatePurchase(Purchase purchase) {
        PurchaseCass purchaseCass = this.mapper.toPurchaseCass(purchase);
        purchaseDao.update(purchaseCass);
    }

    public void removePurchase(int id) {
        purchaseDao.delete(id);
    }

    public List<Purchase> getAllIPurchases() {
        PagingIterable<PurchaseCass> purchaseCasses = this.purchaseDao.findAll();
        List<Purchase> purchases = new ArrayList<>();
        for (PurchaseCass purchaseCass : purchaseCasses) {
            purchases.add(this.mapper.toPurchase(purchaseCass));
        }
        return purchases;
    }

    public Purchase getPurchaseById(int id) {
        PurchaseCass purchase = this.purchaseDao.findById(id);
        return this.mapper.toPurchase(purchase);
    }

    private void createTable() {
        SimpleStatement createTableIfNotExist =
                SchemaBuilder.createTable("onlineStore", "Purchases")
                        .ifNotExists()
                        .withPartitionKey(PurchaseId.ID, DataTypes.INT)
                        .withColumn(PurchaseId.CLIENT_ID, DataTypes.INT)
                        .withColumn(PurchaseId.ITEM_ID, DataTypes.INT)
                        .withColumn(PurchaseId.AMOUNT, DataTypes.INT)
                        .withColumn(PurchaseId.TOTAL_COST, DataTypes.DOUBLE)
                        .build();
        session.execute(createTableIfNotExist);
    }

    @Override
    public void close() throws Exception {
        session.close();
    }
}