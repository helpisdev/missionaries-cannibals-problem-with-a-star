package gr.kapareliotis.ilias.missionariesCannibals;

public class AStarImpl {
    private int maxDepth;

    private int[] getInput() {
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        boolean isInputCorrect;
        int numberOfPeople;
        int boatCapacity;
        int maximumDepth;

        int[] input = new int[2];

        do {
            try {
                isInputCorrect = true;

                System.out.print("Enter the number of people: ");
                numberOfPeople = scanner.nextInt();
                if (numberOfPeople <= 1 || numberOfPeople % 2 != 0) {
                    isInputCorrect = false;
                    continue;
                }

                input[0] = numberOfPeople;

                System.out.print("Enter the boat capacity: ");
                boatCapacity = scanner.nextInt();
                if (boatCapacity <= 1) {
                    isInputCorrect = false;
                    continue;
                }

                input[1] = boatCapacity;

                System.out.print("Enter the maximum river crossings: ");
                maximumDepth = scanner.nextInt();
                if (maximumDepth < 1) {
                    isInputCorrect = false;
                    continue;
                }

                this.maxDepth = maximumDepth;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                isInputCorrect = false;
            }
        } while (!isInputCorrect);

        return input;
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
                int[] input = this.getInput();
                final long start = System.currentTimeMillis();
                NodeImpl root = new NodeImpl(null);
                AStarTreeImpl aStarTree = new AStarTreeImpl(root, input[0], input[1], this.maxDepth);
                aStarTree.getRoot().setBelongingTree(aStarTree);
                ((NodeImpl) aStarTree.getRoot()).setMissionaries(input[0] / 2);
                ((NodeImpl) aStarTree.getRoot()).setCannibals(input[0] / 2);

                solution = this.searchTree(aStarTree);
                System.out.println(this.displaySolution(solution));
                java.text.DecimalFormat formatter = new java.text.DecimalFormat("#,###");
                System.out.println("Valid nodes: " + formatter.format(aStarTree.validNodes));
                System.out.println("Invalid nodes: " + formatter.format(aStarTree.invalidNodes));
                System.out.println("Total search graph nodes: "
                        + formatter.format(aStarTree.validNodes + aStarTree.invalidNodes));
                System.out.println("Number of steps: " + formatter.format(solution.getDepth()));
                final long end = System.currentTimeMillis();
                final long elapsedTime = end - start;
                formatter = new java.text.DecimalFormat("#,###.###");
                System.out.println("Elapsed time (in seconds): " + formatter.format((double)elapsedTime / 1000));

                finished = true;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private String displaySolution(NodeImpl solution) {
        String pointer = "\n               |               \n               |               \n               v               \n";

        if (solution != null) {
            if (solution.getParent() != null) {
                return displaySolution((NodeImpl) solution.getParent()) + pointer + solution;
            }
            return solution.toString();
        }
        return String.format("No solution found at depth: %d", this.maxDepth);
    }
}
