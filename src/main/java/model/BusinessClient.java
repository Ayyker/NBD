package model;

import lombok.*;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@BsonDiscriminator(key = "type", value = "BUSINESS")
public class BusinessClient extends Client {

    @BsonProperty("company_name")
    private String companyName;

    @BsonProperty("nip_id")
    private String nipID;

    @BsonProperty("discount")
    private double discount;

    @BsonCreator
    public BusinessClient(@BsonProperty("_id") ObjectId id,
                          @BsonProperty("company_name") String companyName,
                          @BsonProperty("nip_id") String nipID,
                          @BsonProperty("address") String address,
                          @BsonProperty("discount") double discount) {
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
}