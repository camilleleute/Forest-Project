import processing.core.PImage;

import java.util.List;

public class Sapling extends Plant implements ExecuteActivity {
    // Static variables
    public static final double SAPLING_ACTION_ANIMATION_PERIOD = 1.000; // have to be in sync since grows and gains health at same time
    public static final int SAPLING_HEALTH_LIMIT = 5;
    public static final String SAPLING_KEY = "sapling";
    public static final int SAPLING_NUM_PROPERTIES = 1;
    public static final int SAPLING_HEALTH = 0;
    private final double actionPeriod;
    private final double animationPeriod;
    private int health;
    private final int healthLimit;

    public Sapling(String id, Point position, List<PImage> images, int health, double actionPeriod,
                   double animationPeriod, int healthLimit) {
        super(id, position, images, health);
        this.actionPeriod = actionPeriod;
        this.animationPeriod = animationPeriod;
        this.health = health;
        this.healthLimit = healthLimit;
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
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        this.health++;
        if (!this.transform(world, scheduler, imageStore)) {
            scheduler.scheduleEvent(this, Action.createActivityAction(this, world, imageStore), this.actionPeriod);
        }
    }
    @Override
    public boolean transform(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        if (health <= 0) {
            Entity stump = Entity.createStump(Stump.STUMP_KEY + "_" + this.id, this.position,
                    imageStore.getImageList(Stump.STUMP_KEY));

            world.removeEntity(scheduler, this);

            world.addEntity(stump);

            return true;
        } else if (this.health >= this.healthLimit) {
            Entity tree = Entity.createTree(Tree.TREE_KEY + "_" + this.id, this.position,
                    Functions.getNumFromRange(TREE_ACTION_MAX, TREE_ACTION_MIN), Functions.getNumFromRange(TREE_ANIMATION_MAX, TREE_ANIMATION_MIN),
                    Functions.getIntFromRange(TREE_HEALTH_MAX, TREE_HEALTH_MIN), imageStore.getImageList(Tree.TREE_KEY));

            world.removeEntity(scheduler, this);

            world.addEntity(tree);
            ((ScheduleActions)tree).scheduleActions(scheduler, world, imageStore);

            return true;
        }

        return false;
    }



}


