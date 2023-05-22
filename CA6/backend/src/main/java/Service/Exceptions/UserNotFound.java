package Service.Exceptions;

public class UserNotFound  extends Exception{
    public String getMessage() {
        return "User Not Found";
    }

}
