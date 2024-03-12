import processing.core.PImage;

import java.util.List;

public class Fish implements ScheduleActions, Entity{

    public static final String FISH_KEY = "fish";
    private final String id;
    private Point position;
    private final List<PImage> images;
    private int imageIndex;

    //    private final double actionPeriod;
//    private final double animationPeriod;
    public Fish(String id, Point position, List<PImage> images) {
        this.id = id;
        this.position = position;
        this.images = images;
//        this.actionPeriod = actionPeriod;
//        this.animationPeriod = animationPeriod;
    }

    public double getAnimationPeriod() {
        return 0.1;
    }
//
//    public double getActionPeriod() {
//        return actionPeriod;
//    }
    @Override
    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
//        scheduler.scheduleEvent(this, Action.createActivityAction(this, world, imageStore), getActionPeriod());
        scheduler.scheduleEvent(this, Action.createAnimationAction(this, 0), getAnimationPeriod());
    }

    public String getId() {
        return id;
    }
    public int getImageIndex() { return imageIndex;}
    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }
    public PImage getCurrentImage(){
        return this.images.get(this.imageIndex % this.images.size());
    }

    public void nextImage() {
        this.imageIndex = this.imageIndex + 1;
    }

}

