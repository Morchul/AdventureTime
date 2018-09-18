package com.morchul.adventuretime.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.morchul.adventuretime.Util;
import com.morchul.adventuretime.buffs.Buf;
import com.morchul.adventuretime.screens.GameScreen;

public class Player extends Sprite {

    private GameScreen game;

    //Body and Object
    public Body body;
    private static final int ANIMATION_WIDTH = 50;
    private static final int ANIMATION_HEIGHT = 37;

    //Attributes
    private int life, points;
    public int damage;
    private boolean destroyed;
    private Array<Buf> buffs;

    //Player input
    public boolean right, left, up, down, attack, collideWithObject, climb, climbing, jump;


    //Animation
    private float stateTime;
    private boolean facingRight;
    private State state, previousState;
    private Animation<TextureRegion> idleAnimation;
    private Animation<TextureRegion> runningAnimation;
    private Animation<TextureRegion> fallingAnimation;
    private Animation<TextureRegion> jumpingAnimation;
    private Animation<TextureRegion> attack_1_Animation;
    private Animation<TextureRegion> attack_2_Animation;
    private Animation<TextureRegion> attack_3_Animation;
    private Animation<TextureRegion> deadAnimation;
    private Animation<TextureRegion> hitAnimation;
    private Animation<TextureRegion> slideAnimation;
    private Animation<TextureRegion> climbAnimation;


    public Player(GameScreen game, float x, float y, float height){
        this(game, x, y, height * (ANIMATION_WIDTH / ANIMATION_HEIGHT), height);
    }

    private Player(GameScreen game, float x, float y, float width, float height) {
        this.game = game;

        //initial Player attribute
        buffs = new Array<Buf>();
        life = 100;
        points = game.getApp().getSettings().getPoints();

        //initial body and Sprite
        setBounds(x, y, width, height);
        body = game.getBodyFactory().createPlayerBody(x,y,width / 2, height);
        body.setUserData(this);

        //initial Animation
        idleAnimation = new Animation<TextureRegion>(0.1f, game.getApp().getAssets().adventurerAtlas.findRegions("adventurer-idle"), Animation.PlayMode.LOOP);
        runningAnimation = new Animation<TextureRegion>(0.1f, game.getApp().getAssets().adventurerAtlas.findRegions("adventurer-run"), Animation.PlayMode.LOOP);
        fallingAnimation = new Animation<TextureRegion>(0.1f, game.getApp().getAssets().adventurerAtlas.findRegions("adventurer-fall"), Animation.PlayMode.LOOP);
        jumpingAnimation = new Animation<TextureRegion>(0.1f, game.getApp().getAssets().adventurerAtlas.findRegions("adventurer-jump"), Animation.PlayMode.NORMAL);
        attack_1_Animation = new Animation<TextureRegion>(0.1f, game.getApp().getAssets().adventurerAtlas.findRegions("adventurer-attack1"), Animation.PlayMode.NORMAL);
        attack_2_Animation = new Animation<TextureRegion>(0.1f, game.getApp().getAssets().adventurerAtlas.findRegions("adventurer-attack2"), Animation.PlayMode.NORMAL);
        attack_3_Animation = new Animation<TextureRegion>(0.1f, game.getApp().getAssets().adventurerAtlas.findRegions("adventurer-attack3"), Animation.PlayMode.NORMAL);
        deadAnimation = new Animation<TextureRegion>(0.1f, game.getApp().getAssets().adventurerAtlas.findRegions("adventurer-die"), Animation.PlayMode.NORMAL);
        hitAnimation = new Animation<TextureRegion>(0.1f, game.getApp().getAssets().adventurerAtlas.findRegions("adventurer-hurt"), Animation.PlayMode.NORMAL);
        slideAnimation = new Animation<TextureRegion>(0.1f, game.getApp().getAssets().adventurerAtlas.findRegions("adventurer-wall-slide"), Animation.PlayMode.LOOP);
        climbAnimation = new Animation<TextureRegion>(0.1f, game.getApp().getAssets().adventurerAtlas.findRegions("adventurer-ladder-climb"), Animation.PlayMode.LOOP);
        state = State.IDLE;
        previousState = State.IDLE;
        stateTime = 0;
        facingRight = true;

    }

    public boolean isDestroyed(){return destroyed;}

    public void addBuf(Buf buf){
        buffs.add(buf);
        Gdx.app.log("Player", "Add Buf: " + buf);
    }

    public Array<Buf> getBuffs() {
        return buffs;
    }

    /**
     * update Player
     * @param dt the delta currentTime between this and last frame
     */
    public void update(float dt){
        if(body.getPosition().y < 0) life = 0;
        if(life > 0) {

            if(climb && up) climbing = true;
            else if(!climb) climbing = false;

            handleInput();

            //update position
            setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        } else {
            if(!destroyed) {
                game.getWorld().destroyBody(body);
                destroyed = true;
            }
        }

        if(climbing){
            body.setGravityScale(0);
        } else {
            body.setGravityScale(1);
        }

        updateBuffs(dt);
        setState(dt);
        setRegion(getRegion());
    }

    private void updateBuffs(float dt){
        for(Buf buf: buffs){
            buf.currentTime -= dt;
            if(buf.currentTime <= 0) {
                buf.reset();
                Gdx.app.log("Player", "Remove buf " + buf);
                buffs.removeValue(buf, true);
            }
        }
    }

