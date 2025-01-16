package model;

import lombok.Builder;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@NoArgsConstructor
@Builder
public class Item implements Serializable {

    private int id;

    private String itemID;

    private String itemName;

    private double itemCost;

    private boolean available;

    @Override
    public String toString() {
        return "Item{" +
                "itemID='" + itemID + '\'' +
                ", itemName='" + itemName + '\'' +
                ", itemCost=" + itemCost +
                ", available=" + available +
                '}';
    }

    public Item(int id, String itemID, String itemName, double itemCost, boolean available) {
        this.id = id;
        this.itemID = itemID;
        this.itemName = itemName;
        this.itemCost = itemCost;
        this.available = available;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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