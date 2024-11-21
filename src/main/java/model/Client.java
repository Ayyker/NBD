package model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.types.ObjectId;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.codecs.pojo.annotations.BsonIgnore;

@Getter
@Setter
@NoArgsConstructor
public abstract class Client {

    @BsonId
    @BsonProperty("_id")
    private ObjectId id;

    @BsonProperty("personal_id")
    private String personalID;

    @BsonProperty("address")
    private String address;


    public Client(String personalID, String address) {
        this.personalID = personalID;
        this.address = address;
    }

    @BsonCreator
    public Client(@BsonProperty("_id") ObjectId id,
                  @BsonProperty("personal_id") String personalID,
                  @BsonProperty("address") String address) {
        this.id = id;
        this.personalID = personalID;
        this.address = address;
    }

    @BsonIgnore
    public abstract double getDiscount();

    @Override
    public String toString() {
        return "Client{" +
                "personalID='" + personalID + '\'' +
                ", address=" + address +
                '}';
    }
}
