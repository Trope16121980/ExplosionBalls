package gustavosenorans.cat.ioc;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import gustavosenorans.cat.ioc.utils.Settings;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.title = "ExplosionBalls";
		config.width = Settings.GAME_WIDTH * 2;
		config.height = Settings.GAME_HEIGHT * 2;

		new LwjglApplication(new ExplosionBalls(), config);
	}
}

