package chess.exception;
public class TurnException extends Exception {
    private final String message;

    public TurnException() {
        this.message = """

                 !!!!!! BŁĄD\s
                Tura przeciwnika\s
                 !!!!!! BŁĄD\s
                \s
                """;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
