package com.filipkarlsson.egg.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.filipkarlsson.egg.EggScramble;

/**
 * Created by filip on 19/02/16.
 */
public class HudState extends State {
    //private HealthBar healthBar;
    //private ShapeRenderer shapeRenderer;
    BitmapFont font = new BitmapFont(Gdx.files.internal("font.fnt"));
    private PlayState game;

    public HudState(PlayState game){
        cam.setToOrtho(false, EggScramble.WIDTH / 2, EggScramble.HEIGHT / 2);
        this.game = game;
        //healthBar = new HealthBar(game);

    }

    @Override
    protected void handleInput() {

    }

    @Override
    public void update(float dt) {
        cam.update();

    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        font.setColor(0.0f, 0.0f, 0.0f, 1.0f);
        font.draw(sb, "Score: " + game.getScore(), 10, cam.viewportHeight - 5);

        sb.end();

    }

    @Override
    public void dispose() {

    }
}
