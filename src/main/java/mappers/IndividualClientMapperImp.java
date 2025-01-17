package mappers;

import model.IndividualClient;
import model.Cass.IndividualClientCass;

public class IndividualClientMapperImp {
    public IndividualClientCass toIndividualClientCass(IndividualClient client) {
        return new IndividualClientCass(client.getId(), client.getPesel(), client.getAddress(), client.getFirstName(), client.getLastName());
    }

    public IndividualClient toIndividualClient(IndividualClientCass client) {
        return new IndividualClient(client.getId(), client.getPesel(), client.getAddress(), client.getFirstName(), client.getLastName());
    }
}
