package model.ids;

import com.datastax.oss.driver.api.core.CqlIdentifier;

public class PurchaseId {
    static final CqlIdentifier KEYSPACE_NAME = CqlIdentifier.fromCql("public");
}
