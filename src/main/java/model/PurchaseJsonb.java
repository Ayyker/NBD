package model;

import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Builder
public class PurchaseJsonb {

    @JsonbProperty("_id")
    private String id;

    @JsonbProperty("client")
    private String clientId;

    @JsonbProperty("items")
    private String itemId;

    @JsonbProperty("amount")
    private Integer amount;

    @JsonbProperty("total_cost")
    private double totalCost;

    @JsonbCreator
    public PurchaseJsonb(@JsonbProperty("_id") String id,
                         @JsonbProperty("client") String clientId,
                         @JsonbProperty("items") String itemId,
                         @JsonbProperty("amount") Integer amount,
                         @JsonbProperty("total_cost") double totalCost) {
        this.id = id;
        this.clientId = clientId;
        this.itemId = itemId;
        this.amount = amount;
        this.totalCost = totalCost;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
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