package com.axnshy.cloudmusic.Receiver;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.axnshy.cloudmusic.Config;

/**
 * Created by axnshy on 16/7/31.
 */
public class ToolBarReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //PlayerState代表播放状态
        if (intent.getAction() == Config.MUSIC_UPDATE) {
            // PlayerState = intent.getIntExtra("PlayerState", 0x01);
            /*switch (PlayerService.PlayerState) {
                *//*
                * 1:CIRCLE
                * 2:PREVIOUS
                * 3:PLAY
                * 4:NEXT
                *
                *
                *
                * *//*
                case 1: {
                    mediaPlay.setImageResource(R.drawable.play);
                    //Toast.makeText(context, "music is prepared", Toast.LENGTH_SHORT).show();
                    TopMusicDisplay.setText("");
                    break;
                }
                case 2: {
                    mediaPlay.setImageResource(R.drawable.pause);
                    this.getTopMusicDisplay.setText("正在播放: " + PlayerService.currentMusic.getMusicName());
                    //Toast.makeText(context, "music is playing", Toast.LENGTH_SHORT).show();
                    break;
                }
                case 3: {
                    mediaPlay.setImageResource(R.drawable.play);
                    TopMusicDisplay.setText(" ");
                    //Toast.makeText(context, "music is paused", Toast.LENGTH_SHORT).show();
                    break;

                }
            }*/
        }
    }

}
