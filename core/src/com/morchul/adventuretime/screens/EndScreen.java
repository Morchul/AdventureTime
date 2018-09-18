package com.morchul.adventuretime.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.morchul.adventuretime.AdventureTime;
import com.morchul.adventuretime.Constants;

public class EndScreen implements Screen {

    private AdventureTime app;

    private Stage stage;
    private int points;

    public EndScreen(AdventureTime app) {
        this.app = app;
        points = 0;
    }

    @Override
    public void show() {
        resetSettings();
        stage = new Stage(new FitViewport(Constants.WORLD_PIXEL_WIDTH, Constants.WORLD_PIXEL_HEIGHT));

        Label pointLabel = new Label(String.format("Points: %06d", points), new Label.LabelStyle(app.getAssets().font, Color.WHITE));

        TextButton ok = new TextButton("Ok", app.getAssets().defaultSkin);
        ok.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                app.setMenuScreen();
            }
        });

        Table table = new Table();
        table.setFillParent(true);

        table.add(pointLabel).expandX();
        table.row();
        table.add(ok).expandX();

        stage.addActor(table);

        Gdx.input.setInputProcessor(stage);
    }

    private void resetSettings(){
        app.getSettings().setLevel("level1-1");
        app.getSettings().setPoints(0);
    }

    public void setPoints(int points){
        this.points = points;
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
