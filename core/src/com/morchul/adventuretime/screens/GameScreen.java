package com.morchul.adventuretime.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.morchul.adventuretime.AdventureTime;
import com.morchul.adventuretime.Constants;
import com.morchul.adventuretime.Util;
import com.morchul.adventuretime.box2d.BodyFactory;
import com.morchul.adventuretime.box2d.CollisionDetector;
import com.morchul.adventuretime.box2d.MapCreator;
import com.morchul.adventuretime.input.DefaultInputProcessor;
import com.morchul.adventuretime.input.PlayerInputProcessor;
import com.morchul.adventuretime.screens.hud.Hud;
import com.morchul.adventuretime.sprites.Coin;
import com.morchul.adventuretime.sprites.Player;
import com.morchul.adventuretime.sprites.Skeleton;
import com.morchul.adventuretime.sprites.TreasureChest;

public class GameScreen implements Screen {

    private AdventureTime app;

    private OrthographicCamera camera;
    private FitViewport viewport;
    private SpriteBatch batch;

    private TiledMap map;

    private World world;
    private CollisionDetector collisionDetector;
    private BodyFactory bodyFactory;
    private MapCreator mapCreator;

    private Box2DDebugRenderer b2dRenderer;
    private OrthogonalTiledMapRenderer mapRenderer;
    private ShapeRenderer shapeRenderer;

    public Player player;
    private Skeleton boss;

    private Hud hud;

    private boolean finish;
    private String level;

    public GameScreen(AdventureTime app) {
        this.app = app;
    }

    public void setLevel(String level){this.level = level;}

    @Override
    public void show() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, camera);
        camera.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);

        batch = new SpriteBatch();

        map = app.getAssets().loadSingleAsset("map/" + level + ".tmx", TiledMap.class);
        Constants.setTileConstants(map);

        world = new World(new Vector2(0,-15f), true);


        collisionDetector = new CollisionDetector();
        bodyFactory = new BodyFactory(world);
        world.setContactListener(collisionDetector);

        mapCreator = new MapCreator(this);

        b2dRenderer = new Box2DDebugRenderer();
        mapRenderer = new OrthogonalTiledMapRenderer(map, Constants.MPP);
        shapeRenderer = new ShapeRenderer();

        player = new Player(this, 2 * Constants.TILE_WIDTH, 1 * Constants.TILE_HEIGHT, 2 * Constants.TILE_HEIGHT);

        hud = new Hud(this);

        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(new PlayerInputProcessor(player));
        inputMultiplexer.addProcessor(new DefaultInputProcessor(this));
        inputMultiplexer.addProcessor(hud.getStage());

        Gdx.input.setInputProcessor(inputMultiplexer);

        boss = mapCreator.getSkeletons().get(mapCreator.getSkeletons().size - 1);
        finish = false;
        timeAfterFinish = 0;
    }

    private void update(float delta){

        world.step(delta, 6, 2);

        player.update(delta);
        for(Skeleton skeleton : mapCreator.getSkeletons()){
            skeleton.update(delta);
        }
        for(Coin coin : mapCreator.getCoins()){
            coin.update(delta);
        }
        for(TreasureChest chest : mapCreator.getTreasureChests()){
            chest.update(delta);
        }

        camera.position.set(player.getX(), camera.position.y,0);
        camera.update();

        hud.act(delta);
    }

    private float timeAfterFinish;

    @Override
    public void render(float delta) {

        //check game is finish
        if(!finish && boss.isDestroyed()){
            hud.win();
            player.addPoints(player.getLife() * 10);
            app.getSettings().setLevel(Util.updateLevel(level));
            app.getSettings().setPoints(player.getPoints());
            finish = true;
        } else if(!finish && player.isDestroyed()){
            hud.loos();
            finish = true;
        }
        if(finish){
            timeAfterFinish += delta;
            if(timeAfterFinish >= 5){
                app.nextLevel();
                return;
            }
        }

        //update
        update(delta);

        //----------render---------
        //map
        mapRenderer.setView(camera);
        mapRenderer.render();
        //box2d
//        b2dRenderer.render(world, camera.combined);
        //player and skeletons
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        player.draw(batch);
        for(Skeleton skeleton : mapCreator.getSkeletons()){
            skeleton.draw(batch);
        }
        for(Coin coin: mapCreator.getCoins()){
            coin.draw(batch);
        }
        for(TreasureChest chest : mapCreator.getTreasureChests()){
            chest.draw(batch);
        }
        batch.end();
        //ray
//        shapeRenderer.setProjectionMatrix(camera.combined);
//        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
//        for(Skeleton skeleton : mapCreator.getSkeletons()) {
//            shapeRenderer.line(skeleton.skeletonRayStart, skeleton.skeletonRayEnd);
//        }
//        shapeRenderer.setColor(Color.RED);
//        shapeRenderer.end();
        //hud
        hud.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        app.getAssets().unloadSingleAsset("map/" + level + ".tmx");
        batch.dispose();
        world.dispose();
        b2dRenderer.dispose();
        mapRenderer.dispose();
        shapeRenderer.dispose();
        hud.dispose();
        camera = null;
        viewport = null;
        collisionDetector = null;
        bodyFactory = null;
        mapCreator = null;
        player = null;
        boss = null;
    }

    public AdventureTime getApp() {
        return app;
    }

    public World getWorld() {
        return world;
    }

    public FitViewport getViewport() {
        return viewport;
    }

    public TiledMap getMap() {
        return map;
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public BodyFactory getBodyFactory() {
        return bodyFactory;
    }

    public MapCreator getMapCreator() {
        return mapCreator;
    }
}
