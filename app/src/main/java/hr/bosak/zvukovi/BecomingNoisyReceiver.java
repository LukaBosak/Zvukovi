package hr.bosak.zvukovi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.widget.ImageButton;

public class BecomingNoisyReceiver extends BroadcastReceiver {

    MediaPlayer mediaPlayer = MediaPlayerInstance.getInstance().getMediaPlayer();
    PlayActivity playActivity;

    public BecomingNoisyReceiver(PlayActivity playActivity){
        this.playActivity = playActivity;
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        if(AudioManager.ACTION_AUDIO_BECOMING_NOISY.equals(intent.getAction())) {
            mediaPlayer.pause();
            playActivity.playBTN.setImageResource(R.drawable.ic_action_playback_play);

        }
    }
}
