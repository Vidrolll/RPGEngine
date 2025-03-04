package com.rpg.main.math;

import com.rpg.main.math.vector.Vector2;

import java.util.Random;

public class VoronoiNoise {
    //Grid for the voronoi noise
    private Vector2[] grid;

    /**
     * Creates a new Voronoi Noise grid with a defined random generation seed.
     * One voronoi vertex is generated randomly in each cell space.
     * @param width (Integer) The number of cells to generate in the X direction.
     * @param height (Integer) The number of cells to generate in the Y direction.
     * @param seed (Integer) The seed used to generate the random positions of the vertices.
     */
    public VoronoiNoise(int width, int height, int seed) {
        grid = new Vector2[width*height];
        Random random = new Random(seed);
        for(int i = 0; i < width; i++) {
            for(int j = 0; j < height; j++) {
                grid[j*width+i] = new Vector2(i+random.nextFloat(),j+ random.nextFloat());
            }
        }
    }

    /**
     * Creates a new Voronoi Noise grid with a random generation seed.
     * One voronoi vertex is generated randomly in each cell space.
     * @param width (Integer) The number of cells to generate in the X direction.
     * @param height (Integer) The number of cells to generate in the Y direction.
     */
    public VoronoiNoise(int width, int height) {
        this(width,height,(int)(Math.random()*1000000000000D));
    }

    /**
     * Grabs a noise value at the inputted x and y coordinates.
     * Noise value ranges from [-1,1], and is equal to the distance
     * from the nearest cell.
     * If the inputted value is outside the grid the returned value could potentially
     * be greater than 1 or less than -1.
     * @param x (Float) The x coordinate to grab a noise value from.
     * @param y (Float) The y coordinate to grab a noise value from.
     * @return (Float) The noise value at the given coordinate.
     */
    public float noise(float x, float y) {
        Vector2 point = new Vector2(x,y);
        float value = Float.MAX_VALUE;
        for (Vector2 vector : grid) {
            if (vector.dist(point) < value)
                value = vector.dist(point);
        }
        return (float)(value/Math.sqrt(2));
    }
}
