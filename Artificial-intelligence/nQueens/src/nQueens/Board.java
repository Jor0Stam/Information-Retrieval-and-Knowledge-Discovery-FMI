package nQueens;

import java.util.Arrays;
import java.util.Random;

public class Board {
	public static final char QUEEN = '*';
	public static final char EMPTY_SPACE = '_';

	private int size;
	private int[] queens;
	private int[] colConflicts;
	private int[] mainDiagonalConflicts;
	private int[] secondDiagonalConflicts;
	private int timesTried = 0;
	private boolean solved = false;
	Random rand = new Random();
	
	public Board(int size) {
		this.size = size;
		this.colConflicts = new int[size];
		this.mainDiagonalConflicts = new int[(2 * size) - 1];
		this.secondDiagonalConflicts = new int[(2 * size) - 1];
		this.queens = setRandomQueens();
	}
	
	private int[] setRandomQueens() {
		int[] result = new int[this.size];
		int newQueenCol;

		Arrays.fill(this.colConflicts, 0);
		Arrays.fill(this.mainDiagonalConflicts, 0);
		Arrays.fill(this.secondDiagonalConflicts, 0);

		for (int i = 0; i < this.size; i++) {
			newQueenCol = rand.nextInt(this.size);
			result[i] = newQueenCol;
			this.colConflicts[newQueenCol]++;
			this.mainDiagonalConflicts[newQueenCol - i + this.size - 1]++;
			this.secondDiagonalConflicts[i + newQueenCol]++;
		}

		return result;
	}

	public void solve() {
		int rowOfMaxConflictedQueen;
		do {
			this.timesTried++;
			rowOfMaxConflictedQueen = getRowOfMaxConflictedQueen();
			if (this.solved) {
				break;
			} else if (this.timesTried > 2 * this.size) {
				this.queens = setRandomQueens();
				this.timesTried = 0;
			} else {
				this.moveMaxConflictedQueen(rowOfMaxConflictedQueen);
			}
		}
		while (!this.solved);
	}

	private void moveMaxConflictedQueen(int rowOfMaxConflictedQueen) {
		int newColOfQueen = this.getColWithMinConflictsForRow(rowOfMaxConflictedQueen);

		// Clean old position of queen
		this.colConflicts[this.queens[rowOfMaxConflictedQueen]]--;
		this.mainDiagonalConflicts[this.queens[rowOfMaxConflictedQueen] - rowOfMaxConflictedQueen + this.size - 1]--;
		this.secondDiagonalConflicts[rowOfMaxConflictedQueen + this.queens[rowOfMaxConflictedQueen]]--;

		this.queens[rowOfMaxConflictedQueen] = newColOfQueen;
		// Update new conflicts
		this.colConflicts[this.queens[rowOfMaxConflictedQueen]]++;
		this.mainDiagonalConflicts[newColOfQueen - rowOfMaxConflictedQueen + this.size - 1]++;
		this.secondDiagonalConflicts[rowOfMaxConflictedQueen + newColOfQueen]++;
	}

	private int getRowOfMaxConflictedQueen() {
		int rowOfMaxConflictedQueen = 0;
		int conflictsForI;

		boolean isReallySolved = true;
		for (int i = 0; i < this.size; i++) {
			conflictsForI = getConflicts(i, this.queens[i]); // getConflicts(queen)
			if (getConflicts(rowOfMaxConflictedQueen, this.queens[rowOfMaxConflictedQueen]) < conflictsForI) {
				rowOfMaxConflictedQueen = i;
			}

			if (conflictsForI > 0) {
				isReallySolved = false;
			}
		}
		if (isReallySolved) {
			this.solved = true;
		}
		return rowOfMaxConflictedQueen;
	}

	private int getColWithMinConflictsForRow(int row) {
		int colWithMinConflictsForRow = this.size - 1;
		int numOfConflicts = 0;

		for (int i = 0; i < this.size; i++) {
			if (getConflicts(row, i) < getConflicts(row, colWithMinConflictsForRow)) {
				colWithMinConflictsForRow = i;
			}
		}

		return colWithMinConflictsForRow;
	}
	
	private int getConflicts(int row, int col) {
		int countConflictForQueen = 0;

		countConflictForQueen += Math.max(this.colConflicts[col] - 1, 0);
		countConflictForQueen += Math.max(this.mainDiagonalConflicts[col - row + this.size - 1] - 1, 0);
		countConflictForQueen += Math.max(this.secondDiagonalConflicts[row + col] - 1, 0);

		return countConflictForQueen;
	}

	public void printSolution() {
		char[] field = new char[(int) Math.pow(size, 2)];
		this.populateField(field);

		for (int i = 0; i < this.size; i++) {
			for (int j = 0; j < this.size; j++) {
				System.out.print(field[this.size*i + j] + " ");
			}
			System.out.println("");
		}
	}

	private void populateField(char[] field) {
		for (int i = 0; i < (int) Math.pow(size, 2); i++) {
			if (this.queens[i / size] == i % this.size) {
				field[i] = QUEEN;
			} else {
				field[i] = EMPTY_SPACE;
			}
		}
	}
}