    /**
     * Handle input from PlayerInputProcessor
     */
    private void handleInput(){
        if(right)
            body.setLinearVelocity(3f,body.getLinearVelocity().y);
        else if(left)
            body.setLinearVelocity(-3f,body.getLinearVelocity().y);
        else {
            body.setLinearVelocity(0f, body.getLinearVelocity().y);
        }

        if(climbing) {
            if (up)
                body.setLinearVelocity(body.getLinearVelocity().x, 2);
            else if(down)
                body.setLinearVelocity(body.getLinearVelocity().x, -2);
            else
                body.setLinearVelocity(body.getLinearVelocity().x, 0);
        }
        if(jump) {
            if (body.getLinearVelocity().y == 0) {
                body.applyLinearImpulse(new Vector2(0, 8f), body.getWorldCenter(), true);
                climbing = false;
            }
            jump = false;
        }
    }

    private void attack(int damage){
        for(Skeleton skeleton : game.getMapCreator().getSkeletons()) {
            if (skeleton.inRange) {
                skeleton.damage = damage;
            }
        }
        attack = false;
    }

    /**
     * Sets the State of Player
     * @param dt the delta currentTime between this and last frame
     */
    private void setState(float dt){

        State newState = State.IDLE;
        if(climbing) newState = State.CLIMP;
        else if(body.getLinearVelocity().y > 0) newState = State.JUMPING;
        else if(body.getLinearVelocity().y < 0){
            if((left || right) && collideWithObject){
                newState = State.SLIDE_WALL;
            } else {
                newState = State.FALLING;
            }
        }
        else if(body.getLinearVelocity().x != 0) newState = State.RUNNING;

        if(damage != 0){
            if(!Util.hasBuff(buffs, Buf.IMMORTAL)){
                newState = State.HIT;
                life -= damage;
            }
            damage = 0;
        }
        //-----------------------------------------
        //Attack State
        if(attack) {

            if (state == State.ATTACK2 && attack_2_Animation.isAnimationFinished(stateTime)) {
                newState = State.ATTACK3;
                attack(Util.hasBuff(buffs, Buf.INCREASE_DAMAGE) ? 60 : 30);
            } else if (state == State.ATTACK1 && attack_1_Animation.isAnimationFinished(stateTime)) {
                newState = State.ATTACK2;
                attack(Util.hasBuff(buffs, Buf.INCREASE_DAMAGE) ? 40 : 20);
            } else if (state == State.RUNNING || state == State.IDLE) {
                newState = State.ATTACK1;
                attack(Util.hasBuff(buffs, Buf.INCREASE_DAMAGE) ? 20 : 10);
            } else if(state == State.SLIDE_WALL || state == State.JUMPING || state == State.FALLING){
                attack = false;
            }
        }
        if(state == State.HIT && !hitAnimation.isAnimationFinished(stateTime))
            newState = State.HIT;
        if(state == State.ATTACK1 && !attack_1_Animation.isAnimationFinished(stateTime))
            newState = State.ATTACK1;
        else if(state == State.ATTACK2 && !attack_2_Animation.isAnimationFinished(stateTime))
            newState = State.ATTACK2;
        else if(state == State.ATTACK3 && !attack_3_Animation.isAnimationFinished(stateTime))
            newState = State.ATTACK3;
        //---------------------------------------------

        if(life <= 0){
            newState = State.DEAD;
        }

        //Sets the new State
        if(newState == previousState){
            if (newState != State.CLIMP || up || down) {
                stateTime += dt;
            }
        } else {
            stateTime = 0;
            previousState = state;
            state = newState;
        }
    }

    /**
     * Get the TextureRegion
     * @return TextureRegion of current state Animation and stateTime
     */
    private TextureRegion getRegion(){

        TextureRegion region;
        switch (state){
            case JUMPING: region = jumpingAnimation.getKeyFrame(stateTime);break;
            case RUNNING: region = runningAnimation.getKeyFrame(stateTime);break;
            case FALLING: region = fallingAnimation.getKeyFrame(stateTime);break;
            case ATTACK1: region = attack_1_Animation.getKeyFrame(stateTime);break;
            case ATTACK2: region = attack_2_Animation.getKeyFrame(stateTime);break;
            case ATTACK3: region = attack_3_Animation.getKeyFrame(stateTime);break;
            case DEAD: region = deadAnimation.getKeyFrame(stateTime); break;
            case HIT: region = hitAnimation.getKeyFrame(stateTime); break;
            case SLIDE_WALL: region = slideAnimation.getKeyFrame(stateTime); break;
            case CLIMP: region = climbAnimation.getKeyFrame(stateTime); break;
            case IDLE:
            default: region = idleAnimation.getKeyFrame(stateTime);break;
        }

        //Flip region
        if((body.getLinearVelocity().x < 0 || !facingRight) && !region.isFlipX()) {
            region.flip(true, false);
            facingRight = false;
        } else if((body.getLinearVelocity().x > 0 || facingRight) && region.isFlipX()) {
            region.flip(true, false);
            facingRight = true;
        }

        return region;
    }

    public int getLife(){return life;}

    public int getPoints() {
        return points;
    }

    public void addPoints(int points){this.points += points;}
}
