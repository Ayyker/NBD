package model.Cass;

import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@CqlName("Purchases")
@NoArgsConstructor
@Builder
public class PurchaseCass implements Serializable {

    @PartitionKey
    private int id;

    private int clientId;

    private int itemId;

    private int amount;

    private double totalCost;

    public PurchaseCass(int id, int clientId, int itemId, int amount, double totalCost) {
        this.id = id;
        this.clientId = clientId;
        this.itemId = itemId;
        this.amount = amount;
        this.totalCost = totalCost;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }
}