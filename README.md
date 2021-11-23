# Missionaries-cannibals problem using A\* algorithm
This is a solution to a generalization of the missionaries - cannibals problem using the A* algorithm. The exercise description is as follows:

*The problem of cannibals and missionaries using
algorithm A\* with a closed set. 
Consider that in the initial state we have N missionaries on one bank
and the same number (N) of cannibals on the same bank but N is a parameter
defined when calling the program. The maximum capacity (in
persons) of the boat is M, where M is also defined when calling
program. Also, when calling the program, the
maximum permissible number of river crossings is defined, let K. The program should find the optimal solution that does not exceed
the K crossings, if there is such a solution.*

## How I approach the solution

### A\* structure
For the algorithm implementation I use a tree that eliminates
cycles. I keep a priority queue for the frontier nodes and a 
hash map for the reached nodes. A node is considered reached if 
it has its children expanded, while it is placed in the frontier 
when it has been evaluated but not expanded. A node has 
pointers to its children and its parent, a pointer to the tree it
belongs to, its depth in the tree, and a comparator. The node implementation
also keeps information about the missionaries and cannibals on each bank and on
the boat.

### Node expansion selection
The algorithm starts from the root of the tree which has 
zero cost. It creates its children based on domain 
restrictions, meaning that there can't be more cannibals than
missionaries in any bank or the boat. Once the nodes are created, their 
costs are calculated using an appropriate evaluation
function, and they are added in the frontier data structure, 
which is a priority queue as previously mentioned. The node
with the lowest cost is chosen to be expanded, which is in 
the front of the frontier. When it is expanded, it is added 
in the reached data structure. Once this is done, its
children are then created and the process repeats. While each
node is evaluated, it is checked if it is already in reached,
and the node is not expanded. This eliminates cyclic states. 
If a solution is found, it returns it, else null. 

### Evaluation function
A*'s evaluation is: f(n) = g(n) + W * h(n), where 
*W* = 1 (weight), *g(n)* is the function that returns the cost
from the root of the tree till node n, and *h(n)* is the
heuristic function estimating the cost till the solution.
The heuristic is created by relaxing the problem's constraints,
meaning dropping the condition that cannibals must always be
less than the missionaries at each bank and the boat. By this,
we create a solution graph with more edges, and since this 
is a super-graph of the original problem, it is an admissible heuristic. 
The function created is as follows: 

**2 * (FLOOR((N - 1) / (C - 1))) - 1 + 2 * ((N - 1 - 
(FLOOR(N / (C - 1))) * (C - 1)) != 0 ? 1 : 0) + D % 2**

Where:
- N = Number of people at initial bank
- C = Boat capacity
- D = Depth
- D % 2 = Boat Side (if it is not in the initial bank, it needs an extra cross)

### A* Implementation
To make the implementation efficient, we try to eliminate the combinations
examined that could be a valid child of a node. We start by creating in advance
all the possible missionaries-cannibals combinations on boat, and we store them
in a dynamic array. We than create nodes by examining which of these combinations
are valid for each step. We do that by temporarily (for calculation purposes) 
offloading anyone who onboard to the bank that the boat is currently at, and we
generate possible combinations of people that could board on the boat and stay on
the bank. For this we use the domain's restriction (cannibals <= missionaries), 
the precalculated pairs for the boat, and the total number of missionaries and 
cannibals available on the shore. All valid nodes are created and the boat is 
sent to the other bank, without offloading the people on the shore (we calculate
these combinations on the next node). The nodes created are then checked if they
have already been created previously by searching for them in the reached data
structure. If that's the case, the node is simply omitted, and it is not considered
a child node. Each node left is evaluated and added to the children of the parent.
Finally, all the children of the parent are collectively added to the frontier.

### Code structure
The code is structured in two packages, one for the appropriate abstractions and
one for the implementation. For the abstractions, an abstract node and an abstract
tree are created trying as much as possible not to include any problem specific
information other than that they are going to be used for an A* problem 
implementation. There is also an appropriate interface for the A* necessary 
functions. The implementation package contains a node, a tree, and an A*
implementation for the missionaries-cannibals problem, the main class, and
an appropriate helper enumeration.