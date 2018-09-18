package com.morchul.adventuretime.box2d;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.morchul.adventuretime.sprites.Type;

public class BodyFactory {

    private World world;

    private static final short GROUND_BIT = 1;
    private static final short OBJECT_BIT = 2;
    private static final short PLAYER_BIT = 4;
    private static final short SKELETON_BIT = 8;

    public BodyFactory(World world) {
        this.world = world;
    }

    public void createSkeletonControlBody(float x, float y, float width, float height){
        Body body = createBody(x, y, width, height, BodyDef.BodyType.StaticBody, false);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2, height / 2);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.isSensor = true;
        body.createFixture(fixtureDef).setUserData(Type.SKELETON_CONTROL_WALL);

        shape.dispose();
    }

    public Body createChestBody(float x, float y, float width, float height){
        Body body = createBody(x, y, width, height, BodyDef.BodyType.StaticBody, false);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2, height / 2);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.isSensor = true;
        body.createFixture(fixtureDef).setUserData(Type.CHEST);

        shape.dispose();

        return body;
    }

    public Body createCoinBody(float x, float y, float width, float height){
        Body body = createBody(x, y, width, height, BodyDef.BodyType.StaticBody, false);

        CircleShape shape = new CircleShape();
        shape.setRadius(width / 2);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.isSensor = true;
        body.createFixture(fixtureDef).setUserData(Type.COIN);

        shape.dispose();

        return body;
    }

    public void createGroundBody(float x, float y, float width, float height){
        Body body = createBody(x, y, width, height, BodyDef.BodyType.StaticBody, false);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2, height / 2);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = GROUND_BIT;
        body.createFixture(fixtureDef).setUserData(Type.GROUND);

        shape.dispose();
    }

    public void createObjectBody(float x, float y, float width, float height){
        Body body = createBody(x, y, width, height, BodyDef.BodyType.StaticBody, false);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2, height / 2);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = OBJECT_BIT;
        body.createFixture(fixtureDef).setUserData(Type.OBJECT);

        shape.dispose();
    }

    public Body createSkeletonBody(float x, float y, float width, float height){
        Body body = createBody(x,y,width, height, BodyDef.BodyType.DynamicBody, true);

        PolygonShape shape = new PolygonShape();
        float temp = height / 8;
        Vector2 center = new Vector2(0,-temp);
        shape.setAsBox(width / 2, height / 2-temp,center , 0);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = SKELETON_BIT;
        fixtureDef.filter.maskBits = GROUND_BIT | OBJECT_BIT;

        body.createFixture(fixtureDef).setUserData(Type.SKELETON);
        shape.dispose();

        return body;
    }

    public void createLadderBody(float x, float y, float width, float height){
        Body body = createBody(x, y, width, height, BodyDef.BodyType.StaticBody, false);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2, height / 2);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.isSensor = true;
        body.createFixture(fixtureDef).setUserData(Type.LADDER);

        shape.dispose();
    }

    public Body createPlayerBody(float x, float y, float width, float height){
        Body body = createBody(x,y,width, height, BodyDef.BodyType.DynamicBody, true);

        PolygonShape shape = new PolygonShape();
        float temp = height / 8;
        Vector2 center = new Vector2(0,-temp);
        shape.setAsBox(width / 2, height / 2-temp,center , 0);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.friction = 0f;
        fixtureDef.filter.categoryBits = PLAYER_BIT;
        fixtureDef.filter.maskBits = GROUND_BIT | OBJECT_BIT;

        body.createFixture(fixtureDef).setUserData(Type.PLAYER);

        shape.dispose();
        shape = new PolygonShape();
        shape.setAsBox(width, height / 2);
        fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.isSensor = true;
        body.createFixture(fixtureDef).setUserData(Type.PLAYER_HIT_RANGE);
        shape.dispose();

        return body;
    }

    //--------------------------------------------------------------------

    private Body createBody(float x, float y, float width, float height, BodyDef.BodyType bodyType, boolean fixedRotation){
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.x = x + width / 2;
        bodyDef.position.y = y + height / 2;
        bodyDef.type = bodyType;
        bodyDef.fixedRotation = fixedRotation;

        return world.createBody(bodyDef);
    }
}
