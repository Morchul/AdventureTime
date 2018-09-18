package com.morchul.adventuretime.box2d;

import com.badlogic.gdx.physics.box2d.*;
import com.morchul.adventuretime.sprites.*;

public class CollisionDetector implements ContactListener {

    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        if(fixA.getUserData() == Type.PLAYER_HIT_RANGE && fixB.getUserData() == Type.SKELETON)
            ((Skeleton)fixB.getBody().getUserData()).inRange = true;
        else if(fixB.getUserData() == Type.PLAYER_HIT_RANGE && fixA.getUserData() == Type.SKELETON)
            ((Skeleton)fixA.getBody().getUserData()).inRange = true;

        else if(fixA.getUserData() == Type.PLAYER && fixB.getUserData() == Type.OBJECT)
            ((Player)fixA.getBody().getUserData()).collideWithObject = true;
        else if(fixB.getUserData() == Type.PLAYER && fixA.getUserData() == Type.OBJECT)
            ((Player)fixB.getBody().getUserData()).collideWithObject = true;

        else if(fixA.getUserData() == Type.SKELETON && fixB.getUserData() == Type.SKELETON_CONTROL_WALL)
            ((Skeleton)fixA.getBody().getUserData()).changeDirection();
        else if(fixB.getUserData() == Type.SKELETON && fixA.getUserData() == Type.SKELETON_CONTROL_WALL)
            ((Skeleton)fixB.getBody().getUserData()).changeDirection();
        else if(fixA.getUserData() == Type.SKELETON && fixB.getUserData() == Type.OBJECT)
            ((Skeleton)fixA.getBody().getUserData()).changeDirection();
        else if(fixB.getUserData() == Type.SKELETON && fixA.getUserData() == Type.OBJECT)
            ((Skeleton)fixB.getBody().getUserData()).changeDirection();

        else if(fixA.getUserData() == Type.PLAYER && fixB.getUserData() == Type.COIN)
            ((Coin)fixB.getBody().getUserData()).playerTouch();
        else if(fixB.getUserData() == Type.PLAYER && fixA.getUserData() == Type.COIN)
            ((Coin)fixA.getBody().getUserData()).playerTouch();

        else if(fixA.getUserData() == Type.PLAYER && fixB.getUserData() == Type.CHEST)
            ((TreasureChest)fixB.getBody().getUserData()).playerTouch();
        else if(fixB.getUserData() == Type.PLAYER && fixA.getUserData() == Type.CHEST)
            ((TreasureChest)fixA.getBody().getUserData()).playerTouch();

        else if(fixA.getUserData() == Type.PLAYER && fixB.getUserData() == Type.LADDER)
            ((Player)fixA.getBody().getUserData()).climb = true;
        else if(fixB.getUserData() == Type.PLAYER && fixA.getUserData() == Type.LADDER)
            ((Player)fixB.getBody().getUserData()).climb = true;
    }

    @Override
    public void endContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        if(fixA.getUserData() == Type.PLAYER_HIT_RANGE && fixB.getUserData() == Type.SKELETON)
            ((Skeleton)fixB.getBody().getUserData()).inRange = false;
        else if(fixB.getUserData() == Type.PLAYER_HIT_RANGE && fixA.getUserData() == Type.SKELETON)
            ((Skeleton)fixA.getBody().getUserData()).inRange = false;

        else if(fixA.getUserData() == Type.PLAYER && fixB.getUserData() == Type.OBJECT)
            ((Player)fixA.getBody().getUserData()).collideWithObject = false;
        else if(fixB.getUserData() == Type.PLAYER && fixA.getUserData() == Type.OBJECT)
            ((Player)fixB.getBody().getUserData()).collideWithObject = false;

        else if(fixA.getUserData() == Type.PLAYER && fixB.getUserData() == Type.LADDER)
            ((Player)fixA.getBody().getUserData()).climb = false;
        else if(fixB.getUserData() == Type.PLAYER && fixA.getUserData() == Type.LADDER)
            ((Player)fixB.getBody().getUserData()).climb = false;
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
