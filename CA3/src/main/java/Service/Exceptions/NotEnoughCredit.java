package Service.Exceptions;

public class NotEnoughCredit extends Exception{
    public String getMessage() {
        return "Not Enough Credit";
    }
}
