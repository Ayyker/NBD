package model.Cass;

import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@NoArgsConstructor
public abstract class ClientCass implements Serializable {

    @PartitionKey
    private int id;

    private String personalID;

    private String address;


    public ClientCass(String personalID, String address) {
        this.personalID = personalID;
        this.address = address;
    }


    public abstract double getDiscount();

    @Override
    public String toString() {
        return "ClientCass{" +
                "personalID='" + personalID + '\'' +
                ", address=" + address +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPersonalID() {
        return personalID;
    }

    public void setPersonalID(String personalID) {
        this.personalID = personalID;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}