import processing.core.PImage;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Merman implements Entity, ExecuteActivity, NextPosition, ScheduleActions{
    private final String id;
    private Point position;
    private final List<PImage> images;

    private int saplingCount;

    private int imageIndex;
    private Optional<Point> target = Optional.empty();


    public Merman(String id, Point position, List<PImage> images) {
        this.id = id;
        this.position = position;
        this.images = images;
    }
    public Optional<Point> getTarget(){return this.target;}
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

    public double getAnimationPeriod() {
        return 0.1;
    }
    public double getActionPeriod(){
        return 0.5;
    }
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {

        Random rand = new Random();
        int randCols = rand.nextInt(world.numCols);
        int randRows = rand.nextInt(world.numRows);
        Point randomPoint = new Point(randCols, randRows);

        while (!world.withinBounds(randomPoint) || world.isOccupied(randomPoint)){
            randCols = rand.nextInt(world.numCols);
            randRows = rand.nextInt(world.numRows);
            randomPoint = new Point(randCols, randRows);
        }

        if(this.transformMerman(world, scheduler, imageStore)){
            return;
        }

        if(this.target.isPresent()) {

                if (!this.moveTo(world, target.get(), scheduler)) {
                    Background lake = new Background("lake", imageStore.getImageList("lake"));
                    world.setBackgroundCell(this.position, lake);

                    scheduler.scheduleEvent(this, Action.createActivityAction(this, world, imageStore), this.getActionPeriod());
                } else if (!this.transformMerman(world, scheduler, imageStore)){
                    Function<Point, Stream<Point>> generateNeighbors = point ->
                            Stream.<Point>builder()
                                    .add(new Point(point.x, point.y - 1))
                                    .add(new Point(point.x, point.y + 1))
                                    .add(new Point(point.x - 1, point.y))
                                    .add(new Point(point.x + 1, point.y))
                                    .build();

                    // Apply the function to a specific point to get its neighbors
                    Point tgtPos = this.position;
                    List<Point> neighbors = generateNeighbors.apply(tgtPos).filter(p->world.withinBounds(p)).filter(p->!world.isOccupied(p))
                            .toList(); // Collect the stream to a list

                    if (!neighbors.isEmpty()) {
                        if (world.withinBounds(neighbors.getFirst()) && !world.isOccupied(neighbors.getFirst())) {
                            Entity sapling = Entity.createSapling(Sapling.SAPLING_KEY + "_" + "MermanFull", neighbors.getFirst(), imageStore.getImageList(Sapling.SAPLING_KEY), 0);

                            world.addEntity(sapling);
                            ((ScheduleActions)sapling).scheduleActions(scheduler, world, imageStore);
                        }
                    }
                    this.target = Optional.empty();
                    this.saplingCount++;
                    scheduler.scheduleEvent(this, Action.createActivityAction(this, world, imageStore), this.getActionPeriod());

                }
        } else {
            if (world.withinBounds(randomPoint) && !world.isOccupied(randomPoint)) {
                scheduler.scheduleEvent(this, Action.createActivityAction(this, world, imageStore), this.getActionPeriod());
                this.target = Optional.of(randomPoint);
            }
        }
    }

    public boolean transformMerman(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        if (this.saplingCount >= 3) {
            MermanFull mermanFull = new MermanFull(this.id, this.getPosition(), this.images);

            world.removeEntity(scheduler, this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(mermanFull);
            mermanFull.scheduleActions(scheduler, world, imageStore);

            return true;
        }

        return false;
    }
    public Point nextPosition(WorldModel world, Point destPos) {
        Point start = getPosition();
        List<Point> newPos = new AStarPathingStrategy().computePath(start, destPos,
                p -> world.withinBounds(p) && !world.isOccupied(p),
                (p1, p2) -> p1.adjacent(p2),
                PathingStrategy.CARDINAL_NEIGHBORS);
        if (newPos.isEmpty()){
            return start;
        }
        return newPos.get(0);
    }

    @Override
    public boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler) {
        if (this.position.adjacent(target.getPosition())) {
            return true;

        } else {

            Point nextPos = this.nextPosition(world, target.getPosition());
            if (!this.getPosition().equals(nextPos)) {
                world.moveEntity(scheduler, this, nextPos);
            }
            return false;

        }
    }

    public boolean moveTo(WorldModel world, Point target, EventScheduler scheduler) {
        if (this.position.adjacent(target)) {
            return true;
        } else {

            Point nextPos = this.nextPosition(world, target);
            if (!this.position.equals(nextPos)) {
                world.moveEntity(scheduler, this, nextPos);
            }
            return false;

        }
    }
    @Override
    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this, Action.createActivityAction(this, world, imageStore), getActionPeriod());
        scheduler.scheduleEvent(this, Action.createAnimationAction(this, 0), getAnimationPeriod());
    }

}
