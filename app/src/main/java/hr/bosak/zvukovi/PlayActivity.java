package hr.bosak.zvukovi;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.session.MediaSession;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class PlayActivity extends AppCompatActivity implements OnProgressChangeListener {

    LinearLayout playActivityLL;
    ImageButton playBTN;
    ImageButton forwardBTN;
    ImageButton backwardBTN;
    ImageButton stopBTN;
    ImageButton shuffleBTN;

    Intent intent;

    MediaPlayer mediaPlayer;
    TextView songInfoTV;

    HorizontalSlider seekSlider;
    TextView progressTV;

    SeekBar volumeSeekbar;
    AudioManager audioManager;


    ArrayList<Audio> audioList;

    int audioIndex = 0;
    int currPosition = 0;

    boolean isStopped;
    boolean isForward;

    String info;

    public static final String INDEX = "index data";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);


        getSupportActionBar();
        initWidgets();
        loadAudio();

        setUpPlay();

        setMediaPlayerListener();

        setVolume();

        startSong();

    }


    @Override
    public void finish() {
        int index = audioIndex;
        Intent data = new Intent();
        data.putExtra(INDEX, index);
        setResult(RESULT_OK, data);
        super.finish();

    }

    public void initWidgets(){
        playActivityLL = findViewById(R.id.playActivityLL);

        playBTN = findViewById(R.id.playBTN);
        forwardBTN = findViewById(R.id.forwardBTN);
        backwardBTN = findViewById(R.id.backwardBTN);
        stopBTN = findViewById(R.id.stopBTN);
        shuffleBTN = findViewById(R.id.shuffleBTN);

        seekSlider = findViewById(R.id.seekSlider);
        progressTV = findViewById(R.id.progressTV);
        songInfoTV = findViewById(R.id.songInfoTV);

    }


    private void loadAudio(){

        ContentResolver contentResolver = getContentResolver();

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        String selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0";
        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";

        Cursor cursor = contentResolver.query(uri, null, selection, null, sortOrder);

        if(cursor != null && cursor.getCount() > 0){
            audioList = new ArrayList<>();

            while (cursor.moveToNext()) {
                String data = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                audioList.add(new Audio(data, title, album, artist));
            }
        }
        cursor.close();

    }

    public void setUpPlay(){

        mediaPlayer = MediaPlayerInstance.getInstance().getMediaPlayer();
        intent = getIntent();

        if(intent.hasExtra(SongFragment.COVER)){
            playActivityLL.setBackgroundResource(R.drawable.cover_4);
        } else if(intent.hasExtra(SongFragment.COVER2)){
            playActivityLL.setBackgroundResource(R.drawable.cover_8);
        }

        playBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isForward = false;
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    playBTN.setImageResource(R.drawable.ic_action_playback_play);
                } else if (!(mediaPlayer.isPlaying())) {
                    mediaPlayer.start();
                    playBTN.setImageResource(R.drawable.ic_action_playback_pause);
                }
            }
        });

        forwardBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                audioIndex = audioIndex + 1;

                if(audioIndex == audioList.size()){
                    audioIndex = 0;
                }

                initMediaPlayer();

                if(isForward == true) {
                    playBTN.setImageResource(R.drawable.ic_action_playback_play);
                } else {
                    mediaPlayer.start();
                    playBTN.setImageResource(R.drawable.ic_action_playback_pause);
                }
            }
        });

        backwardBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                audioIndex = audioIndex - 1;

                if (audioIndex < 0){
                    audioIndex = audioList.size() - 1;
                }

                initMediaPlayer();

                if(isForward == true) {
                    playBTN.setImageResource(R.drawable.ic_action_playback_play);
                } else {
                    mediaPlayer.start();
                    playBTN.setImageResource(R.drawable.ic_action_playback_pause);
                }
            }
        });

        stopBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isStopped = true;
                initMediaPlayer();
                isStopped = false;
                isForward = true;
                playBTN.setImageResource(R.drawable.ic_action_playback_play);
            }
        });

        shuffleBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Random random = new Random();
                int randomIndex = random.nextInt(audioList.size());
                audioIndex = randomIndex;
                initMediaPlayer();
                mediaPlayer.start();
                playBTN.setImageResource(R.drawable.ic_action_playback_pause);
            }
        });


        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
             if(!(mediaPlayer.isPlaying()) && !isForward) {
                 audioIndex = audioIndex + 1;
                 initMediaPlayer();
                 mediaPlayer.start();
             }

            }
        });

        PhoneStateListener phoneStateListener = new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                if (state == TelephonyManager.CALL_STATE_RINGING) {
                    mediaPlayer.pause();
                    playBTN.setImageResource(R.drawable.ic_action_playback_play);
                } else if(state == TelephonyManager.CALL_STATE_IDLE) {
                    //Not in call: Play music
                } else if(state == TelephonyManager.CALL_STATE_OFFHOOK) {
                    mediaPlayer.pause();
                    playBTN.setImageResource(R.drawable.ic_action_playback_play);
                }
                super.onCallStateChanged(state, incomingNumber);
            }
        };
        TelephonyManager mgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        if(mgr != null) {
            mgr.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        }
        else if(mgr == null) {
            mgr.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_menu2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()== R.id.action_cancel){
            mediaPlayer.stop();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onProgressChange(View view, int progress) {
        int songDuration = mediaPlayer.getDuration();
        int position = (int)( progress / 100f * songDuration);
        mediaPlayer.seekTo(position);


    }

    public void setMediaPlayerListener(){
        seekSlider.setListener(this);
        final Handler handler = new Handler();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    final int currentPosition = mediaPlayer.getCurrentPosition();

                    handler.post(new Runnable() {
                        @Override
                        public void run() {

                            int progress = (int)((float) currentPosition/ mediaPlayer.getDuration() * 100);
                            int secPosition = currentPosition/1000;
                            int progressTimeMin = secPosition /60;
                            int progressTimeSec = secPosition%60;
                            String currentProgress = String.format("%02d:%02d",progressTimeMin,progressTimeSec);
                            int duration = mediaPlayer.getDuration()/1000;
                            int durationMin = duration/60;
                            int durationSec = duration%60;
                            String durationTotal = String.format("%02d:%02d",durationMin, durationSec);
                            progressTV.setText(currentProgress + " / " + durationTotal);
                            seekSlider.setProgress(progress);
                        }
                    });
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
       thread.start();
    }

    public void setVolume() {

        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        try {
            volumeSeekbar = (SeekBar) findViewById(R.id.volumeSeekbar);
            audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            volumeSeekbar.setMax(audioManager
                    .getStreamMaxVolume(AudioManager.STREAM_MUSIC));
            volumeSeekbar.setProgress(audioManager
                    .getStreamVolume(AudioManager.STREAM_MUSIC));



            volumeSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onStopTrackingTouch(SeekBar arg0) {
                }

                @Override
                public void onStartTrackingTouch(SeekBar arg0) {
                }

                @Override
                public void onProgressChanged(SeekBar arg0, int progress, boolean arg2) {
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                            progress, 0);

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void startSong()  {

        intent = getIntent();

        if (intent.hasExtra((MainActivity.COVER_4))){
            playActivityLL.setBackgroundResource(R.drawable.cover_4);
        }
        else if(intent.hasExtra(MainActivity.COVER_8)){
            playActivityLL.setBackgroundResource(R.drawable.cover_8);
        }

        if(intent.hasExtra(MainActivity.SEND_INDEX) && intent.hasExtra(MainActivity.SEND_CURRENT)) {
            mediaPlayer.stop();
            mediaPlayer.reset();
            audioIndex = intent.getIntExtra(MainActivity.SEND_INDEX, 0);
            currPosition = intent.getIntExtra(MainActivity.SEND_CURRENT, 0);

            mediaPlayer = MediaPlayerInstance.getInstance().getMediaPlayer();
            initMediaPlayer();
            playBTN.setImageResource(R.drawable.ic_action_playback_pause);
            mediaPlayer.seekTo(currPosition);
            mediaPlayer.start();
            if (intent.hasExtra((MainActivity.COVER_4))){
                playActivityLL.setBackgroundResource(R.drawable.cover_4);
            } else if(intent.hasExtra(MainActivity.COVER_8)){
                playActivityLL.setBackgroundResource(R.drawable.cover_8);
            }

        }


        if(intent.hasExtra(MainActivity.POSITION)){

            if (mediaPlayer.isPlaying()){
                mediaPlayer.stop();
                mediaPlayer.reset();
            }

            audioIndex = intent.getIntExtra(MainActivity.POSITION,0);
            initMediaPlayer();
            playBTN.setImageResource(R.drawable.ic_action_playback_pause);
            mediaPlayer.start();

        }
    }


    public void initMediaPlayer(){
        mediaPlayer.stop();
        mediaPlayer.reset();

        try {
            mediaPlayer.setDataSource(audioList.get(audioIndex).getData());
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        info = audioList.get(audioIndex).getArtist() + " - " + audioList.get(audioIndex).getTitle();
        songInfoTV.setText(info);

        int index = audioIndex;
        Intent data = new Intent();
        data.putExtra(INDEX, index);
        setResult(RESULT_OK, data);

    }



}
