package com.morchul.adventuretime.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.morchul.adventuretime.buffs.Buf;
import com.morchul.adventuretime.screens.GameScreen;

public class TreasureChest extends Sprite {

    private GameScreen game;

    //Body and Object
    public Body body;
    private static final int ANIMATION_WIDTH = 73;
    private static final int ANIMATION_HEIGHT = 56;

    //Attributes
    private boolean open;

    //Animation
    private float stateTime;
    private Animation<TextureRegion> openAnimation;

    public TreasureChest(GameScreen game, float x, float y, float height){
        this(game, x, y, height * (ANIMATION_WIDTH / ANIMATION_HEIGHT), height);
    }

    private TreasureChest(GameScreen game, float x, float y, float width, float height) {
        this.game = game;

        //initial body and object
        setBounds(x, y, width, height);
        body = game.getBodyFactory().createChestBody(x,y,width, height);
        body.setUserData(this);

        //initial Attribute
        open = false;

        //initial Animation
        openAnimation = new Animation<TextureRegion>(0.1f, game.getApp().getAssets().chestAtlas.findRegions("chest"), Animation.PlayMode.NORMAL);
        stateTime = 0;

        setRegion(openAnimation.getKeyFrame(stateTime));
    }

    public void update(float dt){
        if(open){
            setRegion(openAnimation.getKeyFrame(stateTime));
            stateTime += dt;
        }
    }

    public void playerTouch(){
        if(!open) {
            game.player.addBuf(Buf.values()[MathUtils.random(0, 1)]);
            open = true;
        }
    }
}
