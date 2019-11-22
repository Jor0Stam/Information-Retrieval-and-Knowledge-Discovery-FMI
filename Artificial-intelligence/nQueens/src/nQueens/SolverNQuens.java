package nQueens;

public class SolverNQuens {
	public static void main(String[] args) {
		Board board = new Board(4);
		long startTime = System.currentTimeMillis();
		board.solve();
		long elapsedTime = System.currentTimeMillis() - startTime;
		board.printSolution();
		System.out.println("\nSolved for " + elapsedTime + " millisecond(s)");
	}
}
