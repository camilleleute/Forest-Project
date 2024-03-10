public class Node {
    private Point position;
    private double g;
    private Node previous;
    private double h;


    public Node(Point position) {
        this.position = position;
        this.g = Double.MAX_VALUE;
        this.previous = null;
        this.h = Double.MAX_VALUE;
    }

    // Getters and Setters
    public Point getPosition() {
        return position;
    }

    public double getG() {
        return g;
    }

    public void setG(double costFromStart) {
        this.g = costFromStart;
    }

    public Node getPrevious() {
        return previous;
    }

    public double getF(){
        return this.getG() + this.getH();
    }

    public void setPrevious(Node previous) {
        this.previous = previous;
    }

    public double getH() {
        return h;
    }

    public void setH(double h) {
        this.h = h;
    }

    // Helper method to calculate the heuristic cost to the goal (Manhattan distance, for example)
    public double manhattanDistance(Point goal) {
        return Math.abs(goal.x - this.position.x) + Math.abs(goal.y - this.position.y);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Node node = (Node) obj;
        return position.equals(node.position);
    }

    @Override
    public int hashCode() {
        return position.hashCode();
    }
}