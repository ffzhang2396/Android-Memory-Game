package com.cs245.cardguesser;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by Brandon on 11/19/2017.
 */

public class MusicService extends Service{
    private MediaPlayer mp;
    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public void onCreate()
    {   //set song to play, takes mp3 and ogg to my knowledge. put songs inside raw folder
        mp = MediaPlayer.create(this, R.raw.starwarsmaintheme);
        // plays forever
        mp.setLooping(true);
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        mp.start();
        return START_NOT_STICKY;
    }
    @Override
    public void onDestroy()
    {
        if(mp != null)  {
            mp.release();
            mp = null;
            super.onDestroy();
        }
    }

    public void togglePlayBack(){
        if (mp.isPlaying()) {
            mp.stop();
        }
        else {
            mp.start();
        }
    }


}
