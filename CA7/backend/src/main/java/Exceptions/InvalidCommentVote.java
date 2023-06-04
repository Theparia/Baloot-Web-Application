package Exceptions;

public class InvalidCommentVote extends Exception{
    public String getMessage() {
        return "Invalid Comment Vote";
    }
}
