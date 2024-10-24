package model;

import jakarta.persistence.*;
import lombok.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "items", indexes = {
        @Index(name = "idx_item_name", columnList = "item_name")
})

public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private long version;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @PrePersist
    protected void onCreate() {
        this.createdDate = LocalDateTime.now();
    }

    @Column(name = "item_id", nullable = false, unique = true, length = 50)
    @NotNull(message = "Item ID cannot be null")
    private String itemID;

    @Column(name = "item_name", nullable = false, length = 100)
    @NotNull(message = "Item name cannot be null")
    @Size(max = 100, message = "Item name cannot exceed 100 characters")
    private String itemName;

    @Column(name = "item_cost", nullable = false)
    @Min(value = 0, message = "Item cost must be a positive value")
    private double itemCost;

    @Column(nullable = false)
    private boolean available;

    public Item(String name, double cost, String itemID, boolean available) {
        this.itemID = itemID;
        this.itemName = name;
        this.itemCost = cost;
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