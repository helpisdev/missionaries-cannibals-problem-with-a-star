package gr.kapareliotis.ilias.missionariesCannibals;

import gr.kapareliotis.ilias.aStarInterface.Node;
import gr.kapareliotis.ilias.aStarInterface.Tree;

import java.util.*;

public class AStarTreeImpl extends Tree {
    private final int numberOfPeople;
    private final int boatCapacity;
    private final int maxDepth;
    private final HashSet<NodeImpl> reached = new HashSet<>();
    private final ArrayList<int[]> pairs = new ArrayList<>();
    private int validNodes = 0;
    private int invalidCombinations = 0;

    public AStarTreeImpl(Node root, int numberOfPeople, int boatCapacity, int maxDepth) {
        super(root);
        this.numberOfPeople = numberOfPeople;
        this.boatCapacity = boatCapacity;
        this.maxDepth = maxDepth;
        this.createPairs();
    }

    private void createPairs() {
        for (int cannibals = 0; cannibals <= this.boatCapacity; ++cannibals) {
            for (int missionaries = 0; missionaries <= this.boatCapacity; ++missionaries) {
                if ((!(missionaries == 0 && cannibals == 0))) {
                    if (cannibals > missionaries) {
                        continue;
                    }
                    if (cannibals + missionaries > this.boatCapacity) {
                        break;
                    }
                    this.pairs.add(new int[]{missionaries, cannibals});
                }
            }
            if (cannibals > 0) {
                this.pairs.add(new int[]{0, cannibals});
            }
        }
    }

    public ArrayList<int[]> getPairs() {
        return this.pairs;
    }

    public int getNumberOfPeople() {
        return this.numberOfPeople;
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

    public int getValidNodes() {
        return this.validNodes;
    }

    public void increaseValidNodes() {
        ++this.validNodes;
    }

    public int getInvalidCombinations() {
        return this.invalidCombinations;
    }

    public void increaseInvalidCombinations() {
        ++this.invalidCombinations;
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
