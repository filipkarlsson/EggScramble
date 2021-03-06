package com.filipkarlsson.egg.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.utils.Array;
import com.filipkarlsson.egg.EggScramble;
import com.filipkarlsson.egg.sprites.Egg;
import com.filipkarlsson.egg.sprites.Ground;
import com.filipkarlsson.egg.sprites.Platform;
import com.filipkarlsson.egg.sprites.Shell;
import com.filipkarlsson.egg.utils.PlatformManager;


import java.util.ArrayList;
/**
 * Created by filip on 10/02/16.
 */
public class PlayState extends State {
    private HudState hud;

    private static final float CAM_SPEED = 5.0f;
    private static final int CAM_OFFSET = 30;
    private int cam_target;

    private Texture bg;
    private Egg egg;
    private int eggInitHP  = 10;
    private int shellValue;
    private int shellGap;
    private int lastShellScore;
    private static final float EXPECTED_JUMP_DAMAGE = 0.4f; //Lower is more difficult

    private static final float SCORE_FACTOR = 1.0f/42;
    private static final int NR_OF_PLATFORMS = 100;

    private Ground ground;

    private PlatformManager platforms;
    private Array<Shell> shells;

    private float rotation;

    //for debug
    private int debug_score;

    public PlayState(GameStateManager gsm){
        super(gsm);

        cam.setToOrtho(false, EggScramble.WIDTH / 2, EggScramble.HEIGHT / 2);
        bg = new Texture("bg.png");
        egg = new Egg(50, 200, eggInitHP);
        platforms = new PlatformManager(NR_OF_PLATFORMS);
        shells = new Array<Shell>();

        lastShellScore = 0;
        ground = new Ground(0,15, getRightEdgeOfScreen(),5);

        initPlatforms();
        int y = (int) (ground.getPosition().y + ground.getBounds().height);
        platforms.addPlatform(new Platform(y + Platform.MIN_GAP, y + Platform.MAX_GAP, this));
        generatePlatforms();

        hud = new HudState(this);

        updateShellProp(); //must be after egg init


    }

    @Override
    public void handleInput() {
        rotation = Gdx.input.getAccelerometerX();

        //for desktop testing
        if(rotation == 0) {
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
                rotation = 5;
            else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
                rotation = -5;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE))
            debug_score += 50;

        hud.handleInput();
    }

    @Override
    public void update(float dt) {
        handleInput();

        egg.update(dt, this);
        if (egg.getHp() == 0)
            gsm.set(new MenuState(gsm));

        generatePlatforms();
        for (Platform platform : platforms.getPlatforms())
            platform.update(dt);

        for (Shell shell : shells)
            shell.update();

        generateShells();
        updateCamera(dt);
        cam.update();
        hud.update(dt);
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        //sb.draw(bg, 0, getBottomOfScreen());
        sb.draw(ground.getTexture(), ground.getPosition().x, ground.getPosition().y, ground.getBounds().width, ground.getBounds().height);

        for (Platform platform : platforms.getPlatforms())
            platform.draw(sb);

        for (Shell shell : shells)
            shell.draw(sb);

        egg.draw(sb);
        sb.end();


        hud.render(sb);
    }

    private void updateCamera(float dt){

        if(egg.getLastBouncedPlatform() != null && !egg.isOnGround()){
            if (egg.getPosition().y + egg.getBounds().height > egg.getLastBouncedPlatform().getPosition().y){
                cam_target = (int) (egg.getLastBouncedPlatform().getPosition().y + cam.viewportHeight/2 - CAM_OFFSET);

                cam.position.add(0, (float) (cam_target - cam.position.y)*dt*CAM_SPEED, 0);
            }
            else if((egg.getVelocity().y < 0 && egg.getTimeSinceBounce() > 100) || egg.getPosition().y < getBottomOfScreen() + CAM_OFFSET * 4)
                cam.position.add(0, egg.getVelocity().y * dt * (egg.getPosition().y < cam.position.y ? 1.5f : 1.0f), 0);


            if (getBottomOfScreen() < 0) cam.position.y = cam.viewportHeight / 2;
        }
        else cam.position.y = cam.viewportHeight / 2;
    }

    @Override
    public void dispose() {
        bg.dispose();
        egg.dispose();
        //font.dispose();
        ground.dispose();

        for (Platform platform : platforms.getPlatforms())
            platform.dispose();

        for (Shell shell : shells)
            shell.dispose();

        System.out.println("Play state disposed");
    }

    public OrthographicCamera getCam(){
        return cam;
    }


    private void generatePlatforms(){
        if (getLastPlatform().getPosition().y < getTopOfScreen()){
            platforms.addPlatform(new Platform(this));
        }
    }


    private void initPlatforms(){
        for (int i = 0; i<platforms.getPlatforms().length; i++){
            platforms.addPlatform(new Platform(-100, -100, 0));
        }
    }

    private void generateShells(){
       if(getScore() - lastShellScore >= shellGap) {
           shells.add(new Shell(getLastPlatform(), shellValue));

           lastShellScore = getScore();

           updateShellProp();
       }
    }

    public void updateShellProp(){
        int score = getScore();
        if (score < 30){
            shellGap = 10;
            shellValue = 10;
        }

        else if (score < 60){
            shellGap = 15;
            shellValue = 15;
        }

        else if (score < 90){
            shellGap = 21;
            shellValue = 16;
        }
        else{
            shellGap = 35;
            shellValue = 16;
        }

    }

    public int getTopOfScreen(){
        return (int) (cam.position.y + cam.viewportHeight / 2);
    }
    public int getBottomOfScreen(){
        return (int) (cam.position.y - cam.viewportHeight / 2);
    }
    public int getRightEdgeOfScreen(){
        return (int) (cam.position.x + cam.viewportWidth / 2);
    }
    public int getLeftEdgeOfScreen(){
        return (int) (cam.position.x - cam.viewportWidth / 2);
    }

    public float getRotation() {
        return rotation;
    }

    public Platform[] getPlatforms() {
        return platforms.getPlatforms();
    }

    public Platform getLastPlatform(){
        return platforms.getLast();
    }

    public Array<Shell> getShells() {
        return shells;
    }

    public Ground getGround() {
        return ground;
    }

    public Egg getEgg() {
        return egg;
    }

    private int getExpectedHP(){
        float avgGap = (Platform.MAX_GAP-Platform.MIN_GAP)/2;
        return (int)(eggInitHP  + shells.size*shellValue - egg.getMax_y()/(avgGap) * EXPECTED_JUMP_DAMAGE);
    }

    public int getScore(){
        return (int) (egg.getMax_y()*SCORE_FACTOR) + debug_score;
    }
}
