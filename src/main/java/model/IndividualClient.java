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
@BsonDiscriminator(key = "type", value = "INDIVIDUAL")
public class IndividualClient extends Client {

    @BsonProperty("first_name")
    private String firstName;

    @BsonProperty("last_name")
    private String lastName;

    @BsonProperty("pesel")
    private String pesel;

    @BsonCreator
    public IndividualClient(@BsonProperty("_id") ObjectId id,
                            @BsonProperty("first_name") String firstName,
                            @BsonProperty("last_name") String lastName,
                            @BsonProperty("personal_id") String personalID,
                            @BsonProperty("address") String address) {
        super(id, personalID, address);
        this.firstName = firstName;
        this.lastName = lastName;
        this.pesel = personalID;
    }


    @Override
    public double getDiscount() {
        // Implementacja specyficzna dla klienta indywidualnego
        return 0.1; // Na przykład stały rabat 10%
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