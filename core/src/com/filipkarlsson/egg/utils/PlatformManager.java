package com.filipkarlsson.egg.utils;

import com.filipkarlsson.egg.sprites.Platform;

/**
 * Created by filip on 15/02/16.
 */
public class PlatformManager {
    private Platform[] platforms;
    private int pointer;
    private final int SIZE;
    private Platform last;

    public PlatformManager(int size){
        platforms = new Platform[size];
        SIZE = platforms.length;
        pointer = 0;
    }

    public void addPlatform(Platform platform){
        if (platforms[pointer] != null)
            platforms[pointer].dispose();

        platforms[pointer] = platform;
        last = platforms[pointer];
        pointer++;
        if (pointer >= SIZE) pointer = 0;

    }

    public Platform[] getPlatforms(){
        return platforms;
    }

    public Platform getLast() {
        return last;
    }
}


