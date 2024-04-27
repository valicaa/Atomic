package com.mygdx.atomic;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import gameScreens.MainGameScreen;

public class Atomic extends Game {
	public SpriteBatch batch;
	public static final int HEIGHT= 720;
	public static final int WIDTH = 1280;

	@Override
	public void create () {
		batch = new SpriteBatch();
		this.setScreen(new MainGameScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}