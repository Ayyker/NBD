package model;

import jakarta.persistence.*;
import lombok.*;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "client_type")
@Table(name = "clients")
public abstract class Client {

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

    @Column(name = "personal_id", nullable = false, unique = true, length = 20)
    @NotNull(message = "Personal ID cannot be null")
    @Pattern(regexp = "\\d{11}|\\w{10,20}", message = "Personal ID must be either 11 digits (PESEL) or valid NIP/ID format")
    private String personalID;

    @Column(name = "address", nullable = false)
    private String address;

    public Client() {}

    public Client(String personalID, String address) {
        this.personalID = personalID;
        this.address = address;
    }

    public abstract double getDiscount();

    @Override
    public String toString() {
        return "Client{" +
                "personalID='" + personalID + '\'' +
                ", address=" + address +
                '}';
    }
}
