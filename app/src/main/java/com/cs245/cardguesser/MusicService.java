/** *************************************************************
 * file: MusicService.java
 * author: Brandon Nguyen, Charly Dang, Colin Koo, Felix Zhang, Gerianna Geminiano
 * class: CS 245 – Programming Graphical User Interface
 *
 * assignment: Android App
 * date last modified: 12/5/17
 *
 * This App is a concentration game. The user is able to select cards to be flipped
 * and also toggle the playback of music. The top 3 high scores from each type of
 * board are also saved
 *
 *************************************************************** */

package com.cs245.cardguesser;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

public class MusicService extends Service{
    private MediaPlayer mp;
    private boolean isToggled;


    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public void onCreate()
    {   //set song to play, takes mp3 and ogg to my knowledge. put songs inside raw folder
        mp = MediaPlayer.create(this, R.raw.starwarsmaintheme);
//        // plays forever
        mp.setLooping(true);

    }

    /*
    method: onStartCommand
    purpose: handles music playback
     */
    public int onStartCommand(Intent intent, int flags, int startId) {



//        if (mp.isPlaying()) {
//            Log.i("onstartcommand", "this is running");

        Bundle extras = intent.getExtras();

        String song = (extras == null) ? " " : extras.getString("song");


        switch (song) {
            case "pause":
                mp.pause();
                break;
            case "resume":
                mp.start();
                break;
            case "game":
                mp.stop();
                mp = MediaPlayer.create(this, R.raw.starwarscantina);
                mp.setLooping(true);
                mp.start();
                break;
            case "main":
                mp.stop();
                mp = MediaPlayer.create(this, R.raw.starwarsmaintheme);
                mp.setLooping(true);
                mp.start();
                break;
            case "hScore":
                mp.stop();
                mp = MediaPlayer.create(this, R.raw.rebelfleet);
                mp.setLooping(true);
                mp.start();
                break;
            default:
                mp.start();
                break;
        }


        return START_NOT_STICKY;
    }


    public void onPause() {

    }


    /*
    method: onDestroy
    purpose: stop music playback if app is destroyed
     */
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
