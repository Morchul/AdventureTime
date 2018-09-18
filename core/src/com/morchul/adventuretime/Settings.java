package com.morchul.adventuretime;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class Settings {

    private Preferences settings;

    private final static String LEVEL_KEY = "level";
    private final static String LEVEL_DEFAULT = "level1-1";

    private final static String POINT_KEY = "points";
    private final static int POINT_DEFAULT = 0;

    public Settings() {
        settings = Gdx.app.getPreferences("game-prefs");
    }

    public void setPoints(int points){
        settings.putInteger(POINT_KEY, points);
        settings.flush();
    }

    public int getPoints(){
        return settings.getInteger(POINT_KEY, POINT_DEFAULT);
    }

    public void setLevel(String level){
        settings.putString(LEVEL_KEY, level);
        settings.flush();
    }

    public String getLevel(){
        return settings.getString(LEVEL_KEY, LEVEL_DEFAULT);
    }
}
