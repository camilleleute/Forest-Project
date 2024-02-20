import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Dude implements Entity{
    // Static variables
    public static final String DUDE_KEY = "dude";
    public static final int DUDE_NUM_PROPERTIES = 3;
    public static final int DUDE_ACTION_PERIOD = 0;
    public static final int DUDE_ANIMATION_PERIOD = 1;
    public static final int DUDE_LIMIT = 2;

    // Instance variables
    private final String id;
    private Point position;
    private final List<PImage> images;
    private final int resourceLimit;
    private int resourceCount;
    private final double actionPeriod;
    private final double animationPeriod;


    public Dude(String id, Point position, double actionPeriod, double animationPeriod, int resourceLimit, int resourceCount,
                List<PImage> images) {
        this.id = id;
        this.position = position;
        this.images = images;
        this.resourceLimit = DUDE_LIMIT;
        this.resourceCount = resourceCount;
        this.actionPeriod = actionPeriod;
        this.animationPeriod = DUDE_ANIMATION_PERIOD;
    }
    public String getId() {
        return id;
    }
    public Point getPosition() {
        return position;
    }
    public void setPosition(Point position) {
        this.position = position;
    }


    public Point nextPositionDude(WorldModel world, Point destPos) {
        int horiz = Integer.signum(destPos.x - this.position.x);
        Point newPos = new Point(this.position.x + horiz, this.position.y);

        if (horiz == 0 || world.isOccupied(newPos) && world.getOccupancyCell(newPos).getKind() != EntityKind.STUMP) {
            int vert = Integer.signum(destPos.y - this.position.y);
            newPos = new Point(this.position.x, this.position.y + vert);

            if (vert == 0 || world.isOccupied(newPos) && world.getOccupancyCell(newPos).getKind() != EntityKind.STUMP) {
                newPos = this.position;
            }
        }

        return newPos;
    }



    public boolean moveToFull(WorldModel world, Entity target, EventScheduler scheduler) {
        if (this.position.adjacent(target.getPosition())) {
            return true;
        } else {
            Point nextPos = this.nextPositionDude(world, target.getPosition());

            if (!this.position.equals(nextPos)) {
                world.moveEntity(scheduler, this, nextPos);
            }
            return false;
        }
    }

    public boolean moveToNotFull(WorldModel world, Entity target, EventScheduler scheduler) {
        if (this.position.adjacent(target.getPosition())) {
            this.resourceCount += 1;
            target.health--;
            return true;
        } else {
            Point nextPos = this.nextPositionDude(world, target.getPosition());

            if (!this.position.equals(nextPos)) {
                world.moveEntity(scheduler, this, nextPos);
            }
            return false;
        }
    }
    public void transformFull(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        Entity dude = createDudeNotFull(this.id, this.position, this.actionPeriod, this.animationPeriod, this.resourceLimit, this.images);

        world.removeEntity(scheduler, this);

        world.addEntity(dude);
        dude.scheduleActions(scheduler, world, imageStore);
    }

    public boolean transformNotFull(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        if (this.resourceCount >= this.resourceLimit) {
            Entity dude = createDudeFull(this.id, this.position, this.actionPeriod, this.animationPeriod, this.resourceLimit, this.images);

            world.removeEntity(scheduler, this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(dude);
            dude.scheduleActions(scheduler, world, imageStore);

            return true;
        }

        return false;
    }

    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        switch (this.kind) {
            case DUDE_FULL:
                scheduler.scheduleEvent(this, Action.createActivityAction(this, world, imageStore), this.actionPeriod);
                scheduler.scheduleEvent(this, Action.createAnimationAction(this, 0), getAnimationPeriod());
                break;

            case DUDE_NOT_FULL:
                    scheduler.scheduleEvent(this, Action.createActivityAction(this, world, imageStore), this.actionPeriod);
                    scheduler.scheduleEvent(this, Action.createAnimationAction(this, 0), getAnimationPeriod());
                    break;

                default:
            }
        }

        public void executeDudeFullActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
            Optional<Entity> fullTarget = world.findNearest(this.position, new ArrayList<>(List.of(EntityKind.HOUSE)));

            if (fullTarget.isPresent() && this.moveToFull(world, fullTarget.get(), scheduler)) {
                this.transformFull(world, scheduler, imageStore);
            } else {
                scheduler.scheduleEvent(this, Action.createActivityAction(this, world, imageStore), this.actionPeriod);
            }
        }

        public void executeDudeNotFullActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
            Optional<Entity> target = world.findNearest(this.position, new ArrayList<>(Arrays.asList(EntityKind.TREE, EntityKind.SAPLING)));

            if (target.isEmpty() || !this.moveToNotFull(world, target.get(), scheduler) || !this.transformNotFull(world, scheduler, imageStore)) {
                scheduler.scheduleEvent(this, Action.createActivityAction(this, world, imageStore), this.actionPeriod);
            }
        }


        public void executeTreeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {

            if (!this.transformPlant(world, scheduler, imageStore)) {

                scheduler.scheduleEvent(this, Action.createActivityAction(this, world, imageStore), this.actionPeriod);
            }
        }


        // need resource count, though it always starts at 0
        public static Entity createDudeNotFull(String id, Point position, double actionPeriod, double animationPeriod, int resourceLimit, List<PImage> images) {
            return new Dude(EntityKind.DUDE_NOT_FULL, id, position, images, resourceLimit, 0, actionPeriod, animationPeriod, 0, 0);
        }

        public static Entity createDudeFull(String id, Point position, double actionPeriod, double animationPeriod, int resourceLimit, List<PImage> images) {
            return new Dude(EntityKind.DUDE_FULL, id, position, images, resourceLimit, 0, actionPeriod, animationPeriod, 0, 0);
        }
    }


