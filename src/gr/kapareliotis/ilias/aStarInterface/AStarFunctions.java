package gr.kapareliotis.ilias.aStarInterface;

interface AStarFunctions {
    int calcHeuristicCost(Node node);

    int calcActualCost(Node node);

    default int evaluateNode(Node node) {
        return calcHeuristicCost(node) + calcActualCost(node);
    }
}