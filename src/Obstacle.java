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

    public Obstacle(String id, Point position, double animationPeriod, List<PImage> images) {
        this.id = id;
        this.position = position;
        this.images = images;
        this.animationPeriod = animationPeriod;
    }
}