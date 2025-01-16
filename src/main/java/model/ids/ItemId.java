package model.ids;

import com.datastax.oss.driver.api.core.CqlIdentifier;

public class ItemId {
    static final CqlIdentifier TABLE_NAME = CqlIdentifier.fromCql("Items");
    static final CqlIdentifier ID = CqlIdentifier.fromCql("id");
    static final CqlIdentifier ITEM_ID = CqlIdentifier.fromCql("itemId");
    static final CqlIdentifier ITEM_NAME = CqlIdentifier.fromCql("itemName");
    static final CqlIdentifier ITEM_COST = CqlIdentifier.fromCql("itemCost");
    static final CqlIdentifier AVAILABLE = CqlIdentifier.fromCql("available");

}
