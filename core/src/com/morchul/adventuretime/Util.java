package com.morchul.adventuretime;

import com.badlogic.gdx.utils.Array;
import com.morchul.adventuretime.buffs.Buf;

public class Util {

    public static String updateLevel(final String level){

        final String f = level.substring(0, 5);
        int number1 = Integer.parseInt(level.substring(7,8));
        int number2 = Integer.parseInt(level.substring(5,6));
        if(number1 == 9){
            number1 = 1;
            ++number2;
        } else
            ++number1;
        String res = f + number2 + "-" + number1;
        if(getLevel(res) > Constants.MAX_LEVEL){
            return "level1-0";
        } else
            return res;
    }

    public static int getLevel(final String level){
        int number1 = Integer.parseInt(level.substring(7,8));
        int number2 = Integer.parseInt(level.substring(5,6));
        return number1 + number2 * 10;
    }

    public static boolean hasBuff(Array<Buf> buffs, Buf buf){
        for(Buf b: buffs){
            if(b == buf) return true;
        }
        return false;
    }
}
