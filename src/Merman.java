import processing.core.PImage;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Merman implements Entity, ExecuteActivity, NextPosition, ScheduleActions{
    private final String id;
    private Point position;
    private final List<PImage> images;
//    private final double actionPeriod;
//    private final double animationPeriod;

    private int saplingCount;

    private int imageIndex;
    private double actionPeriod;
//    public Point target;
    private Optional<Point> target = Optional.empty();


    public Merman(String id, Point position, List<PImage> images) {
        this.id = id;
        this.position = position;
        this.images = images;
//        this.actionPeriod = actionPeriod;
//        this.animationPeriod = animationPeriod;
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

//    public double getActionPeriod() {
//        return actionPeriod;
//    }
    public double getAnimationPeriod() {
        return 0.1;
    }
    public double getActionPeriod(){return 0.5;}
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
//
        Point mermanTarget = randomPoint;


//        Optional<Entity> mermanTarget = world.findNearest(this.position, new ArrayList<>(List.of(Obstacle.class)));

//        if(mermanTarget.isPresent())
//        {
//            Point tgtPos = mermanTarget.get().getPosition();
//
//            if(this.moveTo(world, mermanTarget.get(), scheduler))
//            {
//                transformMerman(world, scheduler, imageStore, tgtPos, mermanTarget.get());
//            }
//        }
//        scheduler.scheduleEvent(this, Action.createActivityAction(this, world, imageStore), this.getActionPeriod());
        if(this.transformMerman(world, scheduler, imageStore)){
            return;
        }

        if(this.target.isPresent()) {
            if (world.withinBounds(mermanTarget) && !world.isOccupied(mermanTarget)) {
//            if (!this.moveTo(world, this.target.get(), scheduler) || !this.transformMerman(world, scheduler, imageStore)) {
//                scheduler.scheduleEvent(this, new Activity(this, world, imageStore), this.getActionPeriod());
//            }

                if (!this.moveTo(world, target.get(), scheduler)) {
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
                            .collect(Collectors.toList()); // Collect the stream to a list

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

//            else if(this.moveTo(world, tgtPos, scheduler))
//            {
//                System.out.println("RandPoint reached");
//                transformMerman(world, scheduler, imageStore);
//            }
            }
        } else {
            if (world.withinBounds(mermanTarget) && !world.isOccupied(mermanTarget)) {
                scheduler.scheduleEvent(this, Action.createActivityAction(this, world, imageStore), this.getActionPeriod());
                Point tgtPos = mermanTarget;
                this.target = Optional.of(tgtPos);
            }
        }


//
//        Optional<Entity> mermanTarget = world.findNearest(this.position, new ArrayList<>(Arrays.asList(Obstacle.class)));
//
//        if(mermanTarget.isPresent()){
//
//            if (this.moveTo(world, mermanTarget.get(), scheduler)){
//
//                Entity sapling = Entity.createSapling(Sapling.SAPLING_KEY + "_" + "mysapling", mermanTarget.get().getPosition(), imageStore.getImageList(Sapling.SAPLING_KEY), 0);
//                world.addEntity(sapling);
////                world.removeEntity(scheduler, this);
//                this.saplingCount++;
//                ((ScheduleActions)sapling).scheduleActions(scheduler, world, imageStore);
//            }
//
//            if(this.saplingCount >= 3) {
//                Entity endTarget = world.findNearest(this.position, new ArrayList<>(Arrays.asList(Obstacle.class))).get();
//
//                if (this.moveTo(world, endTarget, scheduler)) {
//                    world.removeEntity(scheduler, this);
//                }
//            }
//        }
//        if (this.moveTo(world, mermanTarget, scheduler) || !transformMerman(world, scheduler, imageStore)){
//        scheduler.scheduleEvent(this, Action.createActivityAction(this, world, imageStore), this.actionPeriod);
    }

//    public Boolean transformMerman(WorldModel world, EventScheduler scheduler, ImageStore imageStore)
//    {
//
//        world.removeEntity(scheduler, this);
//        return true;
//    }

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

//    public Boolean transformMerman(WorldModel world, EventScheduler scheduler, ImageStore imageStore){
//        if (this.saplingCount >= 3){
//            return true;
//        }
//
//
//
//    }


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
//            Point nextPos = this.nextPosition(world, target.getPosition());
//
//            if (!this.position.equals(nextPos)) {
//                world.moveEntity(scheduler, this, nextPos);
//            }
//            return false;

            Point nextPos = this.nextPosition(world, target.getPosition());
            if (!this.getPosition().equals(nextPos)) {
                world.moveEntity(scheduler, this, nextPos);
            }
            return false;
        }
    }

    // kills fairies
    public boolean moveTo(WorldModel world, Point target, EventScheduler scheduler) {
        if (this.position.adjacent(target)) {

//            Sapling sapling = new Sapling(this.id, validPoints, )

//            world.addEntity(sapling);
//            sapling.scheduleActions(scheduler, world, imageStore);
            return true;
        } else {
            Point nextPos = this.nextPosition(world, target);

            if (!this.position.equals(nextPos)) {
                world.moveEntity(scheduler, this, nextPos);
            }
            return false;
        }
    }
//    public boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler) {
//        if (this.position.adjacent(target.getPosition())) {
//            world.removeEntity(scheduler, target);
//            return true;
//        } else {
//            Point nextPos = this.nextPosition(world, target.getPosition());
//
//            if (!this.position.equals(nextPos)) {
//                world.moveEntity(scheduler, this, nextPos);
//            }
//            return false;
//        }
//    }

    @Override
    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this, Action.createActivityAction(this, world, imageStore), getActionPeriod());
        scheduler.scheduleEvent(this, Action.createAnimationAction(this, 0), getAnimationPeriod());
    }

}
