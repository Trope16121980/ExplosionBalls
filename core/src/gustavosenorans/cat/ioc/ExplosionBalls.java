package gustavosenorans.cat.ioc;

import com.badlogic.gdx.Game;

import gustavosenorans.cat.ioc.helpers.AssetManager;
import gustavosenorans.cat.ioc.screens.SplashScreen;

public class ExplosionBalls extends Game {

    @Override
    public void create() {

        AssetManager.load();
        setScreen(new SplashScreen(this));
    }

    @Override
    public void dispose() {
        super.dispose();
        AssetManager.dispose();
    }
}