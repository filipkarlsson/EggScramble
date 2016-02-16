package com.filipkarlsson.egg.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by filip on 14/02/16.
 */
public class Shell {
    private Vector2 position;
    private Rectangle bounds;
    private int value;
    private Texture texture;
    private boolean active;

    public Shell(int x_pos, int y_pos, int value) {
        this.position = new Vector2(x_pos, y_pos);
        this.value = value;
        texture = new Texture("g_egg.png");
        this.bounds = new Rectangle(x_pos, y_pos, texture.getWidth(),texture.getHeight());
        active = true;
    }

    public Rectangle getBounds(){
        return bounds;
    }
    public Texture getTexture(){
        return texture;
    }
    public Vector2 getPosition(){
        return position;
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
        dispose();
    }
}
