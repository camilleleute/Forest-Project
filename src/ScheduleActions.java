public interface ScheduleActions {
     void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore);
//    default void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
//        scheduler.scheduleEvent(this, Action.createActivityAction(this, world, imageStore), getActionPeriod());
//        scheduler.scheduleEvent(this, Action.createAnimationAction(this, 0), getAnimationPeriod());
//    }
    double getAnimationPeriod();
//    double getActionPeriod();

}

//Duplicate code? Weird THIS issues
//Use inheritance? to much work




