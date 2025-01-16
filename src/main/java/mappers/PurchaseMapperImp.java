package mappers;

import model.Cass.PurchaseCass;
import model.Purchase;

public class PurchaseMapperImp {
    public PurchaseCass toPurchaseCass(Purchase purchase) {
        return new PurchaseCass(purchase.getId(), purchase.getClientId(), purchase.getItemId(), purchase.getAmount(), purchase.getTotalCost());
    }

    public Purchase toPurchase(PurchaseCass purchase) {
        return new Purchase(purchase.getId(), purchase.getClientId(), purchase.getItemId(), purchase.getAmount(), purchase.getTotalCost());
    }
}
