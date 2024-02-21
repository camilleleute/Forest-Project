import processing.core.PImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Fairy implements Entity, ExecuteActivity, NextPosition, ScheduleActions {
    public static final String FAIRY_KEY = "fairy";
    public static final int FAIRY_NUM_PROPERTIES = 2;
    public static final int FAIRY_ACTION_PERIOD = 1;
    public static final int FAIRY_ANIMATION_PERIOD = 0;
    private final String id;
    private Point position;
    private final List<PImage> images;
    private final double actionPeriod;
    private final double animationPeriod;
    private int imageIndex;

    public Fairy(String id, Point position, double actionPeriod, double animationPeriod, List<PImage> images) {
        this.id = id;
        this.position = position;
        this.images = images;
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

        if (horiz == 0 || world.isOccupied(newPos)) {
            int vert = Integer.signum(destPos.y - this.position.y);
            newPos = new Point(this.position.x, this.position.y + vert);

            if (vert == 0 || world.isOccupied(newPos)) {
                newPos = this.position;
            }
        }

        return newPos;
    }
    @Override
    public boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler) {
        if (this.position.adjacent(target.position)) {
            world.removeEntity(scheduler, target);
            return true;
        } else {
            Point nextPos = this.nextPosition(world, target.position);

            if (!this.position.equals(nextPos)) {
                world.moveEntity(scheduler, this, nextPos);
            }
            return false;
        }
    }
    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> fairyTarget = world.findNearest(this.position, new ArrayList<>(List.of(Stump.class)));

        if (fairyTarget.isPresent()) {
            Point tgtPos = fairyTarget.get().getPosition();

            if (this.moveTo(world, fairyTarget.get(), scheduler)) {

                Entity sapling = Entity.createSapling(Sapling.SAPLING_KEY + "_" + fairyTarget.get().getId(), tgtPos, imageStore.getImageList(Sapling.SAPLING_KEY), 0);

                world.addEntity(sapling);
                ((ScheduleActions)sapling).scheduleActions(scheduler, world, imageStore);
            }
        }

        scheduler.scheduleEvent(this, Action.createActivityAction(this, world, imageStore), this.actionPeriod);
    }
}
