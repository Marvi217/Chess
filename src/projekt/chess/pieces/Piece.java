package projekt.chess.pieces;

import java.util.ArrayList;
import java.util.List;

public abstract class Piece {
    private String color;
    private int row;
    private int col;
    public List<int[]> validMoves;
    protected List<int[]> threatenedPositions;
    protected List<int[]> threatenedMoves;
    protected List<int[]> take;

    public Piece(String color) {
        this.color = color;
        this.row = -1;
        this.col = -1;
        this.validMoves = new ArrayList<>();
        this.threatenedPositions = new ArrayList<>();
        this.threatenedMoves = new ArrayList<>();
    }

    public abstract List<int[]> calculateValidMoves(Piece[][] board, int currentRow, int currentCol);
    public abstract List<int[]> calculateThreatenedMoves(Piece[][] board, int selectedRow, int selectedCol);
    public String getColor() {
        return color;
    }

    public String getImagePath() {
        return "";
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public void setPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public List<int[]> getValidMoves() {
        return validMoves;
    }

    public List<int[]> getThreatenedMoves() {
        return threatenedMoves;
    }
}
