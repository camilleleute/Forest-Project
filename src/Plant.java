import processing.core.PImage;

import java.util.List;

public abstract class Plant implements Entity{
    public static final double TREE_ANIMATION_MAX = 0.600;
    public static final double TREE_ANIMATION_MIN = 0.050;
    public static final double TREE_ACTION_MAX = 1.400;
    public static final double TREE_ACTION_MIN = 1.000;
    public static final int TREE_HEALTH_MAX = 3;
    public static final int TREE_HEALTH_MIN = 1;
    private final String id;
    private Point position;
    protected final List<PImage> images;
    protected int imageIndex;
    public static int health;



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

    public boolean transformSapling(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
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
            tree.scheduleActions(scheduler, world, imageStore);

            return true;
        }

        return false;
    }
    public boolean transformPlant(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        if (this.getClass() == Tree.class) {
            return this.transformTree(world, scheduler, imageStore);
        } else if (this.getClass() == Sapling.class) {
            return this.transformSapling(world, scheduler, imageStore);
        } else {
            throw new UnsupportedOperationException(String.format("transformPlant not supported for %s", this));
        }
    }

    public boolean transformTree(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        if (health <= 0) {
            Entity stump = Entity.createStump(Stump.STUMP_KEY + "_" + this.id, this.position, imageStore.getImageList(Stump.STUMP_KEY));

            world.removeEntity(scheduler, this);

            world.addEntity(stump);

            return true;
        }

        return false;
    }
}
