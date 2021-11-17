package gr.kapareliotis.ilias.missionariesCannibals;

import gr.kapareliotis.ilias.aStarInterface.Node;
import gr.kapareliotis.ilias.aStarInterface.Tree;

import java.util.ArrayList;
import java.util.Objects;

public class NodeImpl extends Node {
    private int missionaries = 0;
    private int cannibals = 0;
    private int mOnBank2 = 0;
    private int cOnBank2 = 0;
    private int mOnBoat = 0;
    private int cOnBoat = 0;

    public NodeImpl(Node parent) {
        super(parent);
    }

    private NodeImpl(Node parent, int missionaries, int cannibals, int mOnBoat, int cOnBoat, int mOnBank2, int cOnBank2) {
        super(parent);
        this.missionaries = missionaries;
        this.cannibals = cannibals;
        this.mOnBoat = mOnBoat;
        this.cOnBoat = cOnBoat;
        this.mOnBank2 = mOnBank2;
        this.cOnBank2 = cOnBank2;
        this.setBelongingTree(parent.getBelongingTree());
    }

    private ArrayList<int[]> movePeople() {
        ArrayList<int[]> validCombos = new ArrayList<>();
        ArrayList<int[]> possiblePairs = this.getBelongingTree().getPossiblePairs();

        final boolean isBoatLeft = this.getBelongingTree().getBoatSide(this) == RiverBank.LEFT;
        final int totalMissionaries = this.mOnBoat + (isBoatLeft ? this.missionaries : this.mOnBank2);
        final int totalCannibals = this.cOnBoat + (isBoatLeft ? this.cannibals : this.cOnBank2);

        for (int[] pair : possiblePairs) {
            final int newMissionaries = totalMissionaries - pair[0];
            final int newCannibals = totalCannibals - pair[1];
            if (newCannibals <= newMissionaries && newCannibals >= 0) {
                validCombos.add(new int[]{newMissionaries, newCannibals, pair[0], pair[1]});
            } else {
                this.getBelongingTree().invalidNodes++;
            }
        }

        return validCombos;
    }

    @Override
    public void createChildren() {
        ArrayList<int[]> validCombos = this.movePeople();
        final boolean isBoatLeft = this.getBelongingTree().getBoatSide(this) == RiverBank.LEFT;

        for (int[] people : validCombos) {
            final int missionariesLeft = isBoatLeft ? people[0] : this.missionaries;
            final int missionariesRight = isBoatLeft ? this.mOnBank2 : people[0];
            final int cannibalsLeft = isBoatLeft ? people[1] : this.cannibals;
            final int cannibalsRight = isBoatLeft ? this.cOnBank2 : people[1];

            NodeImpl child = new NodeImpl(
                    this,
                    missionariesLeft,
                    cannibalsLeft,
                    people[2],
                    people[3],
                    missionariesRight,
                    cannibalsRight
            );
            if (!this.getBelongingTree().getReached().contains(child)) {
                super.getChildren().add(child);
                this.getBelongingTree().validNodes++;
            }
        }
    }

    public int getMissionaries() {
        return this.missionaries;
    }

    public void setMissionaries(int missionaries) {
        this.missionaries = missionaries;
    }

    public int getCannibals() {
        return this.cannibals;
    }

    public void setCannibals(int cannibals) {
        this.cannibals = cannibals;
    }

    public int getNumberOfPeople() {
        return this.missionaries + this.cannibals
                + (this.getBelongingTree().getBoatSide(this) == RiverBank.LEFT ? this.cOnBoat + this.mOnBoat : 0);
    }

    public void setMisOnBank2(int mOnBank2) {
        this.mOnBank2 = mOnBank2;
    }

    public void setCanOnBank2(int cOnBank2) {
        this.cOnBank2 = cOnBank2;
    }

    public void setMisOnBoat(int mOnBoat) {
        this.mOnBoat = mOnBoat;
    }

    public void setCanOnBoat(int cOnBoat) {
        this.cOnBoat = cOnBoat;
    }

    @Override
    public AStarTreeImpl getBelongingTree() {
        return (AStarTreeImpl) super.getBelongingTree();
    }

    @Override
    public void setBelongingTree(Tree belongingTree) {
        super.setBelongingTree(belongingTree);
    }

    @Override
    public int getHeuristicCost() {
        return this.getBelongingTree().calcHeuristicCost(this);
    }

    @Override
    public int getActualCost() {
        return this.getBelongingTree().calcActualCost(this);
    }

    @Override
    public int getEvaluationCost() {
        return this.getBelongingTree().evaluateNode(this);
    }

    @Override
    public String toString() {
        final boolean isBoatLeft = this.getBelongingTree().getBoatSide(this) == RiverBank.LEFT;
        final String boatRepresentation =
                isBoatLeft ? "<" + this.mOnBoat + ", " + this.cOnBoat + ">______" :
                        "______<" + this.mOnBoat + ", " + this.cOnBoat + ">";
        return String.format(
                "(%d, %d, %s, %d, %d, Side: %d)",
                this.missionaries,
                this.cannibals,
                boatRepresentation,
                this.mOnBank2,
                this.cOnBank2,
                this.getBelongingTree().getBoatSide(this).getBankValue()
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NodeImpl node)) return false;
        return this.missionaries
                == node.missionaries && this.cannibals
                == node.cannibals && this.mOnBank2
                == node.mOnBank2 && this.cOnBank2
                == node.cOnBank2 && this.mOnBoat
                == node.mOnBoat && this.cOnBoat
                == node.cOnBoat;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                this.missionaries,
                this.cannibals,
                this.mOnBank2,
                this.cOnBank2,
                this.mOnBoat,
                this.cOnBoat
        );
    }
}
