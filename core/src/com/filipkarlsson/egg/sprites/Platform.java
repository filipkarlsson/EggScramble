package com.filipkarlsson.egg.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.filipkarlsson.egg.states.PlayState;
import com.filipkarlsson.egg.utils.MathUtils;

import java.util.concurrent.ThreadLocalRandom;

import javafx.geometry.Pos;

/**
 * Created by filip on 12/02/16.
 */
public class Platform {
    public static int MAX_WIDTH  = 70;
    public static int MIN_WIDTH  = 30;
    public static int MAX_GAP  = 130;
    public static int MIN_GAP  = 40;

    public static int HEIGHT = 15;


    //private Vector3 position;
    //private Vector3 centerPos;
    private Position position;
    private static Texture texture = new Texture("platform_egg.png");
    private Sprite sprite;

    public Platform (int x, int y, int width){
        sprite = new Sprite(texture);
        sprite.setBounds(x, y, width, HEIGHT);
        position = new Position(new Vector3(x, y, 0));
    }

    public Platform (int minY, int maxY, PlayState game){
        int w = ThreadLocalRandom.current().nextInt(MIN_WIDTH, MAX_WIDTH +1);
        int y = ThreadLocalRandom.current().nextInt(minY, maxY +1);
        int x = ThreadLocalRandom.current().nextInt(0, (int) (game.getRightEdgeOfScreen()  - w));
        position = new Position(new Vector3(x, y, 0));
        sprite = new Sprite(texture);
        sprite.setBounds(x, y, w, HEIGHT);

    }

    public Platform (PlayState game){
        int last_Y  = (int) game.getLastPlatform().getPosition().y;
        int w = ThreadLocalRandom.current().nextInt(MIN_WIDTH, MAX_WIDTH +1);
        int y = ThreadLocalRandom.current().nextInt(last_Y + MIN_GAP, last_Y + MAX_GAP + 1);
        int x = ThreadLocalRandom.current().nextInt(0, (int) (game.getRightEdgeOfScreen()  - w));

        sprite = new Sprite(texture);
        sprite.setBounds(x, y, w, HEIGHT);

        if (game.getScore() < 100)
            position = new Position(new Vector3(x, y, 0));
        else if (game.getScore() < 300){
            if(ThreadLocalRandom.current().nextInt(0, 10) > 7)
                position = new Position(new Vector3(x, y, 0), 0, game.getRightEdgeOfScreen()-w, Position.SLOW);
            else
                position = new Position(new Vector3(x, y, 0));
        }
        else{
            if(ThreadLocalRandom.current().nextInt(0, 10) > 5){
                if (ThreadLocalRandom.current().nextBoolean())
                    position = new Position(new Vector3(x, y, 0), 0, game.getRightEdgeOfScreen()-w, Position.SLOW);
                else
                    position = new Position(new Vector3(x, y, 0), 0, game.getRightEdgeOfScreen()-w, Position.FAST);
            }

            else
                position = new Position(new Vector3(x, y, 0));
        }



    }

    public void draw(SpriteBatch sb){
        sprite.draw(sb);
    }

    public void update(float dt){
        position.update(dt);
        sprite.setPosition(position.getPosition().x, position.getPosition().y);
    }

    public void bounce (){

    }

    public Rectangle getBounds () {
        return sprite.getBoundingRectangle();
    }

    public Texture getTexture (){
        return sprite.getTexture();
    }

    public Vector3 getPosition () {
        return position.getPosition();
    }

    public void dispose (){
    }




    private class Position{
        private Vector3 position;
        private boolean moving;
        private int leftLimit, rightLimit;
        private float speed;

        public static final float FAST = 100.0f, SLOW = 50.0f;

        Position(Vector3 position){
            moving = false;
            this.position = position;
        }

        Position(Vector3 position, int leftLimit, int rightLimit, float speed){
            this.position = position;
            this.leftLimit = leftLimit;
            this.rightLimit = rightLimit;
            this.speed = ThreadLocalRandom.current().nextBoolean() ? speed : -speed;
            moving = true;
        }

        public void update(float dt){
            if (moving){
                if (position.x >= rightLimit || position.x <= leftLimit){
                    speed = -speed;
                }
                position.x += speed*dt;
            }
        }


        public Vector3 getPosition() {
            return position;
        }
    }

}
