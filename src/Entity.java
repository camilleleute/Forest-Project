import java.util.*;

import processing.core.PImage;

/**
 * An entity that exists in the world. See EntityKind for the
 * different kinds of entities that exist.
 */
public interface Entity {
    //EntityKind kind = null;
    String id = null;

    Point position = null;
    int imageIndex = 0;
    int resourceLimit = 0;
    int resourceCount = 0;
    double actionPeriod = 0;
    double animationPeriod = 0;
    int health = 0;
    int healthLimit = 0;

    public static Optional<Entity> nearestEntity(List<Entity> entities, Point pos) {
        if (entities.isEmpty()) {
            return Optional.empty();
        } else {
            Entity nearest = entities.get(0);
            int nearestDistance = nearest.getPosition().distanceSquared(pos);

            for (Entity other : entities) {
                int otherDistance = other.getPosition().distanceSquared(pos);

                if (otherDistance < nearestDistance) {
                    nearest = other;
                    nearestDistance = otherDistance;
                }
            }

            return Optional.of(nearest);
        }
    }
    // Should be in everything i think
    // Default methods

    String getId();
    Point getPosition();
    void setPosition(Point position);
    void nextImage();
    PImage getCurrentImage();

        // Should I put scheduleActions in an interface and implement them in the classes specified below?
//     default void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
//        switch (this) {
//            case DudeFull.class: //DUDE_FULL
//                scheduler.scheduleEvent(this, Action.createActivityAction(this, world, imageStore), this.actionPeriod);
//                scheduler.scheduleEvent(this, Action.createAnimationAction(this, 0), DudeFull.getAnimationPeriod());
//                break;
//
//            case DudeNotFull.class:
//                scheduler.scheduleEvent(this, Action.createActivityAction(this, world, imageStore), this.actionPeriod);
//                scheduler.scheduleEvent(this, Action.createAnimationAction(this, 0), getAnimationPeriod());
//                break;
//
//            case Obstacle.class:
//                scheduler.scheduleEvent(this, Action.createAnimationAction(this, 0), getAnimationPeriod());
//                break;
//
//            case Fairy.class:
//                scheduler.scheduleEvent(this, Action.createActivityAction(this, world, imageStore), this.actionPeriod);
//                scheduler.scheduleEvent(this, Action.createAnimationAction(this, 0), getAnimationPeriod());
//                break;
//
//            case Sapling.class:
//                scheduler.scheduleEvent(this, Action.createActivityAction(this, world, imageStore), this.actionPeriod);
//                scheduler.scheduleEvent(this, Action.createAnimationAction(this, 0), getAnimationPeriod());
//                break;
//
//            case Tree.class:
//                scheduler.scheduleEvent(this, Action.createActivityAction(this, world, imageStore), this.actionPeriod);
//                scheduler.scheduleEvent(this, Action.createAnimationAction(this, 0), getAnimationPeriod());
//                break;
//
//            default:
//        }
//    }


//    default double getAnimationPeriod() {
//        return switch (this) {
//            case Dude.class, DUDE_NOT_FULL, Obstacle.class, Fairy.class, Sapling.class, Tree.class -> this.animationPeriod;
//            default ->
//                    throw new UnsupportedOperationException(String.format("getAnimationPeriod not supported for %s", this));
//        };
//    }

    /**
     * Helper method for testing. Preserve this functionality while refactoring.
     */
     default String log(){
        return this.id.isEmpty() ? null :
                String.format("%s %d %d %d", this.id, this.position.x, this.position.y, this.imageIndex);
    }

     static Entity createHouse(String id, Point position, List<PImage> images) {
        return new House(id, position, images);
    }

     static Entity createObstacle(String id, Point position, double animationPeriod, List<PImage> images) {
        return new Obstacle(id, position,animationPeriod, images);
    }

     static Entity createTree(String id, Point position, double actionPeriod, double animationPeriod, int health, List<PImage> images) {
        return new Tree(id, position, actionPeriod, animationPeriod, health, images);
    }

     static Entity createStump(String id, Point position, List<PImage> images) {
        return new Stump(id, position, images);
    }

    // health starts at 0 and builds up until ready to convert to Tree
     static Entity createSapling(String id, Point position, List<PImage> images, int health) {
        return new Sapling(id, position, images, health, Sapling.SAPLING_ACTION_ANIMATION_PERIOD, Sapling.SAPLING_ACTION_ANIMATION_PERIOD, Sapling.SAPLING_HEALTH_LIMIT);
    }

     static Entity createFairy(String id, Point position, double actionPeriod, double animationPeriod, List<PImage> images) {
        return new Fairy(id, position, actionPeriod, animationPeriod, images);
    }

    // need resource count, though it always starts at 0
     static Entity createDudeNotFull(String id, Point position, double actionPeriod, double animationPeriod, int resourceLimit, int resourceCount, List<PImage> images) {
        return new DudeNotFull(id, position, actionPeriod, animationPeriod, resourceLimit, resourceCount, images);
    }

     static Entity createDudeFull(String id, Point position, double actionPeriod, double animationPeriod, int resourceLimit, List<PImage> images) {
        return new DudeFull(id, position, actionPeriod, animationPeriod,  resourceLimit, 2, images);
    }


}
