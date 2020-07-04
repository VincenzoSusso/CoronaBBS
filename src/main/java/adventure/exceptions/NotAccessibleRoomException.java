package adventure.exceptions;

public class NotAccessibleRoomException extends Exception {

    @Override
    public String getMessage() {
        return "The room is not accessible";
    }
}