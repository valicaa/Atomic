package gameScreens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.atomic.Atomic;
import entity.Player;

public class MainGameScreen implements Screen {
    Atomic game;
    Player player;
    SpriteBatch batch;

    public MainGameScreen(Atomic game){
        //Importing Game Object
        this.game = game;
        player = new Player();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float v) {
        Gdx.gl.glClearColor(1,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        player.action();

        game.batch.begin();
        game.batch.draw(player.getPlayerKeyFrame(), player.getX(), player.getY(), player.getPlayerWidth(), player.getPlayerHeight());
        game.batch.end();
    }

    @Override
    public void resize(int i, int i1) {

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