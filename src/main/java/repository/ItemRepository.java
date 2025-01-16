package repository;

import com.datastax.oss.driver.api.core.PagingIterable;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder;
import daos.ItemDao;
import daos.ItemDaoImp;
import mappers.ItemMapperImp;
import model.Cass.ItemCass;
import model.Item;
import model.ids.ItemId;


import java.util.ArrayList;
import java.util.List;

public class ItemRepository extends AbstractCassandraRepository implements AutoCloseable {

    ItemDao itemDao;
    ItemMapperImp mapper = new ItemMapperImp();

    public ItemRepository() {
        super();
        this.createTable();
        this.itemDao = new ItemDaoImp(this.session);
    }

    public void registerItem(Item item) {
        ItemCass itemC = this.mapper.toItemCass(item);
        itemDao.create(itemC);
    }

    public void updateItem(Item item) {
        ItemCass itemC = this.mapper.toItemCass(item);
        itemDao.update(itemC);
    }

    public void removeItem(int id) {
        itemDao.delete(id);
    }

    public List<Item> getAllItems() {
        PagingIterable<ItemCass> items = this.itemDao.findAll();
        List<Item> items2 = new ArrayList<>();
        for (ItemCass item : items) {
            items2.add(this.mapper.toItem(item));
        }
        return items2;
    }

    public Item getItemById(int id) {
        ItemCass item = this.itemDao.findById(id);
        return this.mapper.toItem(item);
    }

    private void createTable() {
        SimpleStatement createTableIfNotExist =
                SchemaBuilder.createTable("onlineStore", "Items")
                        .ifNotExists()
                        .withPartitionKey(ItemId.ID, DataTypes.INT)
                        .withColumn(ItemId.ITEM_ID, DataTypes.ASCII)
                        .withColumn(ItemId.ITEM_NAME, DataTypes.ASCII)
                        .withColumn(ItemId.ITEM_COST, DataTypes.DOUBLE)
                        .withColumn(ItemId.AVAILABLE, DataTypes.BOOLEAN)
                        .build();
        session.execute(createTableIfNotExist);
    }

    @Override
    public void close() throws Exception {
        session.close();
    }
}