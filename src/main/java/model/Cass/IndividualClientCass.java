package model.Cass;

import com.datastax.oss.driver.api.mapper.annotations.ClusteringColumn;
import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@CqlName("IndividualClients")
@NoArgsConstructor
public class IndividualClientCass extends ClientCass implements Serializable {

    @ClusteringColumn(1)
    private String firstName;

    @ClusteringColumn(2)
    private String lastName;

    private String pesel;

    public IndividualClientCass(int id, String pesel, String address, String firstName, String lastName) {
        super(id, pesel, address);
        this.firstName = firstName;
        this.lastName = lastName;
        this.pesel = pesel;
    }

    @Override
    public double getDiscount() {
        return 1;
    }

    @Override
    public String toString() {
        return "IndividualClient{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", pesel='" + pesel + '\'' +
                "} " + super.toString();
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPesel() {
        return pesel;
    }

    public void setPesel(String pesel) {
        this.pesel = pesel;
    }
}
