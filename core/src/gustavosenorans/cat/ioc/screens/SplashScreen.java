package gustavosenorans.cat.ioc.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import gustavosenorans.cat.ioc.ExplosionBalls;
import gustavosenorans.cat.ioc.helpers.AssetManager;
import gustavosenorans.cat.ioc.utils.Settings;


public class SplashScreen implements Screen {

    private Stage stage;
    private ExplosionBalls game;

    private Label.LabelStyle textStyle;
    private Label textLbl;

    private Label.LabelStyle textStyleStart;
    private Label textLblStart;
    private Batch batch;
    private float dollRunTime = 0;
    private int posX = 0 - Settings.DOLLS_WIDTH;

    public SplashScreen(ExplosionBalls game) {

        this.game = game;

        OrthographicCamera camera = new OrthographicCamera(Settings.GAME_WIDTH, Settings.GAME_HEIGHT);
        camera.setToOrtho(true);

        StretchViewport viewport = new StretchViewport(Settings.GAME_WIDTH, Settings.GAME_HEIGHT, camera);

        stage = new Stage(viewport);
        batch = stage.getBatch();

        stage.addActor(new Image(AssetManager.background));

        textStyle = new Label.LabelStyle(AssetManager.font, null);
        textLbl = new Label("Explosion Balls", textStyle);

        Container container = new Container(textLbl);
        container.setTransform(true);
        container.center();
        container.setPosition(Settings.GAME_WIDTH / 2, Settings.GAME_HEIGHT / 3);

        container.addAction(Actions.repeat(RepeatAction.FOREVER, Actions.sequence(Actions.scaleTo(1.5f, 1.5f, 1), Actions.scaleTo(1, 1, 1))));
        stage.addActor(container);

        textStyleStart = new Label.LabelStyle(AssetManager.fontStart, null);
        textLblStart = new Label("Toque la pantalla para comenzar", textStyleStart);
        Container container2 = new Container(textLblStart);
        container2.setTransform(true);
        container2.center();
        container2.setPosition(Settings.GAME_WIDTH / 2, 5*Settings.GAME_HEIGHT / 6);

        container2.addAction(Actions.repeat(RepeatAction.FOREVER, Actions.sequence(Actions.alpha(1.0f, 0.5f), Actions.alpha(0.0f, 0.5f))));
        stage.addActor(container2);

        float y = Settings.GAME_HEIGHT / 2 + textLbl.getHeight();


    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        stage.draw();
        stage.act(delta);
        batch.begin();
        batch.draw( (TextureRegion) AssetManager.dollAnim.getKeyFrame( dollRunTime, true ), posX, Settings.DOLLS_STARTY , Settings.DOLLS_STARTY ,Settings.DOLLS_MEDIDA);
        batch.end();
        if(posX > Settings.GAME_WIDTH){
            posX = 0 - Settings.DOLLS_WIDTH;
        } else {
            posX += 1;
        }
        dollRunTime += delta;

        if (Gdx.input.isTouched()) {
            game.setScreen(new GameScreen(stage.getBatch(), stage.getViewport()));
            dispose();
        }

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
