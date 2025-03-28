package com.rpg.main.math.noise;

import com.rpg.main.math.vector.Vector2;

import java.util.Random;

public class PerlinNoise {
    //Variables for perlin generation.
    Vector2[] permutations;
    int width;
    int height;

    /**
     * Creates a new Perlin Noise permutation grid with a set dimension size.
     * @param width (Integer) The width of the grid to generate.
     * @param height (Integer) The height of the grid to generate.
     */
    public PerlinNoise(int width, int height) {
        permutations = new Vector2[width*height];
        Random r = new Random();
        for(int i = 0; i < permutations.length; i++) {
            float angle = r.nextFloat((float)(2*Math.PI));
            permutations[i] = new Vector2((float)Math.cos(angle),(float)Math.sin(angle));
        }
        this.width = width;
        this.height = height;
    }

    /**
     * Returns a calculated perlin noise value at the inputted index.
     * Note that as each vector used to calculate perlin noise is at each integer position,
     * inputted values should be a decimal value (Ex: 3.25,5.67). Any solid integer positions
     * (Ex: 4,6) will return zero.
     * @param x (Float) The x coordinate to calculate.
     * @param y (Float) The y coordinate to calculate.
     * @return (Float) The perlin value at the inputted index.
     */
    public float noise(float x, float y) {
        int x1 = (int)Math.floor(x);
        int y1 = (int)Math.floor(y);
        int x2 = (x1+1);
        int y2 = (y1+1);
        float d1 = new Vector2(x-x1,y-y1).dot(permutations[(y1%height)*width+(x1%width)]);
        float d2 = new Vector2(x-x2,y-y1).dot(permutations[(y1%height)*width+(x2%width)]);
        float d3 = new Vector2(x-x1,y-y2).dot(permutations[(y2%height)*width+(x1%width)]);
        float d4 = new Vector2(x-x2,y-y2).dot(permutations[(y2%height)*width+(x2%width)]);
        float u = fade(x-x1), v = fade(y-y1);
        return lerp(u,lerp(v,d1,d3),lerp(v,d2,d4))*(float)(2/Math.sqrt(2));
    }

    //idek what these methods do ngl I just looked them up.
    public float fade(float t) {
        return ((6*t - 15)*t + 10)*t*t*t;
    }

    public float lerp(float t, float a1, float a2) {
        return a1 + t*(a2 - a1);
    }
}
