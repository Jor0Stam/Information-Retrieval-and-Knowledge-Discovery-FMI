#ifndef _MATRIX_
#define _MATRIX_

#include <string.h>
#include <assert.h>

typedef uint8_t tile;


enum Move {
   Left,
   Right,
   Up,
   Down,
   Start
};

class Matrix {
   // we assume that we get correct input for the matrix
public:

   static tile *goalArr;

   Matrix(int n) : arrSize(n + 1),
                   arr(new tile[arrSize]) {
   }

   Matrix(const Matrix& rhs) : 
      arrSize(rhs.arrSize),
      arr(new tile[arrSize]),
      rows(rhs.rows),
      cols(rhs.cols),
      emptyIndex(rhs.emptyIndex) {
      
      memcpy(arr, rhs.arr, arrSize * sizeof(tile));
   }

   bool operator==(const Matrix& rhs) const{
      // this can't happen in this task :/
      if (arrSize != rhs.arrSize)
         return false;

      for (int i = 0; i < arrSize; ++i) {
         if (arr[i] != rhs.arr[i]) {
            return false;
         }
      }
      return true;
   }

   ~Matrix() {
      delete [] arr;
   }


   bool buildFromInput();

   void print();

   bool canSlide(Move move) {
      switch(move) {
         case Left: return canSlideLeft();
         case Right: return canSlideRight();
         case Up: return canSlideUp();
         case Down: return canSlideDown();
      }

   }
   bool canSlideLeft() {
      return emptyIndex % cols != cols - 1;
   }

   bool canSlideRight() {
      return emptyIndex % cols != 0;
   }

   bool canSlideUp() {
      return emptyIndex / cols != rows - 1;
   }

   bool canSlideDown() {
      return emptyIndex / cols != 0;
   }

   void slide(Move move) {
      switch(move) {
         case Left: slideLeft(); break;
         case Right: slideRight(); break;
         case Up: slideUp(); break;
         case Down: slideDown(); break;
      }
   }

   void slideLeft() {
      assert(canSlideLeft());
      std::swap(arr[emptyIndex], arr[emptyIndex + 1]);
      ++emptyIndex;
   }

   void slideRight() {
      assert(canSlideRight());
      std::swap(arr[emptyIndex], arr[emptyIndex - 1]);
      --emptyIndex;
   }

   void slideUp() {
      assert(canSlideUp());
      std::swap(arr[emptyIndex], arr[emptyIndex + cols]);
      emptyIndex += cols;
   }

   void slideDown() {
      assert(canSlideDown());
      std::swap(arr[emptyIndex], arr[emptyIndex - cols]);
      emptyIndex -= cols;
   }

   int Heming();
   int Manhattan();

   uint64_t getHash() const{
      uint32_t res = 0;

      for (int i = 0; i < arrSize; ++i) {
         res += arr[i];
         res = ~res;
      }

      return res;
   }

private:
   int arrSize;
   tile *arr;
   int rows;
   int cols;
   int emptyIndex;
};

#endif
