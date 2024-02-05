package gustavosenorans.cat.ioc.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;

import gustavosenorans.cat.ioc.helpers.AssetManager;
import gustavosenorans.cat.ioc.helpers.InputHandler;
import gustavosenorans.cat.ioc.objects.Doll;
import gustavosenorans.cat.ioc.objects.Pelota;
import gustavosenorans.cat.ioc.objects.Sanwich;
import gustavosenorans.cat.ioc.objects.Explosion;
import gustavosenorans.cat.ioc.objects.Moneda;
import gustavosenorans.cat.ioc.objects.Laser;
import gustavosenorans.cat.ioc.objects.ScrollHandler;
import gustavosenorans.cat.ioc.utils.Settings;

import static gustavosenorans.cat.ioc.screens.GameScreen.GameState.PAUSE;
import static gustavosenorans.cat.ioc.screens.GameScreen.GameState.RUNNING;
import static gustavosenorans.cat.ioc.utils.Settings.SCORE_INCREASE_MONEDA;
import static gustavosenorans.cat.ioc.utils.Settings.SCORE_INCREASE_SANWICH;

public class GameScreen implements Screen {

    private ShapeRenderer shapeRenderer;

    public enum GameState {

        READY, RUNNING, GAMEOVER, PAUSE, FIRE

    }

    int score = 0;

    private GameState currentState;

    private final Stage stage;
    private final Doll doll;
    private final ScrollHandler scrollHandler;

    private final ArrayList<Laser> lasers;
    private final ArrayList<Explosion> explosions;
    private final ArrayList<Moneda> monedas;
    private final ArrayList<Sanwich> sanwiches;
    private float explosionTime = 0;

    private final Batch batch;

    private final GlyphLayout textLayout;
    private final GlyphLayout textScore;

    Preferences prefs = Gdx.app.getPreferences( "Score" );

    public GameScreen(Batch prevBatch, Viewport prevViewport) {

        AssetManager.music.play();

        shapeRenderer = new ShapeRenderer();

        stage = new Stage(prevViewport, prevBatch);

        batch = stage.getBatch();

        doll = new Doll(Settings.DOLLS_STARTX, Settings.DOLLS_STARTY, Settings.DOLLS_WIDTH, Settings.DOLLS_HEIGHT);
        scrollHandler = new ScrollHandler();

        stage.addActor(scrollHandler);
        stage.addActor( doll );
        doll.setName("doll");

        Image pause = new Image(AssetManager.pause);
        pause.setName("pause");

        pause.setPosition((Settings.GAME_WIDTH) - pause.getWidth() - 0, 0);
        stage.addActor(pause);

        Image laserButton = new Image(AssetManager.laserButton);
        laserButton.setName("laserButton");

        laserButton.setPosition((Settings.GAME_WIDTH) - laserButton.getWidth() - 0, (Settings.GAME_HEIGHT) - laserButton.getHeight() - 0);
        stage.addActor(laserButton);

        lasers = new ArrayList<>();
        explosions = new ArrayList<>();
        monedas = new ArrayList<>();
        sanwiches = new ArrayList<>();

        textLayout = new GlyphLayout();
        int scoreMax = prefs.getInteger( "HighestScore" );
        int scorePref = prefs.getInteger( "Score" );
        textLayout.setText(AssetManager.font, "Highest Score\n" + "***** " + scoreMax + " *****" + "\nCurrent Score\n" + "***** " + scorePref + " *****" +"\nComenzamos !!!");
        textScore = new GlyphLayout();

        currentState = GameState.READY;

        Gdx.input.setInputProcessor(new InputHandler(this));

    }

