package Service.Exceptions;

public class CommodityNotInBuyList extends Exception{
    public String getMessage() {
        return "Commodity Not in BuyList";
    }
}
