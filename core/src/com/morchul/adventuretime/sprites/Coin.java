package com.morchul.adventuretime.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.morchul.adventuretime.screens.GameScreen;

public class Coin extends Sprite {

    private GameScreen game;

    //Body and Object
    public Body body;
    private static final int ANIMATION_WIDTH = 42;
    private static final int ANIMATION_HEIGHT = 42;

    //Attributes
    private boolean destroy, destroyed;

    //Animation
    private float stateTime;
    private Animation<TextureRegion> idleAnimation;

    public Coin(GameScreen game, float x, float y, float height){
        this(game, x, y, height * (ANIMATION_WIDTH / ANIMATION_HEIGHT), height);
    }

    private Coin(GameScreen game, float x, float y, float width, float height) {
        this.game = game;

        //initial body and object
        setBounds(x, y, width, height);
        body = game.getBodyFactory().createCoinBody(x, y, width, height);
        body.setUserData(this);

        //initial Attributes
        destroy = false;
        destroyed = false;

        //initial Animation
        idleAnimation = new Animation<TextureRegion>(0.1f, game.getApp().getAssets().coinAtlas.findRegions("Coin"), Animation.PlayMode.LOOP);
        stateTime = 0;
    }

    public void update(float dt){
        if(destroy && !destroyed){
            game.getWorld().destroyBody(body);
            game.getMapCreator().getCoins().removeValue(this, true);
            destroyed = true;
        } else if(!destroyed) {
            setRegion(idleAnimation.getKeyFrame(stateTime));
            stateTime += dt;
        }
    }

    public void playerTouch(){
        game.player.addPoints(50);
        destroy = true;
    }
}
