package com.filipkarlsson.egg.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.*;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by filip on 14/02/16.
 */
public class Shell {
    private Sprite sprite;
    private int value;
    private boolean active;
    private Texture texture;
    private Platform platform;


    public Shell(Platform platform, int value) {
        texture = new Texture("g_egg.png");
        this.platform = platform;
        this.value = value;
        this.sprite = new Sprite(texture);
        sprite.setBounds(calculateXpos(), calculateYpos(), texture.getWidth(), texture.getHeight());
        active = true;
    }

    public Rectangle getBounds(){
        return sprite.getBoundingRectangle();
    }
    public Texture getTexture(){
        return texture;
    }
    public Vector2 getPosition(){
        return new Vector2(sprite.getX(), sprite.getY());
    }

    public void update(){
        sprite.setX(calculateXpos());
        sprite.setY(calculateYpos());
    }

    public void draw(SpriteBatch sb){
        if (isActive()) sprite.draw(sb);
    }


    public void dispose(){
        texture.dispose();
    }

    public int getValue() {
        return value;
    }

    public boolean isActive() {
        return active;
    }
    public void deActivate(){
        active = false;
        value = 0;
    }

    private float calculateXpos(){
        return platform.getPosition().x + platform.getBounds().width / 2 - texture.getWidth()/2;
    }

    private float calculateYpos(){
        return platform.getPosition().y + platform.getBounds().height;

    }

}
