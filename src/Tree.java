import processing.core.PImage;

import java.util.List;

public class Tree extends Plant{
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
        this.actionPeriod = TREE_ACTION_PERIOD;
        this.animationPeriod = TREE_ANIMATION_PERIOD;
    }




    public void executeTreeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {

        if (!this.transformPlant(world, scheduler, imageStore)) {

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