package model;

import lombok.*;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Builder
@BsonDiscriminator
public class Purchase {

    @BsonId
    @BsonProperty("_id")
    private ObjectId id;

    @BsonProperty("client_id")
    private ObjectId clientId;

    @BsonProperty("item_ids")
    private List<ObjectId> itemIds;

    @BsonProperty("total_cost")
    private double totalCost;

    @BsonCreator
    public Purchase(@BsonProperty("_id") ObjectId id,
                    @BsonProperty("client_id") ObjectId clientId,
                    @BsonProperty("item_ids") List<ObjectId> itemIds,
                    @BsonProperty("total_cost") double totalCost) {
        this.id = id;
        this.clientId = clientId;
        this.itemIds = itemIds;
        this.totalCost = totalCost;
    }

    @Override
    public String toString() {
        return "Purchase{" +
                "id=" + id +
                ", clientId=" + clientId +
                ", itemIds=" + itemIds +
                ", totalCost=" + totalCost +
                '}';
    }
}