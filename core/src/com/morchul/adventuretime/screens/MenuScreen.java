package com.morchul.adventuretime.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.morchul.adventuretime.AdventureTime;
import com.morchul.adventuretime.Constants;

public class MenuScreen implements Screen {

    private AdventureTime app;
    private Stage stage;

    public MenuScreen(AdventureTime app) {
        this.app = app;
    }

    @Override
    public void show() {
        stage = new Stage(new FitViewport(Constants.WORLD_PIXEL_WIDTH, Constants.WORLD_PIXEL_HEIGHT));

        Gdx.input.setInputProcessor(stage);

        Label l = new Label("Adventure Time", new Label.LabelStyle(app.getAssets().font, Color.WHITE));
        TextButton start = new TextButton("I'm going on an Adventure", app.getAssets().defaultSkin);
        start.addListener(new ClickListener(){
            public void clicked (InputEvent event, float x, float y) {
                app.setGameScreen();
            }
        });

        Table table = new Table().top();
        table.setFillParent(true);
        table.setDebug(false);

        table.add(l);
        table.row();
        table.add(start);

        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        stage.act(delta);
        stage.draw();
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
