package model;

import lombok.*;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;


@Getter
@Setter
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
    public Item(@BsonProperty("id") ObjectId id,
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
}