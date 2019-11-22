#ifndef _NODE_
#define _NODE_

#include <string>
#include "Matrix.h"

std::string moveToStr(Move move) {
   switch(move) {
      case Left : return "Left";
      case Right : return "Right";
      case Up : return "Up";
      case Down : return "Down";
      default : return "Start";
   }
}

struct Node {
   // This constructor is only for the root node
   Node(const Matrix* matrix) :
      parent(nullptr),
      payload(new Matrix(*matrix)),
      currentSteps(0),
      moveToCurr(Start),
      score(payload->Manhattan()) {}

   Node(const Node *parent, Move moveToMake) :
      parent(parent),
      payload(new Matrix(*parent->payload)),
      currentSteps(parent->currentSteps + 1),
      moveToCurr(moveToMake) {
   
      // caller must make sure that this is slidable
      assert(payload->canSlide(moveToMake));
      payload->slide(moveToMake);

      score = currentSteps + payload->Manhattan();
   }

   bool operator==(const Node& rhs) const {
      return *payload == *rhs.payload;
   }

   ~Node() {
      delete payload;
   }

   void printPath() const {
      if (parent) {
         parent->printPath();
      }
      printf("%s\n", moveToStr(moveToCurr).c_str());
      payload->print();
   }

   Matrix *payload;

   const Node *parent;
   const Move moveToCurr;
   const uint32_t currentSteps;
   uint32_t score;
};

struct NodeHash {
   uint32_t operator()(Node const* node) const noexcept {
      return node->payload->getHash();
   }
};

struct NodeEqual {
   bool operator()(Node const* lhs, Node const* rhs) const {
      return *lhs == *rhs;
   }
};

#endif
