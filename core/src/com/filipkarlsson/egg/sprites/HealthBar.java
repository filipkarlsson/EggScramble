package com.filipkarlsson.egg.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.filipkarlsson.egg.states.PlayState;

/**
 * Created by filip on 15/02/16.
 */
public class HealthBar {
    public static int HEIGHT = 15;
    private Rectangle bounds;
    private PlayState game;

    private static Texture texture;

    public HealthBar (PlayState game){
        bounds = new Rectangle(0, game.getCam().viewportHeight - HEIGHT, (float)game.getEgg().getHp()*game.getRightEdgeOfScreen()/Egg.MAX_HP, HEIGHT);
        texture = new Texture("pan.png");
        this.game = game;
    }

    public void update(){
        bounds.width = (float)game.getEgg().getHp()*game.getRightEdgeOfScreen()/Egg.MAX_HP;
    }


    public static Texture getTexture() {
        return texture;
    }

    public Rectangle getBounds() {
        return bounds;
    }


    public float[] getColor(){
        float[] color = new float[3];
        color[0] = game.getEgg().getHp() < 10 ? 0.8f : 0.0f;
        color[1] = game.getEgg().getHp() > 5 ? 0.8f : 0.0f;
        color[2] = 0;

        return color;
    }
}
