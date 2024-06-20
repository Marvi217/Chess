package projekt.chess.exception;
public class InvalidMoveException extends Exception {
    private String message;

    public InvalidMoveException(String piece) {
        this.message ="\n !!!!!! BŁĄD \n" + piece + " nie może wykonać tego ruchu \n !!!!!! BŁĄD \n \n";
    }

    @Override
    public String getMessage() {
        return message;
    }
}