    private void drawElements() {

        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        shapeRenderer.setColor(new Color(0, 1, 0, 1));

        shapeRenderer.rect(doll.getX(), doll.getY(), doll.getWidth(), doll.getHeight());

        ArrayList<Pelota> pelotas = scrollHandler.getPelotas();
        Pelota pelota;

        for (int i = 0; i < pelotas.size(); i++) {

            pelota = pelotas.get(i);
            switch (i) {
                case 0:
                    shapeRenderer.setColor(1, 0, 0, 1);
                    break;
                case 1:
                    shapeRenderer.setColor(0, 0, 1, 1);
                    break;
                case 2:
                    shapeRenderer.setColor(1, 1, 0, 1);
                    break;
                default:
                    shapeRenderer.setColor(1, 1, 1, 1);
                    break;
            }
            shapeRenderer.circle(pelota.getX() + pelota.getWidth() / 2, pelota.getY() + pelota.getWidth() / 2, pelota.getWidth() / 2);
        }
        //  shapeRenderer.end();

    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        stage.draw();

        switch (currentState) {

            case GAMEOVER:
                updateGameOver(delta);
                break;
            case RUNNING:
                updateRunning(delta);
                textScore.setText(AssetManager.fontScore, "Score: " + score);
                break;
            case READY:
                updateReady();
                break;
            case PAUSE:
                textLayout.setText(AssetManager.font, "Pause");
                updatePause(delta);
                break;

        }

       // drawElements();

    }

    private void updateReady() {

        batch.begin();
        AssetManager.font.draw(batch, textLayout, (120) - textLayout.width / 2, (67) - textLayout.height / 2);
        batch.end();

    }
    private void updatePause(float delta) {
        stage.act(delta);
        batch.begin();
        AssetManager.font.draw(batch, textLayout, (120) - textLayout.width / 2, (67) - textLayout.height / 2);
        batch.end();
    }

    private void updateRunning(float delta) {

        int scoreMax = prefs.getInteger( "HighestScore" );
        if(scoreMax < score){
            prefs.putInteger( "HighestScore", score );
            prefs.putInteger( "Score", score );
            prefs.flush();
        }else{
            prefs.putInteger( "Score", score );
            prefs.flush();
        }


        stage.act(delta);
        batch.begin();
        AssetManager.fontScore.draw(batch, textScore, (24) - textScore.width / 10, (13) - textScore.height / 10);
        batch.end();

        if (scrollHandler.collides( doll )) {
            AssetManager.explosionSound.play();
            stage.getRoot().findActor("doll").remove();
            if(score < 100){
                textLayout.setText(AssetManager.font, "GAME OVER !!!");
                currentState = GameState.GAMEOVER;
            }else if (score <= 150){
                textLayout.setText(AssetManager.font, "LOS HA HECHO BIEN !!!");
                currentState = GameState.GAMEOVER;
            }else{
                textLayout.setText(AssetManager.font, "ERES MUY BUENO !!!");
                currentState = GameState.GAMEOVER;
            }
        }

        for(Sanwich sanwich : scrollHandler.getSanwiches()){
            if(scrollHandler.collidesSanwich(doll)){
                if (sanwich != null) {
                    AssetManager.explosionPremio.play();
                    explosions.add(new Explosion(AssetManager.sanwichAnim, (sanwich.getX() + sanwich.getWidth() / 2) - 32, sanwich.getY() + sanwich.getHeight() / 2 - 32, 64f, 64f, delta));
                    sanwiches.remove(sanwich);
                    sanwich.remove();
                    scrollHandler.removeSanwich(sanwich);
                    score += SCORE_INCREASE_SANWICH;
                    break;
                }
            }
        }

        for(Moneda moneda : scrollHandler.getMonedas()) {
            if (scrollHandler.collidesMonedas(doll)) {
                if (moneda != null) {
                    AssetManager.explosionPremio.play();
                    explosions.add(new Explosion(AssetManager.monedaAnim, (moneda.getX() + moneda.getWidth() / 2) - 32, moneda.getY() + moneda.getHeight() / 2 - 32, 64f, 64f, delta));
                    monedas.remove(moneda);
                    moneda.remove();
                    scrollHandler.removeMoneda(moneda);
                    score += SCORE_INCREASE_MONEDA;
                    break;
                }
            }
        }

        if(lasers.size()>0) {
            for (Laser laser : lasers) {
                Pelota pelota = scrollHandler.collides(laser);
                if (pelota != null) {
                    AssetManager.explosionSound.play();
                    explosions.add(new Explosion(AssetManager.explosionAnim, (pelota.getX() + pelota.getWidth() / 2) - 32, pelota.getY() + pelota.getHeight() / 2 - 32, 64f, 64f, delta));
                    lasers.remove(laser);
                    laser.remove();
                    scrollHandler.removePelota( pelota );
                    break;
                }
            }
        }

        if(explosions.size()>0){
            for(Explosion exp : explosions){
                if (!exp.isFinished()) {
                    batch.begin();
                    batch.draw( (TextureRegion) exp.getAnim().getKeyFrame(exp.getDelta(), true), exp.getX(), exp.getY(), exp.getWidth(), exp.getHeight());
                    batch.end();
                    exp.setDelta(exp.getDelta()+delta);
                } else {
                    explosions.remove(exp);
                }
                break;
            }
        }
    }

