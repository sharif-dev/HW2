package com.example.sensors;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.provider.Settings;

public class AlertReceiver extends BroadcastReceiver {

    MediaPlayer player;

    @Override
    public void onReceive(Context context, Intent intent) {
        player = MediaPlayer.create(context, Settings.System.DEFAULT_RINGTONE_URI);
        player.start();
    }


}
