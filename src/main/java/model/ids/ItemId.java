package model.ids;

import com.datastax.oss.driver.api.core.CqlIdentifier;

public class ItemId {
    public static final CqlIdentifier TABLE_NAME = CqlIdentifier.fromCql("Items");
    public static final CqlIdentifier ID = CqlIdentifier.fromCql("id");
    public static final CqlIdentifier ITEM_ID = CqlIdentifier.fromCql("itemId");
    public static final CqlIdentifier ITEM_NAME = CqlIdentifier.fromCql("itemName");
    public static final CqlIdentifier ITEM_COST = CqlIdentifier.fromCql("itemCost");
    public static final CqlIdentifier AVAILABLE = CqlIdentifier.fromCql("available");

}
