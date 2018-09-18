package com.morchul.adventuretime.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.morchul.adventuretime.Constants;
import com.morchul.adventuretime.screens.GameScreen;

public class Skeleton extends Sprite {

    private GameScreen game;

    //Body and Object
    private Body body;
    private SkeletonRayCast skeletonRayCast;
    private Vector2 velocity;
    private static final int ANIMATION_WIDTH = 43;
    private static final int ANIMATION_HEIGHT = 37;

    //Attributes
    private int life;
    public int damage;
    public boolean inRange;
    private boolean destroyed;

    private boolean attack, aggressive;
    private float timeSinceLastAttack;
    private float timeBetweenAttack;
    private static final float defaultTBA = 1.5f;

    //Animation
    private float stateTime;
    private boolean facingRight;
    private State state, previousState;
    private Animation<TextureRegion> idleAnimation;
    private Animation<TextureRegion> hitAnimation;
    private Animation<TextureRegion> walkAnimation;
    private Animation<TextureRegion> attackAnimation;
    private Animation<TextureRegion> deadAnimation;

    public Vector2 skeletonRayStart;
    public Vector2 skeletonRayEnd;

    public Skeleton(GameScreen game, float x, float y, float height, int life, float timeBetweenAttack){
        this(game, x, y, height * (ANIMATION_WIDTH / ANIMATION_HEIGHT), height, life, timeBetweenAttack);
    }

    public Skeleton(GameScreen game, float x, float y, float height){
        this(game, x, y, height * (ANIMATION_WIDTH / ANIMATION_HEIGHT), height, 50, defaultTBA);
    }

    private Skeleton(GameScreen game, float x, float y, float width, float height, int life, float timeBetweenAttack) {
        this.game = game;

        //initial body and Sprite
        setBounds(x,y, width, height);
        body = game.getBodyFactory().createSkeletonBody(x,y,width, height);
        body.setUserData(this);
        velocity = new Vector2(2 * Constants.TILE_WIDTH,0);
        skeletonRayCast = new SkeletonRayCast();

        //initial Attributes
        this.timeBetweenAttack = timeBetweenAttack;
        this.life = life;

        //initial Animation
        idleAnimation = new Animation<TextureRegion>(0.1f, game.getApp().getAssets().skeletonAtlas.findRegions("Skeleton-Idle"), Animation.PlayMode.LOOP);
        walkAnimation = new Animation<TextureRegion>(0.1f, game.getApp().getAssets().skeletonAtlas.findRegions("Skeleton-Walk"), Animation.PlayMode.LOOP);
        hitAnimation = new Animation<TextureRegion>(0.1f, game.getApp().getAssets().skeletonAtlas.findRegions("Skeleton-Hit"), Animation.PlayMode.NORMAL);
        attackAnimation = new Animation<TextureRegion>(0.1f, game.getApp().getAssets().skeletonAtlas.findRegions("Skeleton-Attack"), Animation.PlayMode.NORMAL);
        deadAnimation = new Animation<TextureRegion>(0.1f, game.getApp().getAssets().skeletonAtlas.findRegions("Skeleton-Dead"), Animation.PlayMode.NORMAL);
        state = State.IDLE;
        previousState = State.IDLE;
        stateTime = 0;
        facingRight = true;
    }

    public void update(float dt){

        if(body.getPosition().y < 0) damage = life;

        if(life > 0) {

            isAggressive();
            //setDirection
            setDirection();

            //update position
            setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);

            if (aggressive && inRange && timeSinceLastAttack <= 0) attack = true;
            if (timeSinceLastAttack > 0) timeSinceLastAttack -= dt;

        } else {
            if(!destroyed) {
                game.getWorld().destroyBody(body);
                game.player.addPoints(100);
                destroyed = true;
            }
        }
        if(!destroyed || stateTime < 5) {
            setState(dt);
            setRegion(getRegion());
        } else {
            delete();
        }
    }

    public boolean isDestroyed(){return destroyed; }

    private void delete(){
        game.getMapCreator().getSkeletons().removeValue(this, true);
    }

    @Override
    public void draw(Batch batch){
        if(!destroyed || stateTime < 5){
            super.draw(batch);
        }
    }

    private void isAggressive() {
        skeletonRayStart = body.getWorldCenter();
        skeletonRayEnd = game.player.body.getWorldCenter();
        game.getWorld().rayCast(skeletonRayCast, skeletonRayStart, skeletonRayEnd);
        aggressive = !game.player.isDestroyed() &&
                skeletonRayCast.isSeePlayer() &&
                Math.abs(skeletonRayEnd.y - skeletonRayStart.y) < getHeight() * 1.5;

        //aggressive = !game.player.destroyed && game.player.getX() - getX() > -4 && game.player.getX() - getX() < 4 && Util.canSeePlayer(this, game);
    }

    private void setDirection(){

        if(aggressive) {
            if(!inRange) {
                if (game.player.getX() - getX() < 0) {
                    body.setLinearVelocity(-4 * Constants.TILE_WIDTH, body.getLinearVelocity().y);
                } else {
                    body.setLinearVelocity(4 * Constants.TILE_WIDTH, body.getLinearVelocity().y);
                }
            }
            else if(!game.player.isDestroyed() && game.player.getX() - getX() > - 0.2 && game.player.getX() - getX() < 0.2){
                body.setLinearVelocity(0,body.getLinearVelocity().y);
            }
        } else {
            body.setLinearVelocity(velocity.x, body.getLinearVelocity().y);
        }
    }

    public void changeDirection(){
        velocity.x = -velocity.x;
    }

    private void attack(){
        attack = false;
        timeSinceLastAttack = timeBetweenAttack;
        if(inRange){
            game.player.damage += 20;
        }
    }

    private void setState(float dt){
        State newState = State.IDLE;
        if(body.getLinearVelocity().x != 0) newState = State.RUNNING;

        if(attack){
            //attack();
            newState = State.ATTACK1;
        }

        if(damage != 0){
            newState = State.HIT;
            life -= damage;
            damage = 0;
        }

        if(state == State.HIT && !hitAnimation.isAnimationFinished(stateTime)){
            newState = State.HIT;
        }

        if(state == State.ATTACK1 && !attackAnimation.isAnimationFinished(stateTime)){
            newState = State.ATTACK1;
        }

        if(attack && state == State.ATTACK1 && stateTime >= attackAnimation.getAnimationDuration() / 2)
            attack();

        if(life <= 0) {
            newState = State.DEAD;
        }

        //Sets the new State
        if(newState == previousState){
            stateTime += dt;
        } else {
            stateTime = 0;
            previousState = state;
            state = newState;
        }
    }

    private TextureRegion getRegion(){
        TextureRegion region;
        switch (state){
            case RUNNING: region = walkAnimation.getKeyFrame(stateTime); break;
            case ATTACK1: region = attackAnimation.getKeyFrame(stateTime); break;
            case DEAD: region = deadAnimation.getKeyFrame(stateTime); break;
            case HIT: region = hitAnimation.getKeyFrame(stateTime); break;
            case IDLE:
            default:
                region = idleAnimation.getKeyFrame(stateTime); break;
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

    private class SkeletonRayCast implements RayCastCallback {

        private boolean seePlayer;

        @Override
        public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
            if(fixture.getUserData() == Type.PLAYER || fixture.getUserData() == Type.PLAYER_HIT_RANGE){
                seePlayer = true;
            } else if(fixture.getUserData() != Type.SKELETON){
                seePlayer = false;
            }
            return fraction;
        }

        private boolean isSeePlayer(){
            return seePlayer;
        }
    }
}
