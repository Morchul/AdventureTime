package com.morchul.adventuretime;

import com.badlogic.gdx.maps.tiled.TiledMap;

public class Constants {

    public static final float PPM = 32;
    public static final float MPP = 1 / PPM;

    public static final int WORLD_PIXEL_WIDTH = 512;
    public static final int WORLD_PIXEL_HEIGHT = 256;
    public static final float WORLD_WIDTH = WORLD_PIXEL_WIDTH / PPM; //in meter
    public static final float WORLD_HEIGHT = WORLD_PIXEL_HEIGHT / PPM; //in meter

    public final static int MAX_LEVEL = 13;

    //can Access after start game
    public static int TILE_PIXEL_WIDTH;
    public static int TILE_PIXEL_HEIGHT;
    public static float TILE_WIDTH;
    public static float TILE_HEIGHT;
    public static float TPM;

    public static void setTileConstants(TiledMap map){
        TILE_PIXEL_WIDTH = map.getProperties().get("tilewidth", Integer.class);
        TILE_PIXEL_HEIGHT = map.getProperties().get("tileheight", Integer.class);
        TILE_WIDTH = TILE_PIXEL_WIDTH / PPM;
        TILE_HEIGHT = TILE_PIXEL_HEIGHT / PPM;
        TPM = 1 / TILE_WIDTH;
    }
}
