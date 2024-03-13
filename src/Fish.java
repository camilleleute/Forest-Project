import processing.core.PImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Fish implements ScheduleActions, Entity, ExecuteActivity, NextPosition {

    public static final String FISH_KEY = "fish";
    private final String id;
    private Point position;
    private final List<PImage> images;
    private int imageIndex;

    public Fish(String id, Point position, List<PImage> images) {
        this.id = id;
        this.position = position;
        this.images = images;
    }

    public double getAnimationPeriod() {
        return 0.1;
    }
    private double getActionPeriod() {
        return .5;
    }
    @Override
    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this, Action.createActivityAction(this, world, imageStore), getActionPeriod());
        scheduler.scheduleEvent(this, Action.createAnimationAction(this, 0), getAnimationPeriod());
    }

    public String getId() {
        return id;
    }

    public int getImageIndex() {
        return imageIndex;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public PImage getCurrentImage() {
        return this.images.get(this.imageIndex % this.images.size());
    }

    public void nextImage() {
        this.imageIndex = this.imageIndex + 1;
    }

    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> fishTarget = world.findNearest(this.position, new ArrayList<>(List.of(Dude.class)));

        if(fishTarget.isPresent())
        {
            Point tgtPos = fishTarget.get().getPosition();
            if (this.moveTo(world, fishTarget.get(), scheduler))
            {
                Merman merman = new Merman("merman", tgtPos, imageStore.getImageList("merman"));
                world.addEntity(merman);
                ((ScheduleActions)merman).scheduleActions(scheduler, world, imageStore);
                world.removeEntity(scheduler, this);
            } else if (world.withinBounds(this.getPosition())){
                Background lake = new Background("lake", imageStore.getImageList("lake"));
                world.setBackgroundCell(this.getPosition(), lake);
            }

        } else {
            scheduler.unscheduleAllEvents(this);
            this.scheduleActions(scheduler, world, imageStore);

            Optional<Entity> mermanTarget = world.findNearest(this.getPosition(), new ArrayList<>(List.of(Obstacle.class)));
            if (mermanTarget.isPresent()) {
                if (this.moveTo(world, mermanTarget.get(), scheduler)) {
                    world.removeEntity(scheduler, this);
                } else if (world.withinBounds(this.getPosition())){
                    Background lake = new Background("lake", imageStore.getImageList("lake"));
                    world.setBackgroundCell(this.getPosition(), lake);
                }
            }
        }

        scheduler.scheduleEvent(this, Action.createActivityAction(this, world, imageStore), this.getActionPeriod());
    }


    @Override
    public Point nextPosition(WorldModel world, Point destPos) {
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
}


