import processing.core.PImage;

import java.util.List;

public class Sapling extends Plant {
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
        this.actionPeriod = SAPLING_ACTION_ANIMATION_PERIOD;
        this.animationPeriod = SAPLING_ACTION_ANIMATION_PERIOD;
        this.health = SAPLING_HEALTH;
        this.healthLimit = SAPLING_HEALTH_LIMIT;
    }

    @Override
    public PImage getCurrentImage(){
        return this.images.get(this.imageIndex % this.images.size());
    }
    @Override
    public void nextImage() {
        this.imageIndex = this.imageIndex + 1;
    }

    public void executeSaplingActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        this.health++;
        if (!this.transformPlant(world, scheduler, imageStore)) {
            scheduler.scheduleEvent(this, Action.createActivityAction(this, world, imageStore), this.actionPeriod);
        }
    }



}


