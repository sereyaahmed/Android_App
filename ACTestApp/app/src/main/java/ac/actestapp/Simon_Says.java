package ac.actestapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.immersion.uhl.Launcher;

import java.util.ArrayList;
import java.util.Random;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class Simon_Says extends AppCompatActivity {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

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
    ArrayList<MediaPlayer> directions;
    ArrayList<MediaPlayer> piano;
    ArrayList<Integer> gameSteps;
    int playerPos;
    MediaPlayer info;
    public boolean isPlaying;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        directions=new ArrayList<>();
        piano=new ArrayList<>();
        gameSteps= new ArrayList<>();
        playerPos=0;
        isPlaying=false;
        setContentView(R.layout.activity_simon__says);
         info = new MediaPlayer().create(getApplicationContext(),R.raw.simon_info);
        directions.add(MediaPlayer.create(getApplicationContext(), R.raw.simon_up));
        directions.add(MediaPlayer.create(getApplicationContext(),R.raw.simon_right));
        directions.add(MediaPlayer.create(getApplicationContext(),R.raw.simon_down));
        directions.add(MediaPlayer.create(getApplicationContext(),R.raw.simon_left));
        piano.add(MediaPlayer.create(getApplicationContext(),R.raw.piano_up));
        piano.add(MediaPlayer.create(getApplicationContext(),R.raw.piano_right));
        piano.add(MediaPlayer.create(getApplicationContext(),R.raw.piano_down));
        piano.add(MediaPlayer.create(getApplicationContext(),R.raw.piano_left));
        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.fullscreen_content);
        info.start();
        mContentView.setOnTouchListener(new OnSwipeTouchListener(getApplicationContext()) {
            @Override
            public void onSwipeLeft() {
                if(!isPlaying){
                piano.get(3).seekTo(0);
               piano.get(3).start();
                verifyStep(3);}

            }

            @Override
            public void onSwipeRight() {
                if(!isPlaying){
                piano.get(1).seekTo(0);
                piano.get(1).start();
                verifyStep(1);}

            }

            @Override
            public void onSwipeTop() {
                if(!isPlaying){
                piano.get(0).seekTo(0);
                piano.get(0).start();
                verifyStep(0);}
            }

            @Override
            public void onSwipeBottom() {
                if(!isPlaying) {
                    piano.get(2).seekTo(0);
                    piano.get(2).start();
                    verifyStep(2);
                }
            }
            public void onSingleTap(){
              info.seekTo(0);
                info.start();}
            public void onDoubleTap2(){
                Intent intent = new Intent(getApplicationContext(), FirstActivity.class);
                startActivity(intent);
                onPause();
            info.stop();}
            public void longPress(){
                info.stop();
                if(!isPlaying)
                    start();
            }
//            @Override
//            public void onClick() {
//                MediaPlayer.create(getApplicationContext(), R.raw.scrape).start();
//            }

        });

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
      //  MediaPlayer.create(getApplicationContext(),R.raw.highway_info);
        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    public boolean verifyStep(int x) {
//        if(playerPos<gameSteps.size()){
        boolean ret;
        if(gameSteps.get(playerPos)==x){  playerPos++; ret= true;}
        else{playerPos=0; gameSteps=new ArrayList<>(); ret= false;}
        if(playerPos==gameSteps.size() && ret){ start(); playerPos=0;}
        return ret;
    }
    public void start() {
        while(piano.get(0).isPlaying() ||piano.get(1).isPlaying() || piano.get(2).isPlaying() || piano.get(3).isPlaying());
        isPlaying=true;
        Random gen=new Random();
            gameSteps.add(gen.nextInt(4));
            for (int i=0;i < gameSteps.size(); i++) {
                directions.get(gameSteps.get(i)).start();
                piano.get(gameSteps.get(i)).start();
           while(piano.get(gameSteps.get(i)).isPlaying());
            }
    isPlaying=false;
    }
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
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
