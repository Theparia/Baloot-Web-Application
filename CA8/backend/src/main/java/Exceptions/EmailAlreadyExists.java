package Exceptions;

public class EmailAlreadyExists extends Exception{
    public String getMessage() {
        return "Email Already Exists";
    }

}