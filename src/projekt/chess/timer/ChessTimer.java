package projekt.chess.timer;

import java.io.Serializable;

public class ChessTimer implements Serializable {
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

    public int getTime() {
        return minutes * 60 + seconds;
    }

    public void setTime(int totalSeconds) {
        this.minutes = totalSeconds / 60;
        this.seconds = totalSeconds % 60;
    }
}
