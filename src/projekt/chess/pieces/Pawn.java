package projekt.chess.pieces;

import java.util.ArrayList;
import java.util.List;

import static projekt.chess.board.Board.WHITE;

public class Pawn extends Piece {
    private final String color;

    public Pawn(String color) {
        super(color);
        this.color = color;
    }

    @Override
    public List<int[]> calculateValidMoves(Piece[][] board, int currentRow, int currentCol) {
        validMoves = new ArrayList<>();

        int direction = color.equals(WHITE) ? -1 : 1;
        int startRow = color.equals(WHITE) ? 6 : 1;
        
        int newRow = currentRow + direction;
        int newCol = currentCol;
        if (newRow >= 0 && newRow < 8 && board[newRow][newCol] == null) {
            validMoves.add(new int[]{newRow, newCol});
        }

        if (currentRow == startRow && board[currentRow + direction][currentCol] == null
                && board[currentRow + 2 * direction][currentCol] == null) {
            validMoves.add(new int[]{currentRow + 2 * direction, currentCol});
        }
        
        newRow = currentRow + direction;
        newCol = currentCol - 1;
        if (newRow >= 0 && newRow < 8 && newCol >= 0 && board[newRow][newCol] != null
                && !board[newRow][newCol].getColor().equals(this.getColor())) {
            validMoves.add(new int[]{newRow, newCol});
        }
        
        newRow = currentRow + direction;
        newCol = currentCol + 1;
        if (newRow >= 0 && newRow < 8 && newCol < 8 && board[newRow][newCol] != null
                && !board[newRow][newCol].getColor().equals(this.getColor())) {
            validMoves.add(new int[]{newRow, newCol});
        }

        return validMoves;
    }

    public List<int[]> calculateTake(int currentRow, int currentCol) {
        take = new ArrayList<>();
        int direction = color.equals(WHITE) ? -1 : 1;

        int newRow;
        int newCol;
        newRow = currentRow + direction;
        newCol = currentCol - 1;
        if (newRow >= 0 && newRow < 8 && newCol >= 0) {
            take.add(new int[]{newRow, newCol});
        }

        newRow = currentRow + direction;
        newCol = currentCol + 1;
        if (newRow >= 0 && newRow < 8 && newCol < 8) {
            take.add(new int[]{newRow, newCol});
        }
        return take;
    }

    @Override
    public void calculateThreatenedMoves(Piece[][] board, int currentRow, int currentCol) {
        List<int[]> validMoves = calculateValidMoves(board, currentRow, currentCol);
        threatenedMoves = new ArrayList<>();

        for (int[] move : validMoves) {
            if (isThreatened(board, move[0], move[1], this.color)) {
                threatenedMoves.add(move);
            }
        }

    }

    @Override
    public String getColor() {
        return color;
    }

    @Override
    public String getImagePath() {
        return "resources/" + color + "_pawn.png";
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
