package com.morchul.adventuretime.box2d;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.morchul.adventuretime.Constants;
import com.morchul.adventuretime.screens.GameScreen;
import com.morchul.adventuretime.sprites.Coin;
import com.morchul.adventuretime.sprites.Skeleton;
import com.morchul.adventuretime.sprites.TreasureChest;

public class MapCreator {

    private Array<Skeleton> skeletons;
    private Array<Coin> coins;
    private Array<TreasureChest> treasureChests;

    public MapCreator(GameScreen game) {

        skeletons = new Array<Skeleton>();
        coins = new Array<Coin>();
        treasureChests = new Array<TreasureChest>();

        //Create for all Objects in Ground (2) Layer Bodies in World
        for(MapObject object : game.getMap().getLayers().get(2).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            game.getBodyFactory().createGroundBody(rect.getX() / Constants.PPM, rect.getY() / Constants.PPM, rect.getWidth() / Constants.PPM , rect.getHeight() / Constants.PPM);
        }

        for(MapObject object : game.getMap().getLayers().get(3).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            game.getBodyFactory().createObjectBody(rect.getX() / Constants.PPM, rect.getY() / Constants.PPM, rect.getWidth() / Constants.PPM , rect.getHeight() / Constants.PPM);
        }

        //Skeleton
        for(MapObject object : game.getMap().getLayers().get(5).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            skeletons.add(new Skeleton(game, rect.getX() / Constants.PPM, rect.getY() / Constants.PPM, rect.getHeight() / Constants.PPM));
        }

        //Boss Skeleton
        for(MapObject object : game.getMap().getLayers().get(6).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            skeletons.add(new Skeleton(game, rect.getX() / Constants.PPM, rect.getY() / Constants.PPM, rect.getHeight() / Constants.PPM, 200, 2));
        }

        //Skeleton control wall
        for(MapObject object : game.getMap().getLayers().get(4).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            game.getBodyFactory().createSkeletonControlBody(rect.getX() / Constants.PPM, rect.getY() / Constants.PPM, rect.getWidth() / Constants.PPM , rect.getHeight() / Constants.PPM);
        }

        //Coin
        for(MapObject object : game.getMap().getLayers().get(7).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            coins.add(new Coin(game, rect.getX() / Constants.PPM, rect.getY() / Constants.PPM , rect.getHeight() / Constants.PPM));
        }

        //TreasureChests
        for(MapObject object : game.getMap().getLayers().get(8).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            treasureChests.add(new TreasureChest(game, rect.getX() / Constants.PPM, rect.getY() / Constants.PPM , rect.getHeight() / Constants.PPM));
        }

        //Ladder
        for(MapObject object : game.getMap().getLayers().get(9).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            game.getBodyFactory().createLadderBody(rect.getX() / Constants.PPM, rect.getY() / Constants.PPM , rect.getWidth() / Constants.PPM, rect.getHeight() / Constants.PPM);
        }
    }

    public Array<Skeleton> getSkeletons() {
        return skeletons;
    }

    public Array<Coin> getCoins() {
        return coins;
    }

    public Array<TreasureChest> getTreasureChests() {
        return treasureChests;
    }
}
