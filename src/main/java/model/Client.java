package model;

import lombok.NoArgsConstructor;

import java.io.Serializable;


@NoArgsConstructor
public abstract class Client implements Serializable {

    private int id;

    private String personalID;

    private String address;


    public Client(int id, String personalID, String address) {
        this.id = id;
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
