package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.simulation.MyGdxGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Traffic Simulator";
		cfg.width = 1000;
		cfg.height = 1000;
		cfg.resizable = false;
		new LwjglApplication(new MyGdxGame(), cfg);
	}
}
