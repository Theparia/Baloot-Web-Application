package Service.Exceptions;

public class RatingOutOfRange extends Exception {
    public String getMessage() {
        return "Rating Out of Range";
    }

}
