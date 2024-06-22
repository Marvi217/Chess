package projekt.chess.board;

import projekt.chess.pieces.Piece;

import java.io.File;
import java.io.IOException;

public interface Game {
    Piece[][] getBoard();
    void setBoard(Piece[][] board);
}
