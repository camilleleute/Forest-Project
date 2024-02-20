

import processing.core.PImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DudeFull extends Dude{

    public DudeFull(String id, Point position, double actionPeriod, double animationPeriod, int resourceLimit, int resourceCount,
                List<PImage> images) {
        super(id, position, actionPeriod, animationPeriod, resourceLimit, resourceCount, images); }

    public void executeDudeFullActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> fullTarget = world.findNearest(this.position, new ArrayList<>(List.of(House.class)));

        if (fullTarget.isPresent() && this.moveToFull(world, fullTarget.get(), scheduler)) {
            this.transformFull(world, scheduler, imageStore);
        } else {
            scheduler.scheduleEvent(this, Action.createActivityAction(this, world, imageStore), this.actionPeriod);
        }
    }
    public void transformFull(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        Entity dude = Entity.createDudeNotFull(this.id, this.position, this.actionPeriod, this.animationPeriod, this.resourceLimit, this.resourceCount, this.images);

        world.removeEntity(scheduler, this);

        world.addEntity(dude);
        dude.scheduleActions(scheduler, world, imageStore);
    }

    public boolean moveToFull(WorldModel world, Entity target, EventScheduler scheduler) {
        if (this.position.adjacent(target.getPosition())) {
            return true;
        } else {
            Point nextPos = this.nextPositionDude(world, target.getPosition());

            if (!this.position.equals(nextPos)) {
                world.moveEntity(scheduler, this, nextPos);
            }
            return false;
        }
    }
}
