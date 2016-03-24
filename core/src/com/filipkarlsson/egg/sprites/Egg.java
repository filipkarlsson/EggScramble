package com.filipkarlsson.egg.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.filipkarlsson.egg.states.PlayState;


/**
 * Created by filip on 11/02/16.
 */
public class Egg {
    private static final int GRAVITY = -15;
    private static final int X_SPEED_FACTOR = 70;
    private static final float JUMP_SPEED  = 500.0f;
    private static final int MAX_SPEED  = (int)JUMP_SPEED * 5;
    private static final int BOTTOM_BOUNDS_HEIGHT = 5;
    private static final int TEXTURE_W = 554;
    private static final int TEXTURE_H = 755;

    private static final int HEIGHT = 40;
    private static final int WIDTH = 30;

    private Vector3 position;
    private boolean onGround;


    private int frameCount;

    private Vector3 velocity;
    private Rectangle bottomBounds;
    private Texture textures;
    private TextureRegion[] frames;
    private Sprite sprite;

    private Sound bounceSound;
    private Sound shellSound;
    private double hp;
    private Platform lastBouncedPlatform;

    private int max_y;

    public Egg (int x, int y, int hp){
        position = new Vector3(x, y, 0);
        velocity = new Vector3(0, 0, 0);
        textures = new Texture("egg_animation_texture.png");

        frames = new TextureRegion[5];
        frames[0] = new TextureRegion(textures, 0, 0, TEXTURE_W, TEXTURE_H);
        frames[1] = new TextureRegion(textures, TEXTURE_W  * 1, 0, TEXTURE_W, TEXTURE_H);
        frames[2] = new TextureRegion(textures, TEXTURE_W  * 2, 0, TEXTURE_W, TEXTURE_H);
        frames[3] = new TextureRegion(textures, TEXTURE_W  * 3, 0, TEXTURE_W, TEXTURE_H);
        frames[4] = new TextureRegion(textures, TEXTURE_W  * 4, 0, TEXTURE_W, TEXTURE_H);

        frameCount = 0;

        sprite = new Sprite();
        sprite.setRegion(frames[frameCount]);
        sprite.setBounds(position.x, position.y, WIDTH, HEIGHT);
        bottomBounds = new Rectangle(x,y, sprite.getWidth(), BOTTOM_BOUNDS_HEIGHT);
        this.hp = hp;

        bounceSound = Gdx.audio.newSound(Gdx.files.internal("bounce.wav"));
        shellSound = Gdx.audio.newSound(Gdx.files.internal("shell.wav"));
    }

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

       if(position.x + sprite.getWidth() < 0)
           position.x = game.getRightEdgeOfScreen();
       else if (position.x > game.getRightEdgeOfScreen())
           position.x = - sprite.getWidth();

       velocity.scl(1 / dt);

       sprite.setPosition(position.x, position.y);
       bottomBounds.setPosition(position.x, position.y);

       sprite.setOriginCenter();

       checkShellCollision(game.getShells());

       if (hp < 3)
          frameCount = 4;
       else if (hp < 5)
           frameCount = 3;
       else if (hp < 7)
           frameCount = 2;
       else if (hp < 9)
           frameCount = 1;
       else
           frameCount = 0;


       if (frameCount == 4)
           sprite.setRotation(game.getRotation() * 1.5f + (float)Math.sin(getTimeSinceBounce() * 2)*10);
       else if (frameCount == 3)
           sprite.setRotation(game.getRotation() * 1.5f + (float)Math.sin(getTimeSinceBounce() * 2)*3);
       else
           sprite.setRotation(game.getRotation() * 1.5f);
   }

    public void bounce (){
        bounceSound.play();

        hp = hp + (velocity.y)/(JUMP_SPEED/2.0);
        if (hp<0) hp = 0;

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

    private void checkShellCollision(Array<Shell> shells){
        for (Shell shell : shells){
            if (sprite.getBoundingRectangle().overlaps(shell.getBounds()) && shell.isActive()){
                shellSound.play();
                hp =  shell.getValue();
                shell.deActivate();
            }
        }
    }

    public void draw(SpriteBatch sb){
        sprite.setRegion(frames[frameCount]);
        sprite.draw(sb);
    }


    public Rectangle getBounds () {
        return sprite.getBoundingRectangle();
    }


    public Vector3 getPosition () {
        return position;
    }
    public Vector3 getVelocity() {
        return velocity;
    }

    public void dispose (){
        bounceSound.dispose();
        shellSound.dispose();
        textures.dispose();
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
