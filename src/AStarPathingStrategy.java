import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.*;


class AStarPathingStrategy implements PathingStrategy {


    public List<Point> computePath(Point start, Point end,
                                   Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors) {


        PriorityQueue<Node> openList = new PriorityQueue<>((node1, node2) -> Double.compare(node1.getF(), node2.getF())); //open list with a comparator
        Set<Point> closedList = new HashSet<>();
        Map<Point, Node> allNodes = new HashMap<>();
        Node startNode = new Node(start);
        Node current = null;

        startNode.setG(0);
        startNode.setH(startNode.manhattanDistance(end));
        openList.add(startNode);
        allNodes.put(start, startNode);

        while (!openList.isEmpty()) {
            current = openList.poll();
            if (withinReach.test(current.getPosition(), end)) {
                break;
            }
            if (current.getPosition().equals(end)) {
                return buildPath(current);
            }

            for (Point neighborPos : potentialNeighbors.apply(current.getPosition()).collect(Collectors.toList())) {
                if (canPassThrough.test(neighborPos) && !closedList.contains(neighborPos)) {

                    Node neighborNode;
                    if (allNodes.containsKey(neighborPos)) {

                        neighborNode = allNodes.get(neighborPos);
                    } else {

                        neighborNode = new Node(neighborPos);
                        allNodes.put(neighborPos, neighborNode);
                    }
                    double newPathCost = current.getG() + 1;

                    if (!openList.contains(neighborNode) || newPathCost < neighborNode.getG()) {
                        neighborNode.setPrevious(current);
                        neighborNode.setG(newPathCost);
                        neighborNode.setH(newPathCost + neighborNode.manhattanDistance(end));

                        if (!openList.contains(neighborNode)) {
                            openList.add(neighborNode);
                            allNodes.put(neighborPos, neighborNode);
                        } else {
                            openList.remove(neighborNode);
                            openList.add(neighborNode);
                        }
                    }
                }
                closedList.add(current.getPosition());
            }
        }
        if(current == null){
            return Collections.emptyList();
        }
        else {
            return buildPath(current);
        }
    }
    private List<Point> buildPath(Node target) {
        LinkedList<Point> path = new LinkedList<>();
        Node current = target;
        while (current != null && current.getPrevious() != null) {
            path.addFirst(current.getPosition());

            current = current.getPrevious();
        }
        return path;
    }
}