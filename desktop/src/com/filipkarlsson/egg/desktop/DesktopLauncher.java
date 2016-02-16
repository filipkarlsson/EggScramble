package com.filipkarlsson.egg.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.filipkarlsson.egg.EggScramble;
import com.filipkarlsson.egg.sprites.Egg;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = EggScramble.WIDTH;
		config.height = EggScramble.HEIGHT;
		config.title  = EggScramble.TITLE;
		new LwjglApplication(new EggScramble(), config);
	}
}
