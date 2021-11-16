package gr.kapareliotis.ilias.aStarInterface;

import java.util.Comparator;
import java.util.LinkedList;

abstract public class Node implements Comparator<Node>, Comparable<Node> {
    private final Node parent;
    private final LinkedList<Node> children = new LinkedList<>();
    private final Comparator<Node> nodeComparator;
    private int depth = 0;
    private Tree belongingTree;

    protected Node(Node parent) {
        this.parent = parent;
        this.nodeComparator = Node::compareTo;
        if (this.parent != null) {
            this.depth = this.parent.getDepth() + 1;
        }
    }

    public Node getParent() {
        return this.parent;
    }

    public LinkedList<Node> getChildren() {
        return this.children;
    }

    public int getDepth() {
        return this.depth;
    }

    abstract public int getHeuristicCost();

    abstract public int getActualCost();

    abstract public int getEvaluationCost();

    public abstract void createChildren();

    public Tree getBelongingTree() {
        return this.belongingTree;
    }

    public void setBelongingTree(Tree belongingTree) {
        this.belongingTree = belongingTree;
    }

    public Comparator<Node> getNodeComparator() {
        return this.nodeComparator;
    }

    @Override
    public int compare(Node firstNode, Node secondNode) {
        // total cost
        if (firstNode.getEvaluationCost() < secondNode.getEvaluationCost()) {
            return -1;
        } else if (firstNode.getEvaluationCost() > secondNode.getEvaluationCost()) {
            return 1;
        }

        // global cost
        if (firstNode.getActualCost() < secondNode.getActualCost()) {
            return -1;
        } else if (firstNode.getActualCost() > secondNode.getActualCost()) {
            return 1;
        }

        return 0;
    }

    @Override
    public int compareTo(Node otherNode) {
        return this.compare(this, otherNode);
    }
}
