package projekt.chess.engine;

import projekt.chess.pieces.Piece;

import java.io.Serializable;

public class GameData implements Serializable {
    private static final long serialVersionUID = 1L;
    private Piece[][] boardState;
    private String chatContent;

    public GameData(Piece[][] boardState, String chatContent) {
        this.boardState = boardState;

        this.chatContent = chatContent;
    }
    public Piece[][] getBoardState() {
        return boardState;
    }

    public String getChatContent() {
        return chatContent;
    }
}
