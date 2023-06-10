package Exceptions;

public class ExpiredDiscount extends Exception {
    public String getMessage() {
        return "Discount has expired";
    }
}
