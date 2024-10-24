package model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue("INDIVIDUAL")
@Table(name = "individual_clients")
public class IndividualClient extends Client {

    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @Column(nullable = false, unique = true, length = 11)
    private String pesel;

    public IndividualClient(String firstName, String lastName, String pesel, String address) {
        super(pesel, address);  // Wywołanie konstruktora klasy bazowej
        this.firstName = firstName;
        this.lastName = lastName;
        this.pesel = pesel;
    }

    @Override
    public double getDiscount() {
        return 0.0;  // Klienci indywidualni nie mają zniżki
    }

    @Override
    public String toString() {
        return "IndividualClient{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", pesel='" + pesel + '\'' +
                "} " + super.toString();
    }
}
