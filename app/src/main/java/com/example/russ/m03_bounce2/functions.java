package com.example.russ.m03_bounce2;

import java.util.Random;

public class functions {
    /**
     * Generate a random color (only rgb, no alpha)
     * @return 32bit color (int)
     */
    public static int randomColor(){
        Random r = new Random();

        int red = r.nextInt(256);
        int green = r.nextInt(256);
        int blue = r.nextInt(256);

        return color(red, green, blue, 255);
    }

    /**
     * Create a new color with alpha values (same as Color)
     * @param red 0-255 red value
     * @param green 0-255 green value
     * @param blue 0-255 blue value
     * @param alpha 0-255 alpha value
     * @return 32bit sequence where each color is 8 bits (0-255)
     */
    public static int color(int red, int green, int blue, int alpha){
        return alpha << 24 | red << 16 | green << 8 | blue;
    }
}
