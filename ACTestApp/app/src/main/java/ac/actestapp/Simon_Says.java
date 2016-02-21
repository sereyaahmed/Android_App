package ac.actestapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
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


public class Simon_Says extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private static final int UI_ANIMATION_DELAY = 300;

    private View mContentView;
    private View mControlsView;
    private boolean mVisible;
    ArrayList<MediaPlayer> directions;
    ArrayList<MediaPlayer> piano;
    ArrayList<Integer> gameSteps;
    int playerPos;
    MediaPlayer info;
    int score;
    public boolean isPlaying;
    private TextToSpeech tts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        directions=new ArrayList<>();
        piano=new ArrayList<>();
        gameSteps= new ArrayList<>();
        playerPos=0;
        isPlaying=false;
        tts = new TextToSpeech(this, this);
        tts.setPitch(-8);
        setContentView(R.layout.activity_simon__says);
        score=0;
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

                piano.get(3).seekTo(0);
               piano.get(3).start();
                if(gameSteps.size()!=0)
                if(!verifyStep(3)){

                    tts.speak("Game over. your score is "+score, TextToSpeech.QUEUE_FLUSH, null);}
            }

            @Override
            public void onSwipeRight() {

                piano.get(1).seekTo(0);
                piano.get(1).start();
                if(gameSteps.size()!=0)
                    if(!verifyStep(1)){

                        tts.speak("Game over. your score is "+score, TextToSpeech.QUEUE_FLUSH, null);}

            }

            @Override
            public void onSwipeTop() {

                piano.get(0).seekTo(0);
                piano.get(0).start();
                if(gameSteps.size()!=0)
                    if(!verifyStep(0)){

                tts.speak("Game over. your score is "+score, TextToSpeech.QUEUE_FLUSH, null); }
            }

            @Override
            public void onSwipeBottom() {

                    piano.get(2).seekTo(0);
                    piano.get(2).start();
                if(gameSteps.size()!=0)
                    if(!verifyStep(2)) {
                       tts.speak("Game over. your score is "+score, TextToSpeech.QUEUE_FLUSH, null);
                    }

            }
            public void onSingleTap(){
                if(!info.isPlaying()){
                    info.seekTo(0);
                    info.start();}
                else
                    info.pause();
            }
            public void onDoubleTap2(){
                Intent intent = new Intent(getApplicationContext(), FirstActivity.class);
                startActivity(intent);
                onPause();
            info.stop();}
            public void longPress(){
                info.seekTo(0);
                if(!isPlaying)
                {gameSteps= new ArrayList<>();
                    start();}

            }
        });

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
      //  MediaPlayer.create(getApplicationContext(),R.raw.highway_info);
        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
       // delayedHide(100);
        mControlsView.setVisibility(View.GONE);
        getSupportActionBar().hide();
    }

    public boolean verifyStep(int x) {
//        if(playerPos<gameSteps.size()){
        boolean ret;
        if(gameSteps.get(playerPos)==x){  playerPos++; ret= true;}
        else{playerPos=0; score=gameSteps.size()-1; gameSteps=new ArrayList<>(); ret= false;}
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
                piano.get(gameSteps.get(i)).seekTo(0);
                piano.get(gameSteps.get(i)).start();
            while(directions.get(gameSteps.get(i)).isPlaying());
            }
    isPlaying=false;
    }

    @Override
    public void onInit(int status) {
        tts.setLanguage(Locale.US);
    }
    @Override
    public void onDestroy() {
        // Don't forget to shutdown!
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        info.stop();
        super.onDestroy();
    }
}
