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

    private ArrayList<int[]> movePeopleToTheOtherBank() {
        int mOnCurBank;
        int cOnCurBank;
        final int boatCapacity = this.getBelongingTree().getBoatCapacity();

        if (this.getBelongingTree().getBoatSide(this) == RiverBank.LEFT) {
            mOnCurBank = this.missionaries;
            cOnCurBank = this.cannibals;
        } else {
            mOnCurBank = this.mOnBank2;
            cOnCurBank = this.cOnBank2;
        }

        mOnCurBank += this.mOnBoat;
        cOnCurBank += this.cOnBoat;

        final ArrayList<int[]> combinations = new ArrayList<>();
        final int mLimit = Math.min(boatCapacity, mOnCurBank);
        final int cLimit = Math.min(boatCapacity, cOnCurBank);
        final int peopleLimit = Math.min(boatCapacity, mOnCurBank + cOnCurBank);

        for (int mOnBoard = 0; mOnBoard <= mLimit; ++mOnBoard) {
            int temp = Math.max(cLimit - mOnBoard, 0);
            for (int cOnBoard = 0; cOnBoard <= cLimit; ++cOnBoard) {
                final boolean isLegal = (mOnBoard + cOnBoard != 0)
                        && this.isComboLegal(cOnBoard, mOnBoard)
                        && this.isComboLegal(mOnBoard, mLimit)
                        && this.isComboLegal(cOnBoard, cLimit)
                        && this.isComboLegal(mOnCurBank - mOnBoard, cOnCurBank - cOnBoard)
                        && this.isComboLegal(cOnBoard + mOnBoard, peopleLimit);

                if (isLegal) {
                    combinations.add(new int[]{mOnBoard, cOnBoard});
                }
            }
        }

        return combinations;
    }

    private ArrayList<int[]> createBankBoatCombos(int[] peopleCombo) {
        int mOnOtherBank;
        int cOnOtherBank;
        final int boatCapacity = this.getBelongingTree().getBoatCapacity();

        if (this.getBelongingTree().getBoatSide(this) == RiverBank.RIGHT) {
            mOnOtherBank = this.missionaries;
            cOnOtherBank = this.cannibals;
        } else {
            mOnOtherBank = this.mOnBank2;
            cOnOtherBank = this.cOnBank2;
        }

        mOnOtherBank += peopleCombo[0];
        cOnOtherBank += peopleCombo[1];

        final ArrayList<int[]> combinations = new ArrayList<>();
        final int mLimit = Math.min(boatCapacity, mOnOtherBank);
        final int cLimit = Math.min(boatCapacity, cOnOtherBank);
        final int peopleLimit = Math.min(boatCapacity, mOnOtherBank + cOnOtherBank);

        for (int mOnBoard = 0; mOnBoard <= mLimit; ++mOnBoard) {
            for (int cOnBoard = 0; cOnBoard <= cLimit; ++cOnBoard
            ) {
                final boolean isLegal = this.isComboLegal(cOnBoard, mOnBoard)
                        && this.isComboLegal(mOnBoard, mLimit)
                        && this.isComboLegal(cOnBoard, cLimit)
                        && this.isComboLegal(mOnOtherBank - mOnBoard, cOnOtherBank - cOnBoard)
                        && this.isComboLegal(cOnBoard + mOnBoard, peopleLimit);

                if (isLegal) {
                    combinations.add(new int[]{mOnOtherBank - mOnBoard, cOnOtherBank - cOnBoard, mOnBoard, cOnBoard});
                }
            }
        }

        return combinations;
    }

    private boolean isComboLegal(int people, int limit) {
        if (people < 0 || limit < 0) {
            return false;
        }
        return people <= limit;
    }

    @Override
    public void createChildren() {
        final ArrayList<int[]> moveCombinations = this.movePeopleToTheOtherBank();
        for (int[] movingPeople : moveCombinations) {
            final ArrayList<int[]> validCombinations = this.createBankBoatCombos(movingPeople);
            for (int[] validCombo : validCombinations) {
                final NodeImpl child = new NodeImpl(this);
                final int mOnLeftBank;
                final int cOnLeftBank;
                final int mOnRightBank;
                final int cOnRightBank;
                if (this.getBelongingTree().getBoatSide(this) == RiverBank.LEFT) {
                    mOnRightBank = validCombo[0];
                    cOnRightBank = validCombo[1];
                    mOnLeftBank = this.getBelongingTree().getNumberOfPeople() / 2 - mOnRightBank - validCombo[2];
                    cOnLeftBank = this.getBelongingTree().getNumberOfPeople() / 2 - cOnRightBank - validCombo[3];
                } else {
                    mOnLeftBank = validCombo[0];
                    cOnLeftBank = validCombo[1];
                    mOnRightBank = this.getBelongingTree().getNumberOfPeople() / 2 - mOnLeftBank - validCombo[2];
                    cOnRightBank = this.getBelongingTree().getNumberOfPeople() / 2 - cOnLeftBank - validCombo[3];
                }

                child.setMissionaries(mOnLeftBank);
                child.setCannibals(cOnLeftBank);
                child.setMisOnBank2(mOnRightBank);
                child.setCanOnBank2(cOnRightBank);
                child.setMisOnBoat(validCombo[2]);
                child.setCanOnBoat(validCombo[3]);
                child.setBelongingTree(this.getBelongingTree());

                if (!this.getBelongingTree().getReached().contains(child)) {
                    this.getChildren().add(child);
                }
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
        return String.format(
                "(%d, %d, %d, %d, %d, %d, Side: %d)",
                this.missionaries,
                this.cannibals,
                this.mOnBoat,
                this.cOnBoat,
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
