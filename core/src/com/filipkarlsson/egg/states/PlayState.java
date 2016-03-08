package com.filipkarlsson.egg.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.*;


import com.filipkarlsson.egg.EggScramble;
import com.filipkarlsson.egg.sprites.Egg;
import com.filipkarlsson.egg.sprites.Ground;
import com.filipkarlsson.egg.sprites.HealthBar;
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
    private Ground ground;
    //private HealthBar healthBar;
    //private ShapeRenderer shapeRenderer;

    private PlatformManager platforms;
    private ArrayList<Shell> shells;


   // BitmapFont font = new BitmapFont();

    private float rotation;



    public PlayState(GameStateManager gsm){
        super(gsm);


        cam.setToOrtho(false, EggScramble.WIDTH / 2, EggScramble.HEIGHT / 2);
        bg = new Texture("bg.png");
        egg = new Egg(50, 200);
        platforms = new PlatformManager(Egg.MAX_HP * 2);
        shells = new ArrayList<Shell>();
        ground = new Ground(0,0, getRightEdgeOfScreen(),100);

        initPlatforms();
        int y = (int) (ground.getPosition().y + ground.getBounds().height);
        platforms.addPlatform(new Platform(y + Platform.MIN_GAP, y + Platform.MAX_GAP, this));
        generatePlatforms();

       // healthBar = new HealthBar(this);
       // shapeRenderer = new ShapeRenderer();
       // shapeRenderer.setProjectionMatrix(cam.combined);
        hud = new HudState(this);
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

        hud.handleInput();

    }

    @Override
    public void update(float dt) {
        handleInput();

        egg.update(dt, this);
        if (egg.getHp() == 0)
            gsm.set(new MenuState(gsm));



        generatePlatforms();
        generateShells();
        //healthBar.update();

        updateCamera(dt);


        cam.update();

        hud.update(dt);

    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        //sb.draw(bg, 0, getBottomOfScreen());
        sb.draw(ground.getTexture(),ground.getPosition().x, ground.getPosition().y, ground.getBounds().width, ground.getBounds().height);

        for (Platform platform : platforms.getPlatforms())
            sb.draw(platform.getTexture(), platform.getPosition().x, platform.getPosition().y, platform.getBounds().width, platform.getBounds().height);

        for (int i = 0; i<shells.size(); i++){
            if (shells.get(i).isActive()){
                sb.draw(shells.get(i).getTexture(), shells.get(i).getPosition().x,
                        shells.get(i).getPosition().y,shells.get(i).getBounds().width, shells.get(i).getBounds().height);
            }
        }

        egg.draw(sb);

        //sb.draw(egg.getTexture(), egg.getPosition().x, egg.getPosition().y);
        //font.setColor(0.0f, 0.0f, 0.0f, 1.0f);
        //font.draw(sb, egg.getMax_y() + "", 0, getTopOfScreen() - 50);

        sb.end();

        //shapeRenderer.begin(ShapeType.Filled);
        //float[] c = healthBar.getColor();
        //shapeRenderer.setColor(c[0], c[1], c[2], 1.0f);
        //shapeRenderer.rect(healthBar.getBounds().x, healthBar.getBounds().y, healthBar.getBounds().width, healthBar.getBounds().height);
        //shapeRenderer.end();

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
        if (getNrOfActiveShellsAbove() == 0 && egg.getHp()<10)
        shells.add(new Shell( (int) (getLastPlatform().getPosition().x + getLastPlatform().getBounds().width/2 - Shell.getTextureWidth()/2),
                (int)getLastPlatform().getPosition().y + Platform.HEIGHT + 1, 15));
    }

    public int getNrOfActiveShellsAbove(){
        int n = 0;

        for(Shell shell : shells){
            if(shell.isActive() && shell.getPosition().y > egg.getPosition().y)
                n++;
        }
        return n;
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

    public ArrayList<Shell> getShells() {
        return shells;
    }

    public Ground getGround() {
        return ground;
    }

    public Egg getEgg() {
        return egg;
    }
}
