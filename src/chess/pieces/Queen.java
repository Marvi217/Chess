package chess.pieces;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Queen extends Piece implements Serializable {

    public Queen(String color) {
        super(color);
    }

    @Override
    public List<int[]> calculateValidMoves(Piece[][] board, int currentRow, int currentCol) {
        validMoves = new ArrayList<>();

        int[][] directions = {
                {1, 0}, {-1, 0}, {0, 1}, {0, -1}, // Rook-like moves
                {1, 1}, {1, -1}, {-1, 1}, {-1, -1} // Bishop-like moves
        };

        for (int[] direction : directions) {
            int rowStep = direction[0];
            int colStep = direction[1];
            int newRow = currentRow + rowStep;
            int newCol = currentCol + colStep;

            while (isValidMove(board, currentRow, currentCol, newRow, newCol)) {
                validMoves.add(new int[]{newRow, newCol});
                newRow += rowStep;
                newCol += colStep;
            }
        }

        return validMoves;
    }

    @Override
    public void calculateThreatenedMoves(Piece[][] board, int currentRow, int currentCol) {
        List<int[]> validMoves = calculateValidMoves(board, currentRow, currentCol);
        threatenedMoves = new ArrayList<>();

        for (int[] move : validMoves) {
            if (isThreatened(board, move[0], move[1], getColor())) {
                threatenedMoves.add(move);
            }
        }

    }

    private boolean isValidMove(Piece[][] board, int currentRow, int currentCol, int newRow, int newCol) {
        if (newRow < 0 || newRow >= 8 || newCol < 0 || newCol >= 8) {
            return false;
        }

        int rowDiff = Math.abs(currentRow - newRow);
        int colDiff = Math.abs(currentCol - newCol);

        if (rowDiff == colDiff || currentRow == newRow || currentCol == newCol) {
            int rowStep = Integer.compare(newRow, currentRow);
            int colStep = Integer.compare(newCol, currentCol);

            int step = Math.max(rowDiff, colDiff);
            return isPathClear(board, currentRow, currentCol, newRow, newCol, rowStep, colStep, step, getColor());
        }

        return false;
    }

    static boolean isPathClear(Piece[][] board, int currentRow, int currentCol, int newRow, int newCol, int rowStep, int colStep, int step, String color) {
        for (int i = 1; i < step; i++) {
            if (board[currentRow + i * rowStep][currentCol + i * colStep] != null) {
                return false;
            }
        }

        return board[newRow][newCol] == null || !board[newRow][newCol].getColor().equals(color);
    }

    @Override
    public String getImagePath() {
        return "resources/" + getColor() + "_queen.png";
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
