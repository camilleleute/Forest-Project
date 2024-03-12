import processing.core.PImage;

import java.util.List;
import java.util.Objects;

public class Merman implements Entity, ExecuteActivity, NextPosition, ScheduleActions{
    private final String id;
    private Point position;
    private final List<PImage> images;
//    private final double actionPeriod;
//    private final double animationPeriod;
    private int imageIndex;
    public Merman(String id, Point position, List<PImage> images) {
        this.id = id;
        this.position = position;
        this.images = images;
//        this.actionPeriod = actionPeriod;
//        this.animationPeriod = animationPeriod;
    }
    public String getId() {
        return id;
    }
    public int getImageIndex() { return imageIndex;}
    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }
    public PImage getCurrentImage(){
        return this.images.get(this.imageIndex % this.images.size());
    }

    public void nextImage() {
        this.imageIndex = this.imageIndex + 1;
    }

//    public double getActionPeriod() {
//        return actionPeriod;
//    }
    public double getAnimationPeriod() {
        return 0.1;
    }
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
    // kills fairies
    @Override
    public boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler) {
        if (this.position.adjacent(target.getPosition())) {
            world.removeEntity(scheduler, target);
            return true;
        } else {
            Point nextPos = this.nextPosition(world, target.getPosition());

            if (!this.position.equals(nextPos)) {
                world.moveEntity(scheduler, this, nextPos);
            }
            return false;
        }
    }



    @Override
    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
//        scheduler.scheduleEvent(this, Action.createActivityAction(this, world, imageStore), getActionPeriod());
        scheduler.scheduleEvent(this, Action.createAnimationAction(this, 0), getAnimationPeriod());
    }

}
