package com.morchul.adventuretime;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Disposable;

public class Assets implements Disposable {

    private final AssetManager assetManager;
    private boolean isFinished;

    public Skin defaultSkin;
    public BitmapFont font;

    public TextureAtlas adventurerAtlas;
    public TextureAtlas skeletonAtlas;
    public TextureAtlas coinAtlas;
    public TextureAtlas chestAtlas;

    public Assets() {
        assetManager = new AssetManager();
        isFinished = false;

        preLoad();
        load();
    }

    private void preLoad(){
        assetManager.load("skin/default/uiskin.json", Skin.class);
        assetManager.finishLoading();
        defaultSkin = assetManager.get("skin/default/uiskin.json", Skin.class);
    }

    private void load(){
        FileHandleResolver resolver = new InternalFileHandleResolver();
        assetManager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
        assetManager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));
        assetManager.setLoader(TiledMap.class, new TmxMapLoader(resolver));

        FreetypeFontLoader.FreeTypeFontLoaderParameter font = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        font.fontFileName = "font/font.ttf";
        font.fontParameters.size = 50;

        assetManager.load("animation/Adventurer.txt", TextureAtlas.class);
        assetManager.load("animation/Skeleton.txt", TextureAtlas.class);
        assetManager.load("animation/Coin.txt", TextureAtlas.class);
        assetManager.load("animation/chest.txt", TextureAtlas.class);
        assetManager.load("font.ttf", BitmapFont.class, font);

    }

    public <T> T  loadSingleAsset(String path, Class<T> type){
        assetManager.load(path, type);
        assetManager.finishLoading();
        return assetManager.get(path, type);
    }
    public void unloadSingleAsset(String path){
        assetManager.unload(path);
    }

    private void get(){
        adventurerAtlas = assetManager.get("animation/Adventurer.txt", TextureAtlas.class);
        skeletonAtlas = assetManager.get("animation/Skeleton.txt", TextureAtlas.class);
        coinAtlas = assetManager.get("animation/Coin.txt", TextureAtlas.class);
        chestAtlas = assetManager.get("animation/chest.txt", TextureAtlas.class);
        font = assetManager.get("font.ttf", BitmapFont.class);
    }

    public boolean update(){
        if(assetManager.update()){
            isFinished = true;
            get();
        }
        return isFinished;
    }

    public float getProgress(){
        return assetManager.getProgress();
    }

    public boolean isFinished(){
        return isFinished;
    }

    @Override
    public void dispose() {
        assetManager.dispose();
    }
}
