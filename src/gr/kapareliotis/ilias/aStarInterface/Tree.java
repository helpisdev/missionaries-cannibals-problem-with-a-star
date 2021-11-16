package gr.kapareliotis.ilias.aStarInterface;

import java.util.LinkedList;
import java.util.PriorityQueue;

abstract public class Tree implements AStarFunctions {
    private final PriorityQueue<Node> frontier;
    private final Node root;

    protected Tree(Node root) {
        this.frontier = new PriorityQueue<>(root.getNodeComparator());
        this.root = root;
        this.frontier.add(root);
    }

    public Node expandNode() {
        Node expandedNode = this.frontier.poll();
        if (expandedNode == null) return null;

        return this.checkRestriction(expandedNode);
    }

    abstract protected Node checkRestriction(Node expandedNode);

    public Node getRoot() {
        return this.root;
    }

    protected void addNodes(LinkedList<Node> children) {
        this.frontier.addAll(children);
    }
}
