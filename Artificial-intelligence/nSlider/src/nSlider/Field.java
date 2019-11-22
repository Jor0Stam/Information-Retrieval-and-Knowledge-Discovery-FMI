package nSlider;

public class Field {
    private int[] tiles;
    private int size, rowSize;
	private Field prevState;
	private int costSoFar = 0;

    public Field() {
    }

    public int[] getTiles() {
    	return this.tiles;
    }

    public int getSize() {
    	return this.size;
    }

    public int getRowSize() {
    	return this.rowSize;
    }
    
    public Field getPrevState() {
    	return this.prevState;
    }
    
    public int getCost() {
    	return this.costSoFar;
    }

    public void populateField(int size, int[] field) {
       this.size = size;
       this.tiles = field;
       this.rowSize = (int) Math.sqrt(size);
    }

    public Field moveTiles(int index, Move direction) {
       switch (direction) {
          case LEFT:
             if (index == 0 || index % this.rowSize == 0) {
                return null;
             }
             tiles[index - 1] = tiles[index] + tiles[index - 1];
             tiles[index] = tiles[index - 1] - tiles[index];
             tiles[index - 1] = tiles[index - 1] - tiles[index];
             break;

          case UP:
             if (index % this.rowSize == index) {
                return null;
             }
             tiles[index - this.rowSize + 1] = tiles[index] + tiles[index - this.rowSize + 1];
             tiles[index] = tiles[index - this.rowSize + 1] - tiles[index];
             tiles[index - this.rowSize + 1] = tiles[index - this.rowSize + 1] - tiles[index];
             break;

          case RIGHT:
             if (index == size - 1 || index % this.rowSize == this.rowSize - 1) {
                return null;
             }
             tiles[index + 1] = tiles[index] + tiles[index + 1];
             tiles[index] = tiles[index + 1] - tiles[index];
             tiles[index + 1] = tiles[index + 1] - tiles[index];
             break;

          case DOWN:
             if (index >= this.rowSize * (this.rowSize - 1)) {
                return null;
             }
             tiles[index + 1] = tiles[index] + tiles[index + 1];
             tiles[index] = tiles[index + 1] - tiles[index];
             tiles[index + 1] = tiles[index + 1] - tiles[index];
             break;

          default:
             return null;
       }

       Field result = new Field();
       result.populateField(this.size, this.tiles);
       return result;
    }

    public int calcManhattan() {
       int result = 0;
       int tileValue;

       for (int i = 0; i < this.size; i++) {
    	   if (this.tiles[i] != 0) {
    		   tileValue = this.tiles[i];
    	   } else {
    		   tileValue = this.size;
    	   }
    	   
    	   result += (Math.abs((i + 1) - tileValue) / this.rowSize) + 
    			   (Math.abs((i + 1) - tileValue) % this.rowSize);

//    	   result += Math.abs( (tileValue % this.rowSize) - (i % rowSize) ) + 
//    			   Math.abs( (tileValue / this.rowSize) - (i / rowSize) );
    	   System.out.println("i: " + i + " result: " + result);
       }

       return result;
    }
    
    public void printField() {
    	System.out.print("[");

    	for (int i = 0; i < this.size; i++ ) {
    		System.out.print(this.tiles[i] + " ");
    	}

    	System.out.println("]");
    }
}

enum Move {
 LEFT,
 UP,
 RIGHT,
 DOWN
}
