package Service.Exceptions;

public class InvalidPriceInterval extends Exception{
    public String getMessage() {
        return "Invalid Price Interval";
    }
}
