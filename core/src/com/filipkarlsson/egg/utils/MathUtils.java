package com.filipkarlsson.egg.utils;

import java.util.Random;

/**
 * Created by filip on 13/02/16.
 */
public class MathUtils {
    private static Random random;

    public static int RandomUtil(int min , int max){
        return random.nextInt(max+1) + min;
    }
}
