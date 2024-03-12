import processing.core.PImage;

import java.util.List;
import java.util.Objects;

public class Merman implements Entity, ExecuteActivity, NextPosition, ScheduleActions{


    @Override
    public String getId() {
        return null;
    }

    @Override
    public Point getPosition() {
        return null;
    }

    @Override
    public void setPosition(Point position) {

    }

    @Override
    public void nextImage() {

    }

    @Override
    public PImage getCurrentImage() {
        return null;
    }

    @Override
    public int getImageIndex() {
        return 0;
    }

    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {

    }

    public Point nextPosition(WorldModel world, Point destPos) {
        Point start = getPosition();
        List<Point> newPos = new AStarPathingStrategy().computePath(start, destPos,
                p -> world.withinBounds(p) && !world.isOccupied(p) && Objects.equals(world.getBackgroundCell(p).getId(), "lake"),
                (p1, p2) -> p1.adjacent(p2),
                PathingStrategy.CARDINAL_NEIGHBORS);
        if (newPos.isEmpty()){
            return start;
        }
        return newPos.get(0);
    }

    @Override
    public boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler) {
        return false;
    }

    @Override
    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {

    }

    @Override
    public double getAnimationPeriod() {
        return 0;
    }
}
