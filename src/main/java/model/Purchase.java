package model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "purchases")
public class Purchase{
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToMany
    @JoinTable(
            name = "purchase_items",  // Tabela łącząca
            joinColumns = @JoinColumn(name = "purchase_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id")
    )
    private Set<Item> items;

    @Column(nullable = false)
    private boolean pending;

    public double getFinalCost() {
        return items.stream().mapToDouble(Item::getItemCost).sum() * client.getDiscount();
    }

    @Override
    public String toString() {
        return "Purchase{" +
                "id=" + id +
                ", client=" + client +
                ", items=" + items +
                ", version=" + version +
                '}';
    }
}