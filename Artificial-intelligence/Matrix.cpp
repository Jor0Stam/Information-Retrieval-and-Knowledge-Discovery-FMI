#include <iostream>
#include <unordered_set>
#include <queue>
#include <sstream>
#include <string.h> // memcpy
#include <stdlib.h> // abs
#include "Matrix.h"

using namespace std;
bool Matrix::buildFromInput() {
   string firstRow;

   /*
    * needed to get the input from cind
    * since cin can't directly go to uint8_t
    * without screwing stuff up
    */
   int holder; 

   getline(cin, firstRow);
   stringstream ss;
   ss.str(firstRow);

   int counter = 0;
   while (ss.rdbuf()->in_avail() != 0) {
      ss >> holder;
      if (holder == 0) {
         emptyIndex = counter;
      }
      arr[counter++] = holder;
   }

   cols = counter;

   if (arrSize % counter != 0) {
      // that's a bummer
      printf("Wrong input\n");
      return false;
   }

   rows = arrSize / counter;
   for (int i = counter; i < arrSize; ++i) {
      cin >> holder;
      if (holder == 0) {
         emptyIndex = i;
      }
      arr[i] = static_cast<tile>(holder);
   }

   Matrix::goalArr = new tile[arrSize];

   for (int i = 1; i < arrSize; ++i) {
      Matrix::goalArr[i - 1] = i;
   }
   Matrix::goalArr[arrSize - 1] = 0;

   return true;
}

void Matrix::print() {
   for (int i = 0; i < rows; ++i) {
      for (int j = 0; j < cols; ++j) {
         printf("%d ", arr[i * cols + j]);
      }
      printf("\n");
   }
   printf("\n");
}

int Matrix::Heming() {
   int res = 0;

   for (int i = 0; i < arrSize; ++i) {
      if (arr[i] != Matrix::goalArr[i]) {
         ++res;
      }
   }
   return res;
}

int Matrix::Manhattan() {
   int res = 0;
   for (int i = 0; i < arrSize; ++i) {
      for (int j = 0; j < arrSize; ++j) {
         if (arr[i] == Matrix::goalArr[j]) {
            res += abs(i % cols - j % cols);
            res += abs(i / cols - j / cols);
         }
      }
   }
   return res;
}

tile *Matrix::goalArr = nullptr;
