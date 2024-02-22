import processing.core.PImage;

import java.util.List;

public class Stump implements Entity  {
    public static final String STUMP_KEY = "stump";
    public static final int STUMP_NUM_PROPERTIES = 0;
    private final String id;
    private Point position;
    private final List<PImage> images;
    private int imageIndex;

    public Stump(String id, Point position, List<PImage> images) {
        this.id = id;
        this.position = position;
        this.images = images;
    }

    public String getId() {
        return id;
    }
    public Point getPosition() {
        return position;
    }

    public int getImageIndex() { return imageIndex;}
    public void setPosition(Point position) {
        this.position = position;
    }
    public PImage getCurrentImage(){
        return this.images.get(this.imageIndex % this.images.size());
    }
    public void nextImage() {
        this.imageIndex = this.imageIndex + 1;
    }
}