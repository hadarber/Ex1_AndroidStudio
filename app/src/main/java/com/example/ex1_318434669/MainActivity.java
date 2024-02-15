package com.example.ex1_318434669;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import com.example.ex1_318434669.Logic.GameManager;
import com.google.android.material.imageview.ShapeableImageView;

public class MainActivity extends AppCompatActivity {
    private final GameManager gameManager = new GameManager();
    private AppCompatImageView main_IMG_background;
    private ImageView[][] main_matrix;
    private ShapeableImageView[] life;
    private final int MAX_COLUMN = 3;
    private ImageView[] player_row;
    private final int DELAY_GEN_OBS = 2000;
    private final int DELAY_UPDATE_OBS_ON_MATRIX = 1000;
    int playerIndex = 0;
    boolean hit = false;
    boolean endGame = false;
    private final Handler handler_gen_obs = new Handler();
    private final Handler handler_update_matrix = new Handler();
    private Runnable runnable_upd_mat;
    private Runnable runnable_gen_obs;

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        findViews();

        // generate new obs
        generatingObstacles();
        moveObstacle();
        Glide.with(this).load(R.drawable.background).centerCrop().into(main_IMG_background);
    }

    protected void onPause() {

        super.onPause();
        stopRunnable();

    }
    protected void onDestroy() {

        super.onDestroy();
    }


    private void moveObstacle() {

        runnable_upd_mat = new Runnable() {
            @Override
            public void run() {
                handler_update_matrix.postDelayed(this, DELAY_UPDATE_OBS_ON_MATRIX); //Do it again in a second
                for(int j = 0; j < MAX_COLUMN; j++){
                    for(int i = MAX_COLUMN ; i >= 0 ; i--){
                        if(main_matrix[j][i].getVisibility() == View.VISIBLE) {
                            main_matrix[j][i].setVisibility(ImageView.INVISIBLE);
                            if(i!=MAX_COLUMN){
                                main_matrix[j][i+1].setVisibility(ImageView.VISIBLE);
                            }
                        }
                    }
                }

                ButtonLog();
                playerIndex = gameManager.findPlayer(player_row);
                hit = gameManager.checkHitting(main_matrix[playerIndex][MAX_COLUMN]);
                endGame = gameManager.endGame();

                if(hit){
                    deleteHeart(life);
                    gameManager.deleteHeartManager();
                    vibrate();
                    if(gameManager.life > 0){
                        messageOnHit();
                    }
                }
                if(endGame){
                    messageOnEndGame();
                   // stopRunnable();
                }
            }
        };
        handler_update_matrix.postDelayed(runnable_upd_mat, DELAY_UPDATE_OBS_ON_MATRIX); //Do it again in a second
    }
    private void stopRunnable() {
        handler_update_matrix.removeCallbacks(runnable_upd_mat);
        handler_gen_obs.removeCallbacks(runnable_gen_obs);
    }
    private void vibrate(){
        // Vibrate for 600 milliseconds
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(VibrationEffect.createOneShot(600, VibrationEffect.DEFAULT_AMPLITUDE));
    }
    private void deleteHeart(ShapeableImageView[] hearts){
        for (int i = 0; i < 3; i++) {
            if (hearts[i].getVisibility() == View.VISIBLE) {
                hearts[i].setVisibility(ShapeableImageView.INVISIBLE);
                break;
            }
        }
    }

    private void messageOnHit(){
        Toast toast = Toast.makeText(this,"Be Careful !",Toast.LENGTH_SHORT);
        toast.show();
    }
    private void messageOnEndGame(){
        Toast toast = Toast.makeText(this,"YOU LOSE !",Toast.LENGTH_LONG);
        toast.show();
    }


    private void findViews() {
        main_IMG_background = findViewById(R.id.main_IMG_background);
        ImageView[] col_0 = new ImageView[]{
                findViewById(R.id.image_col_00),
                findViewById(R.id.image_col_10),
                findViewById(R.id.image_col_20),
                findViewById(R.id.image_col_30)};
        ImageView[] col_1 = new ImageView[]{
                findViewById(R.id.image_col_01),
                findViewById(R.id.image_col_11),
                findViewById(R.id.image_col_21),
                findViewById(R.id.image_col_31)};
        ImageView[] col_2 = new ImageView[]{
                findViewById(R.id.image_col_02),
                findViewById(R.id.image_col_12),
                findViewById(R.id.image_col_22),
                findViewById(R.id.image_col_32)};
        player_row = new ImageView[]{
                findViewById(R.id.player_0),
                findViewById(R.id.player_1),
                findViewById(R.id.player_2)};
        life = new ShapeableImageView[]{
                findViewById(R.id.main_IMG_heart1),
                findViewById(R.id.main_IMG_heart2),
                findViewById(R.id.main_IMG_heart3)};

        main_matrix = new ImageView[][]{
                col_0,
                col_1,
                col_2};
    }
    private void generatingObstacles()
    {
        runnable_gen_obs = new Runnable() {
            @Override
            public void run() {
                handler_gen_obs.postDelayed(this, DELAY_GEN_OBS); //Do it again in a second
                int rand = (int) (Math.random() * MAX_COLUMN);
                main_matrix[rand][0].setVisibility(View.VISIBLE);
            }
        };
        handler_gen_obs.postDelayed(runnable_gen_obs, 100); //Do it again in a second
    }

    private void ButtonLog(){
        Button leftButton = findViewById(R.id.main_BTN_left);
        Button rightButton = findViewById(R.id.main_BTN_right);
        leftButton.setOnClickListener(v -> {
            if(player_row[0].getVisibility() == View.VISIBLE){
                player_row[0].setVisibility(View.VISIBLE);
            }
            if(player_row[1].getVisibility() == View.VISIBLE){
                player_row[1].setVisibility(View.INVISIBLE);
                player_row[0].setVisibility(View.VISIBLE);
            }
            if(player_row[2].getVisibility() == View.VISIBLE){
                player_row[2].setVisibility(View.INVISIBLE);
                player_row[1].setVisibility(View.VISIBLE);
            }
        });
        rightButton.setOnClickListener(v -> {
            if(player_row[2].getVisibility() == View.VISIBLE){
                player_row[2].setVisibility(View.VISIBLE);
            }
            if(player_row[1].getVisibility() == View.VISIBLE){
                player_row[1].setVisibility(View.INVISIBLE);
                player_row[2].setVisibility(View.VISIBLE);
            }
            if(player_row[0].getVisibility() == View.VISIBLE){
                player_row[0].setVisibility(View.INVISIBLE);
                player_row[1].setVisibility(View.VISIBLE);
            }
        });
    }




}

