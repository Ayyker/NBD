package model.ids;

import com.datastax.oss.driver.api.core.CqlIdentifier;

public class BusinessClientId {
    public static final CqlIdentifier TABLE_NAME = CqlIdentifier.fromCql("BusinessClients");
    public static final CqlIdentifier ID = CqlIdentifier.fromCql("id");
    public static final CqlIdentifier COMPANY_NAME = CqlIdentifier.fromCql("companyName");
    public static final CqlIdentifier NIP_ID = CqlIdentifier.fromCql("nipID");
    public static final CqlIdentifier discount = CqlIdentifier.fromCql("discount");
    public static final CqlIdentifier ADDRESS = CqlIdentifier.fromCql("address");
}
