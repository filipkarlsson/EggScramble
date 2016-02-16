package com.filipkarlsson.egg.sprites;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.filipkarlsson.egg.EggScramble;
import com.filipkarlsson.egg.states.PlayState;

import java.util.List;


/**
 * Created by filip on 11/02/16.
 */
public class Egg {
    private static final int GRAVITY = -15;
    private static final int X_SPEED_FACTOR = 50;
    private static final float JUMP_SPEED  = 500.0f;
    private static final int MAX_SPEED  = (int)JUMP_SPEED * 3;
    private static final int BOTTOM_BOUNDS_HEIGHT = 5;

    private Vector3 position;
    private boolean onGround;



    private Vector3 velocity;
    private Rectangle bounds;
    private Rectangle bottomBounds;
    private Texture[] textures;

    private double hp;
    private Platform lastBouncedPlatform;

    public static final int MAX_HP = 20;

    private int max_y;


    public Egg (int x, int y){
        position = new Vector3(x, y, 0);
        velocity = new Vector3(0, 0, 0);
        textures = new Texture[2];
        textures[0] = new Texture("egg1.png");
        textures[1] = new Texture("egg_cr.png");
        bounds = new Rectangle(x, y, textures[0].getWidth(), textures[0].getHeight());
        bottomBounds = new Rectangle(x,y, textures[0].getWidth(), BOTTOM_BOUNDS_HEIGHT);
        hp = 15;
    }

   // public void update(float dt, float rotation, OrthographicCamera cam, List<Platform> platforms){
   public void update(float dt, PlayState game){
        if (velocity.y > MAX_SPEED)
            velocity.y = MAX_SPEED;

        if(position.y <= 0){
            position.y = 0;
            bounce();
        }
        else if(checkCollision(game))
            bounce();
        else
            velocity.add(0, GRAVITY, 0);

        velocity.x = -game.getRotation()*X_SPEED_FACTOR;


        velocity.scl(dt);
        position.add(velocity.x, velocity.y, 0);


        if(position.x + bounds.width < 0)
            position.x = game.getRightEdgeOfScreen();
        else if (position.x > game.getRightEdgeOfScreen())
            position.x = - bounds.width;

        velocity.scl(1 / dt);

        bounds.setPosition(position.x, position.y);
        bottomBounds.setPosition(position.x, position.y);

        checkShellCollision(game.getShells());
    }

    public void bounce (){
        hp = hp + (velocity.y)/(JUMP_SPEED/2.0);
        if (hp<0) hp = 0;
        else if (hp>MAX_HP) hp = MAX_HP;

        velocity.y = JUMP_SPEED;

        if (position.y>max_y) max_y = (int)position.y;
    }


    private boolean checkCollision(PlayState game){
        boolean collision = false;

        for (Platform platform : game.getPlatforms()){
            if(bottomBounds.overlaps(platform.getBounds()) && velocity.y < 0){
                collision = true;
                lastBouncedPlatform = platform;
                onGround = false;
            }
        }

        if (bottomBounds.overlaps(game.getGround().getBounds())){
            collision = true;
            onGround = true;
        }

        return collision;
    }

    private void checkShellCollision(List<Shell> shells){

        for (Shell shell : shells){
            if (bounds.overlaps(shell.getBounds()) && shell.isActive()){
                hp +=  shell.getValue();
                shell.deActivate();
            }

        }

    }



    public Rectangle getBounds () {
        return bounds;
    }

    public Texture getTexture (){

        return hp>5 ? textures[0] : textures[1];
    }

    public Vector3 getPosition () {
        return position;
    }
    public Vector3 getVelocity() {
        return velocity;
    }

    public void dispose (){
        textures[0].dispose();
        textures[1].dispose();
    }


    public int getTimeSinceBounce(){
        return (int) ((velocity.y - JUMP_SPEED)  / GRAVITY);
    }

    public double getHp() {
        return hp;
    }

    public Platform getLastBouncedPlatform() {
        return lastBouncedPlatform;
    }

    public int getMax_y() {
        return max_y;
    }

    public boolean isOnGround() {
        return onGround;
    }
}
