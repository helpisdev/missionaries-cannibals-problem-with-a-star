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
hash map for the reached. A node is considered reached if 
it has its children expanded, while it is considered frontier 
when it has been evaluated but not expanded. A node has 
pointers to its children and its parent, and a boolean 
marking it as blocked or not. A node is marked as blocked 
if it is already found in reached with a lower cost, or if 
it at depth K. When marked blocked the node is deleted. 

### Node expansion selection
The algorithm starts from the root of the tree which has 
zero cost. It creates its children based on domain 
restrictions, meaning that there can't be more cannibals than
missionaries in any bank. I assume that this also holds true
for the boat as well. Once the nodes are created, their 
costs are calculated using an appropriate evaluation
function, and they are added in the frontier data structure, 
which is a priority queue as previously mentioned. The node
with the lowest cost is chosen to be expanded, which is in 
the front of the frontier. When it is expanded, it is added 
in the reached data structure. Once this is done, its
children are then created the process repeats. While each
node is evaluated, it is checked if it is already in reached,
and the node with the highest cost is deleted. This 
eliminates cyclic states. If a solution is found, it returns
it, else null. 

### Evaluation function
A*'s evaluation is: f(n) = g(n) + W * h(n), where 
W = 1 (weight) and g is the function that returns the cost
from the root of the tree till node n, while h is the
heuristic function estimating the cost till the solution.
The heuristic is used by relaxing the problem's constraints,
meaning dropping the condition that cannibals must always be
less than the missionaries at each bank or the boat. By this,
we create a solution graph with more edges, and since this 
is a calculates a solution for a super-graph of the original
problem, it is an admissible heuristic. The function created
is as follows: 

**2 * (FLOOR((N / 1) / (C - 1))) - 1 + 2 * ((N - 1 - 
(FLOOR(N / (C - 1))) * (C - 1)) != 0 ? 1 : 0) + D % 2**

Where:
- N = Number of people at initial bank
- C = Boat capacity
- D = Depth
- D % 2 = Boat Side (if it is not in the initial bank, it needs an extra cross)
