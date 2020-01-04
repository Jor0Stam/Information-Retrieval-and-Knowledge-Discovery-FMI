package board;

public class Board {
    /*
        X = -1
        Draw = 0
        O = 1
     */
    private int[] board = {0, 0, 0, 0, 0, 0, 0, 0, 0};
    private boolean isFinished = false;
    private String winner = "";

    public Board() {}

    public static Board copyBoard(Board board) {
        Board newBoard = new Board();

        newBoard.board = board.board.clone();
        newBoard.isFinished = board.isFinished;
        newBoard.winner = board.winner;

        return newBoard;
    }

    public boolean isGameFinished() {
        return this.isFinished;
    }

    public String getWinner() {
        return this.winner;
    }

    private boolean makeMove(int x, int y, int move) {
        if ((x < 0 || x > 3) || (y < 0 || y > 3) || this.board[3*x + y] != 0) {
            // Debug message
            // System.out.println("Invalid move! Try again!");
            return false;
        }

        this.board[3*x + y] = move;
        this.checkForWinner(x, y, move);
        return true;
    }

    public int[] getBoard() {
        return this.board;
    }

    public boolean playX(int x, int y) {
        checkForFinishedGame();
        return this.isFinished || this.makeMove(x, y, -1);
    }

    public boolean playO(int x, int y) {
        checkForFinishedGame();
        return this.isFinished || this.makeMove(x, y, 1);
    }

    private void checkForFinishedGame() {
        boolean fieldFull = true;
        for (int i = 0; i < 9; i++) {
            if (this.board[i] == 0) {
                fieldFull = false;
                break;
            }
        }

        if (fieldFull) {
            this.isFinished = true;
        }
    }

    private void checkForWinner(int x, int y, int move) {
        checkForFinishedGame();

        this.isFinished = this.isFinished ||
                checkForLine(0, 1, 2) ||
                checkForLine(3, 4, 5) ||
                checkForLine(6, 7, 8) ||

                checkForLine(0, 3, 6) ||
                checkForLine(1, 4, 7) ||
                checkForLine(2, 5, 8) ||

                checkForLine(0, 4, 8) ||
                checkForLine(2, 4, 6);
    }

    private boolean checkForLine(int firstTile, int secondTile, int thirdTile) {
        boolean result = this.board[firstTile] == this.board[secondTile] &&
                         this.board[secondTile] == this.board[thirdTile] &&
                         this.board[firstTile] != 0;

        if (result) {
            this.winner = castNumToPlayer(firstTile);
        }

        return result;
    }

    private String castNumToPlayer(int num) {
        return this.board[num] == 1 ? "O" : (this.board[num] == -1 ? "X" : " ");
    }

    public void printBoard() {
        for(int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.print(" | " + this.castNumToPlayer(i * 3 + j));
            }
            System.out.println("|");
        }
    }
}
