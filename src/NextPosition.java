public interface NextPosition {
    Point nextPosition(WorldModel world, Point destPos);
    boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler);
}
