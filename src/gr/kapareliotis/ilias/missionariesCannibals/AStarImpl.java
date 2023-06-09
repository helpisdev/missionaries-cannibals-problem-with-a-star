package gr.kapareliotis.ilias.missionariesCannibals;

public class AStarImpl {
    private int maxDepth;
    private int boatCapacity;
    private int numberOfPeople;

    private void getInput() {
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        boolean isInputCorrect;
        int missionaries;
        int boatCapacity;
        int maximumDepth;

        do {
            try {
                isInputCorrect = true;

                System.out.print("Enter the number of missionaries: ");
                missionaries = scanner.nextInt();
                if (missionaries < 1) {
                    System.out.println("There must be at least 1 missionary.");
                    isInputCorrect = false;
                    continue;
                }

                this.numberOfPeople = missionaries * 2;

                System.out.print("Enter the boat capacity: ");
                boatCapacity = scanner.nextInt();
                if (boatCapacity <= 1) {
                    System.out.println("The boat must have a capacity of at lest 2 people.");
                    isInputCorrect = false;
                    continue;
                }

                this.boatCapacity = boatCapacity;

                System.out.print("Enter the maximum river crossings: ");
                maximumDepth = scanner.nextInt();
                if (maximumDepth < 1) {
                    System.out.println("Solution depth must be at least 1.");
                    isInputCorrect = false;
                    continue;
                }

                this.maxDepth = maximumDepth;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                isInputCorrect = false;
            }
        } while (!isInputCorrect);
    }

    private NodeImpl searchTree(AStarTreeImpl aStarTree) {
        NodeImpl solution = null;
        NodeImpl node = (NodeImpl) aStarTree.getRoot();
        boolean found = false;

        do {
            if (node == null) {
                break;
            }
            if ((node.getCannibals() == 0 && node.getMissionaries() == 0) && node.getDepth() <= this.maxDepth) {
                solution = node;
                solution.setMisOnBank2(solution.getBelongingTree().getNumberOfPeople() / 2);
                solution.setCanOnBank2(solution.getBelongingTree().getNumberOfPeople() / 2);
                solution.setMisOnBoat(0);
                solution.setCanOnBoat(0);
                found = true;
            }
            node = (NodeImpl) aStarTree.expandNode();
        } while (!found);

        return solution;
    }

    public void run() {
        boolean finished = false;
        NodeImpl solution;

        while (!finished) {
            try {
                this.getInput();
                final long start = System.currentTimeMillis();
                NodeImpl root = new NodeImpl(null);
                AStarTreeImpl aStarTree = new AStarTreeImpl(root, this.numberOfPeople, this.boatCapacity, this.maxDepth);
                aStarTree.getRoot().setBelongingTree(aStarTree);
                ((NodeImpl) aStarTree.getRoot()).setMissionaries(this.numberOfPeople / 2);
                ((NodeImpl) aStarTree.getRoot()).setCannibals(this.numberOfPeople / 2);

                solution = this.searchTree(aStarTree);
                this.printInfo(solution, aStarTree, start);

                finished = true;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void printInfo(NodeImpl solution, AStarTreeImpl aStarTree, long start) {
        java.text.DecimalFormat formatter = new java.text.DecimalFormat("#,###");
        System.out.println(this.produceSolutionDisplay(solution));

        final String results = String.format(
                "%s%s%n%s%s%n%s%s%n%s%s%n%s%s%n",
                "Number of moving people: ",
                formatter.format(this.numberOfPeople),
                "Boat capacity: ",
                formatter.format(this.boatCapacity),
                "Number of max steps: ",
                formatter.format(this.maxDepth),
                "Valid nodes: ",
                formatter.format(aStarTree.getValidNodes()),
                "Combinations examined: ",
                formatter.format(aStarTree.getValidNodes() + aStarTree.getInvalidCombinations())
        );

        System.out.println(results);

        if (solution != null) {
            System.out.println("Heuristic result: " + aStarTree.calcHeuristicCost(aStarTree.getRoot()));
            System.out.println("Number of steps: " + formatter.format(solution.getDepth()));
        }

        final long end = System.currentTimeMillis();
        final long elapsedTime = end - start;
        formatter = new java.text.DecimalFormat("#,###.###");
        System.out.println("Elapsed time (in seconds): " + formatter.format((double)elapsedTime / 1000));
    }

    private String produceSolutionDisplay(NodeImpl solution) {
        String pointer = "\n               |               \n               |               \n               v               \n";

        if (solution != null) {
            if (solution.getParent() != null) {
                return produceSolutionDisplay((NodeImpl) solution.getParent()) + pointer + solution;
            }
            return solution.toString();
        }
        return String.format("No solution found at depth: %d", this.maxDepth);
    }
}
