package mappers;

import model.BusinessClient;
import model.Cass.BusinessClientCass;

public class BusinessClientMapperImp {
    public BusinessClientCass toBusinessClientCass(BusinessClient businessClient) {
        return new BusinessClientCass(businessClient.getId(), businessClient.getAddress(), businessClient.getCompanyName(), businessClient.getNipID(), businessClient.getDiscount());
    }

    public BusinessClient toBusinessClient(BusinessClientCass businessClientCass) {
        return new BusinessClient(businessClientCass.getId(), businessClientCass.getAddress(), businessClientCass.getCompanyName(), businessClientCass.getNipID(), businessClientCass.getDiscount());
    }
}
