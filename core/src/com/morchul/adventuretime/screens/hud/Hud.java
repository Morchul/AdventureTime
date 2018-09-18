package com.morchul.adventuretime.screens.hud;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.morchul.adventuretime.Constants;
import com.morchul.adventuretime.buffs.Buf;
import com.morchul.adventuretime.screens.GameScreen;
import com.morchul.adventuretime.sprites.Player;

public class Hud implements Disposable {

    private Stage stage;
    private FitViewport fitViewport;
    private Player player;

    private Label playerLife;
    private Label playerPoints;
    private Label timeLabel;

    private Label winLabel;
    private Label loosLabel;

    private Label buffLabel;

    private Table table;

    private int time;
    private float timeDone;

    public Hud(GameScreen game) {
        this.player = game.player;
        fitViewport = new FitViewport(Constants.WORLD_PIXEL_WIDTH, Constants.WORLD_PIXEL_HEIGHT);
        stage = new Stage(fitViewport, game.getBatch());

        table = new Table(game.getApp().getAssets().defaultSkin).top();
        table.setFillParent(true);
        table.setDebug(false);

        playerLife = new Label(String.format("%03d", player.getLife()), game.getApp().getAssets().defaultSkin);
        playerPoints = new Label(String.format("%06d", player.getPoints()), game.getApp().getAssets().defaultSkin);
        timeLabel = new Label(String.format("%03d", time), game.getApp().getAssets().defaultSkin);
        winLabel = new Label("Win", new Label.LabelStyle(game.getApp().getAssets().font, Color.WHITE));
        loosLabel = new Label("Game Over", new Label.LabelStyle(game.getApp().getAssets().font, Color.WHITE));
        buffLabel = new Label("", game.getApp().getAssets().defaultSkin);

        table.row().top();
        table.add("Points").expandX();
        table.add("Life").expandX();
        table.add("Time").expandX();
        table.row().top();
        table.add(playerPoints).expandX();
        table.add(playerLife).expandX();
        table.add(timeLabel).expandX();
        table.row();
        table.add(buffLabel);
        table.row();

        stage.addActor(table);

        time = 0;
        timeDone = 0;
    }

    public void win(){
        player.addPoints((180 - time) * 10);
        table.add(winLabel).center().colspan(3).expand();
    }

    public void loos(){
        table.add(loosLabel).center().colspan(3).expand();
    }

    public void act(float dt){
        stage.act(dt);
        timeDone += dt;
        if(timeDone > 1){
            timeDone -= 1;
            ++time;
        }
        timeLabel.setText(String.format("%03d", time));
        playerLife.setText(String.format("%03d", player.getLife()));
        playerPoints.setText(String.format("%06d", player.getPoints()));

        buffLabel.setText("");
        for(Buf buf : player.getBuffs()){
            buffLabel.setText(buf.toString());
        }
    }

    public void draw(){
        stage.draw();
    }

    public Stage getStage() {
        return stage;
    }

    @Override
    public void dispose(){
        stage.dispose();
    }
}
