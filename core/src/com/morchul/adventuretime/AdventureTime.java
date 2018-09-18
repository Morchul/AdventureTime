package com.morchul.adventuretime;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.morchul.adventuretime.screens.EndScreen;
import com.morchul.adventuretime.screens.GameScreen;
import com.morchul.adventuretime.screens.LoadingScreen;
import com.morchul.adventuretime.screens.MenuScreen;

public class AdventureTime extends Game {

	private Assets assets;
	private Settings settings;

	private MenuScreen menuScreen;
	private GameScreen gameScreen;
	private EndScreen endScreen;
	
	@Override
	public void create () {
		settings = new Settings();
		assets = new Assets();

		menuScreen = new MenuScreen(this);
		gameScreen = new GameScreen(this);
		endScreen = new EndScreen(this);

		setScreen(new LoadingScreen(this));
	}

	public void setMenuScreen(){
		changeScreen(menuScreen);
	}

	public void setGameScreen(){
		gameScreen.setLevel(settings.getLevel());
		changeScreen(gameScreen);
	}

	public void nextLevel(){
		int points = gameScreen.player.getPoints();
		setMenuScreen();
		String nextLevel = settings.getLevel();
		if(Util.getLevel(nextLevel) == 10){
			endScreen.setPoints(points);
			changeScreen(endScreen);
		} else
			setGameScreen();
	}

	private void changeScreen(Screen screen){
		Screen oldScreen = getScreen();
		setScreen(screen);
		oldScreen.dispose();
	}

	@Override
	public void render(){
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		super.render();
	}

	public Assets getAssets() {
		return assets;
	}

	public Settings getSettings() {
		return settings;
	}

	public void dispose(){
		getScreen().dispose();
		assets.dispose();
	}
}
