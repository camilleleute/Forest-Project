import processing.core.PImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MermanFull extends Merman implements Entity, ExecuteActivity, NextPosition, ScheduleActions{
    public MermanFull(String id, Point position, List<PImage> images) {
        super(id, position, images);
    }


    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> mermanTarget = world.findNearest(this.getPosition(), new ArrayList<>(List.of(Obstacle.class)));

        if (mermanTarget.isPresent()) {
            Point tgtPos = mermanTarget.get().getPosition();

            if (this.moveTo(world, mermanTarget.get(), scheduler)) {
                world.removeEntity(scheduler, this);
            }
        }
        scheduler.scheduleEvent(this, Action.createActivityAction(this, world, imageStore), this.getActionPeriod());
    }
}
