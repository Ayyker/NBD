package mappers;

import model.IndividualClient;
import model.Cass.IndividualClientCass;

public class IndividualClientMapperImp {
    public IndividualClientCass toIndividualClientCass(IndividualClient client) {
        return new IndividualClientCass(client.getId(), client.getAddress(), client.getFirstName(), client.getLastName(), client.getPesel());
    }

    public IndividualClient toIndividualClient(IndividualClientCass client) {
        return new IndividualClient(client.getId(), client.getAddress(), client.getFirstName(), client.getLastName(), client.getPesel());
    }
}
