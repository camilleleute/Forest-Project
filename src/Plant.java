import processing.core.PImage;

import java.util.List;

public abstract class Plant implements Entity, Transform, ScheduleActions {
    public static final double TREE_ANIMATION_MAX = 0.600;
    public static final double TREE_ANIMATION_MIN = 0.050;
    public static final double TREE_ACTION_MAX = 1.400;
    public static final double TREE_ACTION_MIN = 1.000;
    public static final int TREE_HEALTH_MAX = 3;
    public static final int TREE_HEALTH_MIN = 1;
    protected final String id;
    protected Point position;
    protected final List<PImage> images;
    protected int imageIndex;
    protected static int health;



    public Plant(String id, Point position, List<PImage> images, int health) {
        this.id = id;
        this.position = position;
        this.images = images;
        this.health = health;
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

    public static int getHealth() {
        return health;
    }



        // How do I reference a different transform method?
    @Override
    public boolean transform(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        if (this.getClass() == Tree.class) {
            return ((Tree)this).transform(world, scheduler, imageStore);
        } else if (this.getClass() == Sapling.class) {
            return ((Sapling)this).transform(world, scheduler, imageStore);
        } else {
            throw new UnsupportedOperationException(String.format("transformPlant not supported for %s", this));
        }
    }


}
