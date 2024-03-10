import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PathTests {
    @Test
    public void testSingleStep() {
        boolean[][] grid = {
                {true, true, true},
                {true, true, true},
                {true, true, true}
        };
        PathingStrategy strategy = new SingleStepPathingStrategy();
        List<Point> path = strategy.computePath(
                new Point(0, 0),
                new Point(2, 2),
                p -> isWithinBounds(p, grid) && grid[p.y][p.x],
                (p1, p2) -> p1.adjacent(p2),
                PathingStrategy.CARDINAL_NEIGHBORS
        );
        List<Point> expected = List.of(new Point(0, 1));
        assertEquals(expected, path);
    }

    @Test
    public void testSingleStep1() {
        boolean[][] grid = {
                {true, true, true},
                {false, false, true},
                {true, true, true}
        };
        PathingStrategy strategy = new SingleStepPathingStrategy();
        List<Point> path = strategy.computePath(
                new Point(0, 0),
                new Point(2, 2),
                p -> isWithinBounds(p, grid) && grid[p.y][p.x],
                (p1, p2) -> p1.adjacent(p2),
                PathingStrategy.CARDINAL_NEIGHBORS
        );
        List<Point> expected = List.of(new Point(1, 0));
        assertEquals(expected, path);
    }

    private boolean isWithinBounds(Point p, boolean[][] grid) {
        return p.x >= 0 && p.x < grid[0].length &&
                p.y >= 0 && p.y < grid.length;
    }

    @Test
    public void aStarTest() {
        boolean[][] grid = {
                {true, true, true},
                {false, false, true},
                {true, true, true}
        };
        PathingStrategy strategy = new AStarPathingStrategy();
        List<Point> actual = strategy.computePath(
                new Point(0, 0),
                new Point(2, 2),
                p -> isWithinBounds(p, grid) && grid[p.y][p.x],
                (p1, p2) -> p1.adjacent(p2),
                PathingStrategy.CARDINAL_NEIGHBORS
        );

        List<Point> expected = List.of(new Point(1, 0), new Point (2,0), new Point(2, 1));
        assertEquals(expected, actual);
    }

    @Test
    public void aStarTest1() {
        boolean[][] grid = {
                {true, true, true, true},
                {true, false, false, true},
                {false, true, true, true}
        };
        PathingStrategy strategy = new AStarPathingStrategy();
        List<Point> actual = strategy.computePath(
                new Point(0, 0),
                new Point(3, 2),
                p -> isWithinBounds(p, grid) && grid[p.y][p.x],
                (p1, p2) -> p1.adjacent(p2),
                PathingStrategy.CARDINAL_NEIGHBORS
        );

        List<Point> expected = List.of(new Point(1, 0), new Point (2,0),
                new Point(3, 0), new Point(3,1));
        assertEquals(expected, actual);
    }

    @Test
    public void aStarTest2() {
        boolean[][] grid = {
                {true, true, true, true},
                {true, false, false, true},
                {true, true, true, true}
        };
        PathingStrategy strategy = new AStarPathingStrategy();
        List<Point> actual = strategy.computePath(
                new Point(0, 0),
                new Point(3, 2),
                p -> isWithinBounds(p, grid) && grid[p.y][p.x],
                (p1, p2) -> p1.adjacent(p2),
                PathingStrategy.CARDINAL_NEIGHBORS
        );

        List<Point> expected = List.of(new Point(0, 1), new Point (0,2),
                new Point(1, 2), new Point(2,2));
        assertEquals(expected, actual);
    }

    @Test
    public void aStarTest3() {
        boolean[][] grid = {
                {true, true, true},
                {false, false, true},
                {true, true, true}
        };
        PathingStrategy strategy = new AStarPathingStrategy();
        List<Point> actual = strategy.computePath(
                new Point(0, 0),
                new Point(2, 2),
                p -> isWithinBounds(p, grid) && grid[p.y][p.x],
                (p1, p2) -> p1.adjacent(p2),
                PathingStrategy.CARDINAL_NEIGHBORS
        );

        assertEquals(3, actual.size());
    }

    @Test
    public void aStarTest4() {
        boolean[][] grid = {
                {true, false, true, true, true},
                {true, false, true, false, true},
                {true, false, true, false, true},
                {true, true, true, false, true}
        };
        PathingStrategy strategy = new AStarPathingStrategy();
        List<Point> actual = strategy.computePath(
                new Point(0, 0),
                new Point(4, 3),
                p -> isWithinBounds(p, grid) && grid[p.y][p.x],
                (p1, p2) -> p1.adjacent(p2),
                PathingStrategy.CARDINAL_NEIGHBORS
        );
        System.out.print(actual);
        assertEquals(12, actual.size());
    }

    @Test
    public void aStarTest5() {
        boolean[][] grid = {
                {true, false, true, true, true},
                {true, true, true, true, true},
                {true, false, true, false, true},
                {true, true, true, false, true}
        };
        PathingStrategy strategy = new AStarPathingStrategy();
        List<Point> actual = strategy.computePath(
                new Point(0, 0),
                new Point(2, 3),
                p -> isWithinBounds(p, grid) && grid[p.y][p.x],
                (p1, p2) -> p1.adjacent(p2),
                PathingStrategy.CARDINAL_NEIGHBORS
        );
        System.out.print(actual);
        assertEquals(4, actual.size());
    }



}
