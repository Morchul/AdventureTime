package com.morchul.adventuretime.buffs;

public enum Buf{

    INCREASE_DAMAGE(5),
    IMMORTAL(7);


    public float currentTime;
    private final float time;

    Buf(float time){
        this.currentTime = time;
        this.time = time;
    }

    public void reset(){
        currentTime = time;
    }
}
