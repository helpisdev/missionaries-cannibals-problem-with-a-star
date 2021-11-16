package gr.kapareliotis.ilias.missionariesCannibals;

import gr.kapareliotis.ilias.aStarInterface.Node;
import gr.kapareliotis.ilias.aStarInterface.Tree;

import java.util.HashSet;

public class AStarTreeImpl extends Tree {
    private final int numberOfPeople;
    private final int boatCapacity;
    private final int maxDepth;
    private final HashSet<NodeImpl> reached = new HashSet<>();

    public AStarTreeImpl(Node root, int numberOfPeople, int boatCapacity, int maxDepth) {
        super(root);
        this.numberOfPeople = numberOfPeople;
        this.boatCapacity = boatCapacity;
        this.maxDepth = maxDepth;
    }

    public int getNumberOfPeople() {
        return this.numberOfPeople;
    }

    public int getBoatCapacity() {
        return this.boatCapacity;
    }

    public RiverBank getBoatSide(Node node) {
        return RiverBank.getBank(node.getDepth() % 2);
    }

    public void addReached(NodeImpl node) {
        this.reached.add(node);
    }

    public HashSet<NodeImpl> getReached() {
        return this.reached;
    }

    @Override
    protected Node checkRestriction(Node expandedNode) {
        this.addReached((NodeImpl) expandedNode);
        if (!(expandedNode.getDepth() == this.maxDepth)) {
            expandedNode.createChildren();
            this.addNodes(expandedNode.getChildren());
        }
        return expandedNode;
    }

    @Override
    public int calcHeuristicCost(Node node) {
        final int peopleOnLeftBank = ((NodeImpl) node).getNumberOfPeople();
        final int inevitableCrossings = 2 * ((peopleOnLeftBank - 1) / (this.boatCapacity - 1)) - 1;
        final int peopleLeftToMove = peopleOnLeftBank - 1
                - (peopleOnLeftBank / (this.boatCapacity - 1)) * (this.boatCapacity - 1);
        final boolean twoMoreCrossings = peopleLeftToMove > 0;
        return inevitableCrossings + (twoMoreCrossings ? 2 : 0) + getBoatSide(node).getBankValue();
    }

    @Override
    public int calcActualCost(Node node) {
        return node.getDepth();
    }
}
