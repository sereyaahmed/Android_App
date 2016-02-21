package ac.actestapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;


import com.immersion.uhl.Launcher;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;
public class Activity2 extends AppCompatActivity implements TextToSpeech.OnInitListener { //Highway game
    //native void methodname();
    //static{
    //System.loadLibrary("rs3dtest");
    //}
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    public ArrayList<MediaPlayer> car; // sound files for passing cars will be loaded here.
    private static final boolean AUTO_HIDE = true;
    final boolean[] position_right = {true}; // your vehicle position.
    final int[] rand= {3};
    private Launcher m_launcher;

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
    private TextToSpeech tts;
    private View mContentView;
    private View mControlsView;
    private boolean mVisible;
    public String info;
    private static final int SWIPE_DISTANCE_THRESHOLD = 100;
    private static final int SWIPE_VELOCITY_THRESHOLD = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MediaPlayer scrape = MediaPlayer.create(getApplicationContext(),R.raw.scrape); // load scrape sound
        car = new ArrayList<>();
        info="Swipe right and left to avoid incoming cars. Tap and hold to start. Tap twice to go to the menu. Tap once to repeat this message.";

        car.add(MediaPlayer.create(getApplicationContext(), R.raw.car_left));
        car.add(MediaPlayer.create(getApplicationContext(), R.raw.car_right));
        m_launcher =  new Launcher(this);
        tts = new TextToSpeech(this, this);
        tts.setPitch(-8);
        final Random gen = new Random();

//        final int[] counter = {0};
        final CountDownTimer cdt = new CountDownTimer(2000,1500){ // makes random direction car sound, delayed (duration, tick-time).

            @Override
            public void onTick(long millisUntilFinished) {


                car.get(0).seekTo(0);
                car.get(1).seekTo(0);
            }

            @Override
            public void onFinish() {
                if((rand[0]==0 && !position_right[0])||(rand[0]==1&&position_right[0])){ // if crash
                    MediaPlayer.create(getApplicationContext(),R.raw.crash).start(); // boom
                    m_launcher.play(Launcher.EXPLOSION3); // vzt vzt
                    this.cancel();
                }
                else{
                    if(rand[0]!=3)
                car.get(rand[0]).pause();
                    rand[0] =gen.nextInt(2);

                car.get(rand[0]).start(); // random car "direction".
                this.start();
                }

            }


        };

        setContentView(R.layout.activity_2);

        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls_act2);
        mContentView = findViewById(R.id.fullscreen_content_act2);
        // alternative to below ->
        mContentView.setOnTouchListener(new OnSwipeTouchListener(getApplicationContext()) {
            public void onSwipeLeft() {
                //MediaPlayer.create(getApplicationContext(), R.raw.left).start();
                if(position_right[0])
                    position_right[0] =false; // move to the left.
                else MediaPlayer.create(getApplicationContext(),R.raw.scrape).start();
            }
            public void onSwipeRight() {
                //MediaPlayer.create(getApplicationContext(), R.raw.right).start();
                if(!position_right[0])
                    position_right[0] =true; // move to the left.
                else //MediaPlayer.create(getApplicationContext(),R.raw.scrape);
                    MediaPlayer.create(getApplicationContext(),R.raw.scrape).start();
            }

            public void onSwipeTop(){

            }
            public void longPress() {
                cdt.cancel(); // cancel previous game
                MediaPlayer engine =MediaPlayer.create(getApplicationContext(),R.raw.engine_start);
                tts.stop();
                engine.start();
                position_right[0]=true;
                while(engine.isPlaying());
                rand[0]=3;
                cdt.start(); // start game.
            }
            public void onSwipeBottom(){}
            public void onSingleTap() {
                if (!tts.isSpeaking()) {
                    tts.speak(info, TextToSpeech.QUEUE_FLUSH, null);
                } else
                    tts.stop();
            }
            public void onDoubleTap2() { //pause and go to menu
                Intent intent = new Intent(getApplicationContext(), FirstActivity.class);
                    startActivity(intent);
                    cdt.cancel();
                    onPause();}
        });

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mControlsView.setVisibility(View.GONE);
        getSupportActionBar().hide();
        delayedSpeak(100);
    }
    @Override
    protected void onPause(){
        tts.stop();
        super.onPause();
    }

    @Override
    public void onInit(int status) {
        tts.setLanguage(Locale.UK);
    }
    @Override
    public void onDestroy() {
        // Don't forget to shutdown!
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }
    private final Handler mHandler = new Handler();
    private final Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            tts.speak(info, TextToSpeech.QUEUE_FLUSH, null);
        }
    };
    private void delayedSpeak(int delayMillis) {
        mHandler.removeCallbacks(mRunnable);
        mHandler.postDelayed(mRunnable, delayMillis);
    }
}
