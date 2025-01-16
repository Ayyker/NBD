package mappers;

import model.Cass.ItemCass;
import model.Cass.PurchaseCass;
import model.Item;
import model.Purchase;

public class PurchaseMapperImp {
    public PurchaseCass toItemCass(Purchase purchase) {
        return new PurchaseCass(purchase.getId(), purchase.getClientId(), purchase.getItemId(), purchase.getAmount(), purchase.getTotalCost());
    }

    public Purchase toItem(PurchaseCass purchase) {
        return new Purchase(purchase.getId(), purchase.getClientId(), purchase.getItemId(), purchase.getAmount(), purchase.getTotalCost());
    }
}
