import processing.core.PImage;

import java.util.List;

public class Tree extends Plant implements ExecuteActivity{
    public static final String TREE_KEY = "tree";
    public static final int TREE_NUM_PROPERTIES = 3;
    public static final int TREE_ANIMATION_PERIOD = 0;
    public static final int TREE_HEALTH = 2;

    public static final int TREE_ACTION_PERIOD = 1;

    // Instance variables
    private final double actionPeriod;
    private final double animationPeriod;


    public Tree(String id, Point position, double actionPeriod, double animationPeriod, int health, List<PImage> images) {
        super(id, position, images, health);
        this.actionPeriod = actionPeriod;
        this.animationPeriod = animationPeriod;
    }

    @Override
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
    public boolean transform(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        if (health <= 0) {
            Entity stump = Entity.createStump(Stump.STUMP_KEY + "_" + this.id, this.position, imageStore.getImageList(Stump.STUMP_KEY));

            world.removeEntity(scheduler, this);

            world.addEntity(stump);

            return true;
        }

        return false;
    }


    // Issue with referencing diff transform method again
    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {

        if (!((Plant)this).transform(world, scheduler, imageStore)) {

            scheduler.scheduleEvent(this, Action.createActivityAction(this, world, imageStore), this.actionPeriod);
        }
    }

    @Override
    public PImage getCurrentImage(){
        return this.images.get(this.imageIndex % this.images.size());
    }
    @Override
    public void nextImage() {
        this.imageIndex = this.imageIndex + 1;
    }
}