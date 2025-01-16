package model.ids;

import com.datastax.oss.driver.api.core.CqlIdentifier;

public class PurchaseId {
    public static final CqlIdentifier KEYSPACE_NAME = CqlIdentifier.fromCql("onlineStore");
    public static final CqlIdentifier TABLE_NAME = CqlIdentifier.fromCql("Purchases");
    public static final CqlIdentifier ID = CqlIdentifier.fromCql("id");
    public static final CqlIdentifier CLIENT_ID = CqlIdentifier.fromCql("clientId");
    public static final CqlIdentifier ITEM_ID = CqlIdentifier.fromCql("itemId");
    public static final CqlIdentifier AMOUNT = CqlIdentifier.fromCql("amount");
    public static final CqlIdentifier TOTAL_COST = CqlIdentifier.fromCql("totalCost");
}
