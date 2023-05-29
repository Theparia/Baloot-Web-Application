package Exceptions;

public class CommodityAlreadyExistsInBuyList extends Exception {
    public String getMessage() {
        return "Commodity Already Exists in BuyList";
    }
}
