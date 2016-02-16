package com.filipkarlsson.egg.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.filipkarlsson.egg.states.PlayState;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by filip on 12/02/16.
 */
public class Platform {
    public static int MAX_WIDTH  = 70;
    public static int MIN_WIDTH  = 30;
    public static int MAX_GAP  = 130;
    public static int MIN_GAP  = 40;

    public static int HEIGHT = 15;


    private Vector2 position;
    private Rectangle bounds;
    private static Texture texture = new Texture("platform_egg.png");

    public Platform (int x, int y, int width){
        position = new Vector2(x, y);
        bounds = new Rectangle(x, y, width, HEIGHT);
    }

    public Platform (int minY, int maxY, PlayState game){
        int w = ThreadLocalRandom.current().nextInt(MIN_WIDTH, MAX_WIDTH +1);
        int y = ThreadLocalRandom.current().nextInt(minY, maxY +1);
        int x = ThreadLocalRandom.current().nextInt(0, (int) (game.getRightEdgeOfScreen()  - w));
        position = new Vector2(x, y);
        bounds = new Rectangle(x, y, w, HEIGHT);
    }

    public Platform (PlayState game){
        int last_Y  = (int) game.getLastPlatform().getPosition().y;
        int w = ThreadLocalRandom.current().nextInt(MIN_WIDTH, MAX_WIDTH +1);
        int y = ThreadLocalRandom.current().nextInt(last_Y + MIN_GAP, last_Y + MAX_GAP + 1);
        int x = ThreadLocalRandom.current().nextInt(0, (int) (game.getRightEdgeOfScreen()  - w));

        position = new Vector2(x, y);
        bounds = new Rectangle(x, y, w, HEIGHT);
    }

    public void update(float dt){

    }

    public void bounce (){

    }

    public Rectangle getBounds () {
        return bounds;
    }

    public Texture getTexture (){
        return texture;
    }

    public Vector2 getPosition () {
        return position;
    }

    public void dispose (){
    }

}
