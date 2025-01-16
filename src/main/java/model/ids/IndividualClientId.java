package model.ids;

import com.datastax.oss.driver.api.core.CqlIdentifier;

public class IndividualClientId {
    public static final CqlIdentifier TABLE_NAME = CqlIdentifier.fromCql("IndividualClients");
    public static final CqlIdentifier ID = CqlIdentifier.fromCql("id");
    public static final CqlIdentifier FIRST_NAME = CqlIdentifier.fromCql("firstName");
    public static final CqlIdentifier LAST_NAME = CqlIdentifier.fromCql("lastName");
    public static final CqlIdentifier PESEL = CqlIdentifier.fromCql("pesel");
    public static final CqlIdentifier ADDRESS = CqlIdentifier.fromCql("address");
}
