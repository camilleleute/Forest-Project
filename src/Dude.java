import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public abstract class Dude implements Entity, ExecuteActivity, NextPosition, ScheduleActions {
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

    @Override
    public String getId() {
        return id;
    }
    @Override
    public Point getPosition() {
        return position;
    }

    @Override
    public void setPosition(Point position) {
        this.position = position;
    }
    @Override
    public PImage getCurrentImage(){
        return this.images.get(this.imageIndex % this.images.size());
    }
    @Override
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
        int horiz = Integer.signum(destPos.x - this.position.x);
        Point newPos = new Point(this.position.x + horiz, this.position.y);

        if (horiz == 0 || world.isOccupied(newPos) && world.getOccupancyCell(newPos).getClass() != Stump.class) {
            int vert = Integer.signum(destPos.y - this.position.y);
            newPos = new Point(this.position.x, this.position.y + vert);

            if (vert == 0 || world.isOccupied(newPos) && world.getOccupancyCell(newPos).getClass() != Stump.class) {
                newPos = this.position;
            }
        }

        return newPos;
    }

}


