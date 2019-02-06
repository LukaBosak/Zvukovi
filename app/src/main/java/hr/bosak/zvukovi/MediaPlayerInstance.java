package hr.bosak.zvukovi;

import android.media.MediaPlayer;

public class MediaPlayerInstance {

    private static MediaPlayerInstance instance = null;

    private MediaPlayer mediaPlayer;

    public static MediaPlayerInstance getInstance(){
        if(instance == null){
            instance = new MediaPlayerInstance();
        }
        return instance;
    }


    private MediaPlayerInstance(){
        mediaPlayer = new MediaPlayer();
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }
}
