package mappers;

import model.Cass.ItemCass;
import model.Item;

public class ItemMapperImp {
    public ItemCass toItemCass(Item item) {
        return new ItemCass(item.getId(), item.getItemID(), item.getItemName(), item.getItemCost(), item.isAvailable());
    }

    public Item toItem(ItemCass item) {
        return new Item(item.getId(), item.getItemID(), item.getItemName(), item.getItemCost(), item.isAvailable());
    }
}
