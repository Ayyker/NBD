package model;

import lombok.Builder;
import lombok.NoArgsConstructor;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;


@NoArgsConstructor
@Builder
public class Item {

    @BsonId
    @BsonProperty("_id")
    private ObjectId id;

    @BsonProperty("item_id")
    private String itemID;

    @BsonProperty("item_name")
    private String itemName;

    @BsonProperty("item_cost")
    private double itemCost;

    @BsonProperty("available")
    private boolean available;

    @BsonCreator
    public Item(@BsonProperty("_id") ObjectId id,
                @BsonProperty("item_id") String itemID,
                @BsonProperty("item_name") String itemName,
                @BsonProperty("item_cost") double itemCost,
                @BsonProperty("available") boolean available) {
        this.id = id;
        this.itemID = itemID;
        this.itemName = itemName;
        this.itemCost = itemCost;
        this.available = available;
    }

    @Override
    public String toString() {
        return "Item{" +
                "itemID='" + itemID + '\'' +
                ", itemName='" + itemName + '\'' +
                ", itemCost=" + itemCost +
                ", available=" + available +
                '}';
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getItemID() {
        return itemID;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public double getItemCost() {
        return itemCost;
    }

    public void setItemCost(double itemCost) {
        this.itemCost = itemCost;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}