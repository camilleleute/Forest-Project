import processing.core.PImage;

import java.util.List;

public class Obstacle implements Entity {
    public static final String OBSTACLE_KEY = "obstacle";
    public static final int OBSTACLE_NUM_PROPERTIES = 1;
    public static final int OBSTACLE_ANIMATION_PERIOD = 0;

    private final String id;
    private Point position;
    private final List<PImage> images;
    private final double animationPeriod;
    private int imageIndex;

    public Obstacle(String id, Point position, double animationPeriod, List<PImage> images) {
        this.id = id;
        this.position = position;
        this.images = images;
        this.animationPeriod = animationPeriod;
    }

    public double getAnimationPeriod() {
        return OBSTACLE_ANIMATION_PERIOD;
    }

    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore){
        scheduler.scheduleEvent(this, Action.createAnimationAction(this, 0), getAnimationPeriod());
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
}