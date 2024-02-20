import processing.core.PImage;

import java.util.List;

public class Stump implements Entity  {
    public static final String STUMP_KEY = "stump";
    public static final int STUMP_NUM_PROPERTIES = 0;
    private final String id;
    private Point position;
    private final List<PImage> images;

    public Stump(String id, Point position, List<PImage> images) {
        this.id = id;
        this.position = position;
        this.images = images;
    }





}