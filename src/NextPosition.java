import java.util.List;

public interface NextPosition {
     default Point nextPosition(WorldModel world, Point destPos) {
        Point start = getPosition();
        List<Point> newPos = new AStarPathingStrategy().computePath(start, destPos,
                p -> world.withinBounds(p) && !world.isOccupied(p),
                (p1, p2) -> p1.adjacent(p2),
                PathingStrategy.CARDINAL_NEIGHBORS);
        if (newPos.isEmpty()){
            return start;
        }
        return newPos.get(0);
    }

    Point getPosition();

    boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler);
}
