package mappers;

import com.mongodb.MongoClientSettings;
import model.BusinessClient;
import model.Client;
import model.IndividualClient;
import org.bson.*;
import org.bson.codecs.*;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

public class ClientCodec implements Codec<Client> {

    private final Codec<Document> documentCodec;

    public ClientCodec() {
        this.documentCodec = new DocumentCodec();
    }

    @Override
    public void encode(BsonWriter writer, Client client, EncoderContext encoderContext) {
        Document doc = toDocument(client);
        documentCodec.encode(writer, doc, encoderContext);
    }

    @Override
    public Class<Client> getEncoderClass() {
        return Client.class;
    }

    @Override
    public Client decode(BsonReader reader, DecoderContext decoderContext) {
        Document doc = documentCodec.decode(reader, decoderContext);
        return fromDocument(doc);
    }

    private Document toDocument(Client client) {
        Document doc = new Document();
        doc.put("_id", client.getId());
        doc.put("personal_id", client.getPersonalID());
        doc.put("address", client.getAddress());

        if (client instanceof IndividualClient) {
            doc.put("type", "INDIVIDUAL");
            doc.put("first_name", ((IndividualClient) client).getFirstName());
            doc.put("last_name", ((IndividualClient) client).getLastName());
            doc.put("pesel", ((IndividualClient) client).getPesel());
        } else if (client instanceof BusinessClient) {
            doc.put("type", "BUSINESS");
            doc.put("company_name", ((BusinessClient) client).getCompanyName());
            doc.put("nip_id", ((BusinessClient) client).getNipID());
            doc.put("discount", ((BusinessClient) client).getDiscount());
        }

        return doc;
    }

    private Client fromDocument(Document doc) {
        String type = doc.getString("type");
        Client client;

        switch (type) {
            case "INDIVIDUAL":
                client = new IndividualClient(
                        doc.getObjectId("_id"),
                        doc.getString("first_name"),
                        doc.getString("last_name"),
                        doc.getString("personal_id"),
                        doc.getString("address")
                );
                break;
            case "BUSINESS":
                client = new BusinessClient(
                        doc.getObjectId("_id"),
                        doc.getString("company_name"),
                        doc.getString("nip_id"),
                        doc.getString("address"),
                        doc.getDouble("discount")
                );
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }

        return client;
    }

    public static CodecRegistry getCodecRegistry() {
        return CodecRegistries.fromRegistries(
                CodecRegistries.fromCodecs(new ClientCodec()),
                CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build()),
                MongoClientSettings.getDefaultCodecRegistry()
        );
    }
}
