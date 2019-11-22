package nSlider;

import java.util.Scanner;

public class Nslider {
	private FieldState inputField;
	private FieldState currentField;

	private void setUp() {
//		Scanner sc = new Scanner(System.in);
//		System.out.println("Enter number of tiles");
//		int size = sc.nextInt() + 1;
//		int[] field = new int[size];
//		
//		System.out.println("Enter position index for empty tile");
//		int zeroPosition = sc.nextInt();
//		if (zeroPosition == -1) {
//			zeroPosition = size - 1;
//		}
//		
//		for (int i = 0; i < size; i++) {
//			if (i == zeroPosition) {
//				field[i] = 0;
//				i++;
//			}
//			System.out.println("Enter the " + i + "th element of the puzzle");
//			field[i] = sc.nextInt();
//		}
		int size = 9;
		int zeroPosition = 4;
		int[] field = {1, 2, 3, 4, 0, 5, 6, 7, 8};
		
		
		this.inputField.getField().populateField(size, field);
//		sc.close();
	}

	public void solve() {
//		for (this.currentField.getField()) {
//			
//		}
	}
	
	public static void main(String[] args) {
		Nslider slider = new Nslider();
		slider.setUp();
		
//		slider.getField().printField();
//		System.out.println(slider.getField().calcManhattan());
	}
}