package com.morchul.adventuretime.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.morchul.adventuretime.AdventureTime;
import com.morchul.adventuretime.Constants;

public class LoadingScreen implements Screen {

    private final AdventureTime app;
    private final Stage stage;

    private ProgressBar progressBar;

    public LoadingScreen(final AdventureTime app) {
        this.app = app;
        stage = new Stage(new FitViewport(Constants.WORLD_PIXEL_WIDTH, Constants.WORLD_PIXEL_HEIGHT));

    }

    @Override
    public void show() {
        Label titel = new Label("Adventure Time", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        progressBar = new ProgressBar(0,1,0.1f,false,app.getAssets().defaultSkin);

        Table table = new Table();
        table.setFillParent(true);
        table.setDebug(false);

        table.add(titel);
        table.row();
        table.add(progressBar);

        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        stage.draw();
        progressBar.setValue(app.getAssets().getProgress());
        if(app.getAssets().update()){
            app.setMenuScreen();
        }
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
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
        stage.dispose();
    }
}
