package projekt.chess.exception;
public class TurnException extends Exception {
    private String message;

    public TurnException() {
        this.message ="\n !!!!!! BŁĄD \n" +"Tura przeciwnika "+ "\n !!!!!! BŁĄD \n \n";
    }

    @Override
    public String getMessage() {
        return message;
    }
}




