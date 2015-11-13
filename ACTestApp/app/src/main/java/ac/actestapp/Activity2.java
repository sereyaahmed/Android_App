package ac.actestapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import java.util.ArrayList;
import java.util.Random;
/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class Activity2 extends AppCompatActivity { //Highway game
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    public ArrayList<MediaPlayer> car; // sound files for passing cars will be loaded here.
    private static final boolean AUTO_HIDE = true;
    final boolean[] position_right = {true}; // your vehicle position.
    final int[] rand = {0};
    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;

    private View mContentView;
    private View mControlsView;
    private boolean mVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MediaPlayer scrape = MediaPlayer.create(getApplicationContext(),R.raw.scrape); // load scrape sound
        car = new ArrayList<>();
        //car.add(MediaPlayer.create(getApplicationContext(), R.raw.car_close_left));
        //car.add(MediaPlayer.create(getApplicationContext(), R.raw.car_close_right));
        car.add(MediaPlayer.create(getApplicationContext(), R.raw.car_left));
        car.add(MediaPlayer.create(getApplicationContext(), R.raw.car_right));

        final Random gen = new Random();


        final CountDownTimer cdt = new CountDownTimer(1500,1500){ // makes random direction car sound, delayed (duration, tick-time).

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                if((rand[0]==0 && !position_right[0])||(rand[0]==1&&position_right[0])){
                    MediaPlayer.create(getApplicationContext(),R.raw.crash).start();
                    this.cancel();
                }
                else{
                rand[0] =gen.nextInt(2);
                car.get(rand[0]).start(); // random car "direction".
                this.start();}
            }


        };

        setContentView(R.layout.activity_2);

        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls_act2);
        mContentView = findViewById(R.id.fullscreen_content_act2);

        mContentView.setOnTouchListener(new OnSwipeTouchListener(getApplicationContext()) {
            @Override
            public void onSwipeLeft() {
                //MediaPlayer.create(getApplicationContext(), R.raw.left).start();
                if(position_right[0])
                position_right[0] =false; // move to the left.
                else MediaPlayer.create(getApplicationContext(),R.raw.scrape).start();
            }
            @Override
            public void onSwipeRight() {
                //MediaPlayer.create(getApplicationContext(), R.raw.right).start();
                if(!position_right[0])
                    position_right[0] =true; // move to the left.
                else //MediaPlayer.create(getApplicationContext(),R.raw.scrape);
                    MediaPlayer.create(getApplicationContext(),R.raw.scrape).start();
            }
            @Override
            public void onSwipeTop(){
                cdt.cancel(); // cancel previous game
                MediaPlayer.create(getApplicationContext(),R.raw.engine_start).start();
                rand[0]=0;
                position_right[0]=true;
               cdt.start(); // start game.
            }
            @Override
            public void onSwipeBottom(){
                Intent intent = new Intent(getApplicationContext(), FirstActivity.class);
                startActivity(intent);
                cdt.cancel();
                onPause();
                            }
            @Override
            public void onClick() {
                show();
            }
        });

        // Set up the user interaction to manually show or hide the system UI.


        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        findViewById(R.id.highway_button).setOnTouchListener(mDelayHideTouchListener3);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener3 = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {

            Intent intent = new Intent(getApplicationContext(), FirstActivity.class);
            startActivity(intent);
            return false;
        }
    };

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);


        }
    };

    private final Handler mHideHandler = new Handler();
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }
}
