package com.example.ex1_318434669.Logic;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.imageview.ShapeableImageView;

import java.lang.Math;
public class GameManager {
    public int life = 3;
    public boolean checkHitting(ImageView obs){
        return obs.getVisibility() == View.VISIBLE;
    }

    public void deleteHeartManager(){
        life--;
    }
    public boolean endGame(){
        return life == 0;
    }

    public int findPlayer(ImageView[] player) {
        for (int i = 0; i < 3; i++) {
            if (player[i].getVisibility() == View.VISIBLE) {
                return i;
            }
        }
        return -1;
    }

}