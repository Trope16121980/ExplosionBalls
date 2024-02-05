package gustavosenorans.cat.ioc.helpers;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;

import gustavosenorans.cat.ioc.objects.Doll;
import gustavosenorans.cat.ioc.screens.GameScreen;

public class InputHandler implements InputProcessor {

    int previousY = 0;
    private Doll doll;
    private GameScreen screen;
    private Vector2 stageCoord;

    private Stage stage;

    public InputHandler(GameScreen screen) {

        this.screen = screen;
        doll = screen.getSpacecraft();
        stage = screen.getStage();

    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        switch (screen.getCurrentState()) {

            case READY:
            case PAUSE:
                screen.setCurrentState(GameScreen.GameState.RUNNING);
                break;
            case RUNNING:
                previousY = screenY;
                stageCoord = stage.screenToStageCoordinates(new Vector2(screenX, screenY));
                Actor actorHit = stage.hit(stageCoord.x, stageCoord.y, true);
                if (actorHit != null)
                    switch (actorHit.getName()) {
                        case "doll":
                            break;
                        case "pause":
                            screen.setCurrentState(GameScreen.GameState.PAUSE);
                            break;
                        case "laserButton":
                            screen.setCurrentState(GameScreen.GameState.FIRE);
                            break;
                    }
                break;
            case GAMEOVER:
                screen.reset();
                break;
        }

        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {

        doll.goStraight();
        return true;
    }


    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (Math.abs(previousY - screenY) > 2)

            if (previousY < screenY) {
                doll.goDown();
            } else {
                doll.goUp();
            }
        previousY = screenY;
        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
