/**
 * An action that can be taken by an entity
 */
public interface Action {
//     private final ActionKind kind;
//     private final Entity entity;
//     private final WorldModel world;
//     private final ImageStore imageStore;
//     private final int repeatCount;

//    public Action(ActionKind kind, Entity entity, WorldModel world, ImageStore imageStore, int repeatCount) {
//        this.kind = kind;
//        this.entity = entity;
//        this.world = world;
//        this.imageStore = imageStore;
//        this.repeatCount = repeatCount;
//    }

    void executeAction(EventScheduler scheduler);

    //Why am I getting these errors?
     static Action createActivityAction(Entity entity, WorldModel world, ImageStore imageStore) {
        return new Activity(entity, world, imageStore);
    }

     static Action createAnimationAction(Entity entity, int repeatCount) {
        return new Animation(entity, repeatCount);
    }
}
