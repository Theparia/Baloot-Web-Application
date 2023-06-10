package Exceptions;

public class InvalidCredentials extends Exception {
    public String getMessage() {
        return "Invalid Credentials";
    }
}