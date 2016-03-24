package com.filipkarlsson.egg.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by filip on 14/02/16.
 */
public class Ground {
    private Vector2 position;
    private Rectangle bounds;
    private Texture texture;

    public Ground(int x, int y, int width, int height){
        position  = new Vector2(x, y);
        texture = new Texture("ground.png");
        bounds = new Rectangle(x, y, width, height);

    }

    public Rectangle getBounds() {
        return bounds;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Texture getTexture() {
        return texture;
    }
    public void dispose(){
        texture.dispose();
    }
}
