#include <iostream>
#include <stack>
#include <unordered_set>
#include <queue>
#include <sstream>
#include <string.h> // memcpy
#include <stdlib.h> // abs
#include "Matrix.h"
#include "Node.h"
#include <climits>

#define MAX_ITER 40
using namespace std;


void findSolutionIterative(Node *start) {
   auto cmp = [](Node* left, Node* right) {
      return left->score < right->score;
   };
   int result = 0;
   int threshold = start->score;


   stack<Node*> stack;

   Move moves[] = {Left, Right, Up, Down}; 

   Node *current;
   Matrix *currentMatrix;
   priority_queue<Node*, vector<Node*>, decltype(cmp)> queue(cmp);
   unordered_set<Node*, NodeHash, NodeEqual> visited;

   while (threshold < MAX_ITER) {
      
      stack.push(start);
      uint32_t minNewThreshold = INT_MAX;

      while (!stack.empty()) {
         current = stack.top();
         currentMatrix = current->payload;
         visited.emplace(current);
         stack.pop();

         if (current->score - current->currentSteps == 0) {
            printf("Found solution:\nTotal: %d\n", current->currentSteps);
            current->printPath();
            return;
         }


         for (const auto & muv : moves) {
            if (currentMatrix->canSlide(muv) && 
                  visited.count(current) != 0) {
               Node *potentialNode = new Node(current, muv);

               if (potentialNode->score > threshold) {
                  minNewThreshold = min(potentialNode->score, minNewThreshold);
                  delete potentialNode;
               } else {
                  queue.push(new Node(current, muv));
               }
            }
         }

         while (!queue.empty()) {
            stack.push(queue.top());
            queue.pop();
         }
      }
      /*
       * all of the nodes for which there was memory
       * allocated are currently in the visited set
       * clear 'em up
       */
       
      for (auto &node : visited) {
         if (node != start)
            delete node;
      }
      visited.clear();
      threshold = minNewThreshold;
      printf("Current threshold: %d\n", threshold);
   }
   printf("It is taking too long\n");
}

int main() {
   int n;
   //printf("Enter biggest: ");
   cin >> n;
   // now there is a redundant \n in the stream
   cin.ignore(256, '\n');
   Matrix matr(n);

   matr.buildFromInput();

   Node start(&matr);
   findSolutionIterative(&start);
}
