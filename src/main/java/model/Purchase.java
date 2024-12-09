package model;

import lombok.Builder;
import lombok.NoArgsConstructor;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

@NoArgsConstructor
@Builder
@BsonDiscriminator
public class Purchase {

    @BsonId
    @BsonProperty("_id")
    private ObjectId id;

    @BsonProperty("client")
    private ObjectId clientId;

    @BsonProperty("items")
    private ObjectId itemId;

    @BsonProperty("amount")
    private Integer amount;

    @BsonProperty("total_cost")
    private double totalCost;

    @BsonCreator
    public Purchase(@BsonProperty("_id") ObjectId id,
                    @BsonProperty("client") ObjectId clientId,
                    @BsonProperty("items") ObjectId itemId,
                    @BsonProperty("amount") Integer amount,
                    @BsonProperty("total_cost") double totalCost) {
        this.id = id;
        this.clientId = clientId;
        this.itemId = itemId;
        this.amount = amount;
        this.totalCost = totalCost;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public ObjectId getClientId() {
        return clientId;
    }

    public void setClientId(ObjectId clientId) {
        this.clientId = clientId;
    }

    public ObjectId getItemId() {
        return itemId;
    }

    public void setItemId(ObjectId itemId) {
        this.itemId = itemId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }
}
