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
@DiscriminatorValue("BUSINESS")
@Table(name = "business_clients")
public class BusinessClient extends Client {

    @Column(name = "company_name", nullable = false, length = 100)
    private String companyName;

    @Column(name = "nip_id", nullable = false, length = 15, unique = true)
    private String nipID;

    @Column(nullable = false)
    private double discount;

    public BusinessClient(String companyName, String nipID, String address, double discount) {
        super(nipID, address);
        this.companyName = companyName;
        this.nipID = nipID;
        this.discount = discount;
    }

    @Override
    public double getDiscount() {
        return discount;
    }

    @Override
    public String toString() {
        return "BusinessClient{" +
                "companyName='" + companyName + '\'' +
                ", nipID='" + nipID + '\'' +
                ", discount=" + discount +
                "} " + super.toString();
    }
}