    private void updateGameOver(float delta) {
        stage.act(delta);
        score = 0;
        batch.begin();
        AssetManager.font.draw(batch, textLayout, (Settings.GAME_WIDTH - textLayout.width) / 2, (Settings.GAME_HEIGHT - textLayout.height) / 2);
        AssetManager.fontScore.draw(batch, textScore, (24) - textScore.width / 10, (13) - textScore.height / 10);
        batch.draw( (TextureRegion) AssetManager.explosionAnim.getKeyFrame(explosionTime, false), (doll.getX() + doll.getWidth() / 2) - 32, doll.getY() + doll.getHeight() / 2 - 32, 64, 64);
        batch.end();

        explosionTime += delta;

    }

    public void reset() {
        int scoreMax = prefs.getInteger( "HighestScore" );
        int scorePref = prefs.getInteger( "Score" );
        textLayout.setText(AssetManager.font, "Highest Score\n" + "***** " + scoreMax + " *****" + "\nCurrent Score\n" + "***** " + scorePref + " *****" +"\nComenzamos !!!");
        doll.reset();
        scrollHandler.reset();
        for(Laser l : lasers) l.remove();
        lasers.clear();

        currentState = GameState.READY;

        stage.addActor( doll );
        explosionTime = 0.4f;

    }


    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {
        AssetManager.music.pause();
        if (getCurrentState() == RUNNING) {
            setCurrentState(PAUSE);
        }
    }

    @Override
    public void resume() {
        AssetManager.music.play();
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    public Doll getSpacecraft() {
        return doll;
    }

    public Stage getStage() {
        return stage;
    }

    public GameState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(GameState currentState) {
        switch (currentState) {
            case PAUSE:
                AssetManager.music.setVolume(0.05f);
                hideElements();
                doll.startPause();
                scrollHandler.setPause();
                for(Laser l: lasers) l.startPause();
                break;
            case READY:
                score = 0;
                hideElements();
                break;
            case RUNNING:
                AssetManager.music.setVolume(0.1f);
                showElements();
                doll.stopPause();
                scrollHandler.stopPause();
                for(Laser l: lasers) l.stopPause();
                break;
            case FIRE:
                Laser laser = new Laser( doll.getX()+ doll.getWidth(), doll.getY()+ doll.getHeight()/2, 12, 8, Settings.DOLLS_VELOCITY*2);
                stage.getRoot().addActor(laser);
                lasers.add(laser);
                AssetManager.laserSound.play();
                currentState = GameState.RUNNING;
                break;
        }

        this.currentState = currentState;
    }

    private void hideElements() {
        stage.getRoot().findActor("pause").setVisible(false);
        stage.getRoot().findActor("laserButton").setVisible(false);
    }

    private void showElements() {
        stage.getRoot().findActor("pause").setVisible(true);
        stage.getRoot().findActor("laserButton").setVisible(true);
    }
}
