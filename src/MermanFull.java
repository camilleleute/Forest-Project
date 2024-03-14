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

            if (this.moveTo(world, mermanTarget.get(), scheduler)) {
                world.removeEntity(scheduler, this);
            } else if (world.withinBounds(this.getPosition())){
                Background lake = new Background("lake", imageStore.getImageList("lake"));
                world.setBackgroundCell(this.getPosition(), lake);
            }
        }
        scheduler.scheduleEvent(this, Action.createActivityAction(this, world, imageStore), this.getActionPeriod());
    }
}
