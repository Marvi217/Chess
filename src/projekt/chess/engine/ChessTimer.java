package projekt.chess.engine;

public class ChessTimer {
    private int minutes;
    private int seconds;

    public ChessTimer(int initialMinutes, int initialSeconds) {
        this.minutes = initialMinutes;
        this.seconds = initialSeconds;
    }

    public void decrementTime() {
        if (seconds > 0) {
            seconds--;
        } else {
            seconds = 59;
            minutes--;
        }
    }

    public int getMinutes() {
        return minutes;
    }

    public int getSeconds() {
        return seconds;
    }
}

