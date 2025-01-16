package model;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@NoArgsConstructor
@AllArgsConstructor
public class BusinessClient extends Client implements Serializable {

    private String companyName;

    private String nipID;

    private double discount;

    public BusinessClient(int id, String address, String companyName, String nipID, double discount) {
        super(id, nipID, address);
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


    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getNipID() {
        return nipID;
    }

    public void setNipID(String nipID) {
        this.nipID = nipID;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }
}