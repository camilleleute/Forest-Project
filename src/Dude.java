import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public abstract class Dude implements Entity, ExecuteActivity, NextPosition, ScheduleActions, PathingStrategy {
    // Static variables
    public static final String DUDE_KEY = "dude";
    public static final int DUDE_NUM_PROPERTIES = 3;
    public static final int DUDE_ACTION_PERIOD = 0;
    public static final int DUDE_ANIMATION_PERIOD = 1;
    public static final int DUDE_LIMIT = 2;

    // Instance variables
    protected final String id;
    protected Point position;
    protected final List<PImage> images;
    protected final int resourceLimit;
    protected int resourceCount;
    protected final double actionPeriod;
    protected final double animationPeriod;
    protected int imageIndex;


    public Dude(String id, Point position, double actionPeriod, double animationPeriod, int resourceLimit, int resourceCount,
                List<PImage> images) {
        this.id = id;
        this.position = position;
        this.images = images;
        this.resourceLimit = resourceLimit;
        this.resourceCount = resourceCount;
        this.actionPeriod = actionPeriod;
        this.animationPeriod = animationPeriod;
    }

    public String getId() {
        return id;
    }
    public Point getPosition() {
        return position;
    }

    public int getImageIndex() { return imageIndex;}

    public void setPosition(Point position) {
        this.position = position;
    }
    public PImage getCurrentImage(){
        return this.images.get(this.imageIndex % this.images.size());
    }
    public void nextImage() {
        this.imageIndex = this.imageIndex + 1;
    }

    public double getAnimationPeriod() {
        return animationPeriod;
    }

    public double getActionPeriod() {
        return actionPeriod;
    }

    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this, Action.createActivityAction(this, world, imageStore), getActionPeriod());
        scheduler.scheduleEvent(this, Action.createAnimationAction(this, 0), getAnimationPeriod());
    }

    @Override
    public Point nextPosition(WorldModel world, Point destPos) {
        Point start = getPosition();
        List<Point> newPos = new SingleStepPathingStrategy().computePath(start, destPos,
                p -> world.withinBounds(p) && !world.isOccupied(p),(p1, p2) -> p1.adjacent(p2),
                PathingStrategy.CARDINAL_NEIGHBORS);
        if (newPos.isEmpty()){
            return start;
        }
        return newPos.get(0);
    }

}


