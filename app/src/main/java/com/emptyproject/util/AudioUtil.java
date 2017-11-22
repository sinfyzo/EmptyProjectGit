package com.emptyproject.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.datecoy.R;
import com.datecoy.listioner.CommonListioner;
import com.datecoy.model.UserNew;
import com.datecoy.webapi.WebUtil;

import java.io.IOException;

/**
 * Created by INFYZO\shailesh.pateliya on 30/6/17.
 */

public class AudioUtil implements WebUtil {
    public static AudioUtil audioUtil;
    public static Activity mactivity;
    SeekBar seekBar;
    Dialog audioDialog;

    MediaPlayer mediaPlayer;
    TextView txtStart, txtEnd, tapView;
    ImageView playButton, uploadAudio, record, cancel;
    LinearLayout playsend, r1;
    //For Audio
    private Handler mHandler = new Handler();
    boolean isAudioViewed = false;
    public static AudioUtil getInstance(Activity activity) {
        mactivity = activity;
        if (audioUtil == null)
            audioUtil = new AudioUtil();
        return audioUtil;
    }
    public void showAudioDialog(final MediaPlayer mediaPlayer1, final String filePath, final UserNew user, final CommonListioner listner, final int profileType){
        isAudioViewed = false;
        audioDialog = new Dialog(mactivity);
        audioDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        audioDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        audioDialog.setContentView(R.layout.popup_audio_record);
        tapView = (TextView) audioDialog.findViewById(R.id.tapview);
        tapView.setVisibility(View.GONE);
        txtStart = (TextView) audioDialog.findViewById(R.id.textView2);
        txtEnd = (TextView) audioDialog.findViewById(R.id.textView3);
        seekBar = (SeekBar) audioDialog.findViewById(R.id.sbar);
        playsend = (LinearLayout) audioDialog.findViewById(R.id.playsend);
        r1 = (LinearLayout) audioDialog.findViewById(R.id.r1);
        playsend.setVisibility(View.VISIBLE);
        r1.setVisibility(View.VISIBLE);
        cancel = (ImageView) audioDialog.findViewById(R.id.cancel);
        record = (ImageView) audioDialog.findViewById(R.id.record);
        record.setVisibility(View.GONE);
        playButton = (ImageView) audioDialog.findViewById(R.id.play);
        uploadAudio = (ImageView) audioDialog.findViewById(R.id.send);
        uploadAudio.setVisibility(View.GONE);
        // Listeners
        mediaPlayer = new MediaPlayer();
        seekBar.setOnSeekBarChangeListener(onMPSeekBarChangeListenerForDialog);
        mediaPlayer.setOnCompletionListener(onMPCompletionListenerForDialog);
        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                clearAudioSetData();
                return false;
            }
        });


        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    if (mediaPlayer != null) {
                        mediaPlayer.pause();
                        playButton.setImageResource(R.drawable.ic_profile_media_audio_play_dialog);
                    }
                } else {
                    // Resume song
                    if (mediaPlayer != null) {
                        playButton.setImageResource(R.drawable.ic_profile_media_audio_pause_dialog);
                        playAudio();

                    }
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                audioDialog.dismiss();
                clearAudioSetData();
            }
        });

        audioDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                clearAudioSetData();
            }
        });
        audioDialog.show();

        if(!isAudioViewed && profileType != PROFILE_TYPE_SELF){
            isAudioViewed = true;
            ProfileActions.getInstance(mactivity).notifyMediaView(user, listner, profileType, MEDIA_AUDIO,"");
        }
        if(null == mediaPlayer)
            mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(filePath);
            mediaPlayer.prepare();
            mediaPlayer.start();
            playButton.setImageResource(R.drawable.ic_profile_media_audio_pause_dialog);
            seekBar.setProgress(0);
            seekBar.setMax(100);
            updateProgressBarForDialog();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    SeekBar.OnSeekBarChangeListener onMPSeekBarChangeListenerForDialog = new SeekBar.OnSeekBarChangeListener(){

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            // remove message Handler from updating progress bar
            mHandler.removeCallbacks(mUpdateTimeTaskForDialog);
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            mHandler.removeCallbacks(mUpdateTimeTaskForDialog);
            int totalDuration = mediaPlayer.getDuration();
            int currentPosition = Util.getInstance(mactivity).progressToTimer(seekBar.getProgress(), totalDuration);

            // forward or backward to certain seconds
            mediaPlayer.seekTo(currentPosition);
            // update timer progress again
            updateProgressBarForDialog();
        }
    };

    MediaPlayer.OnCompletionListener onMPCompletionListenerForDialog = new MediaPlayer.OnCompletionListener(){

        @Override
        public void onCompletion(MediaPlayer mp) {
            //        mp.release();
            playButton.setImageResource(R.drawable.ic_profile_media_audio_play_dialog);
        }
    };
    /**
     * Function to play a song
     */
    public void playAudio() {
        // Play song
        try {

            mediaPlayer.start();
            // Displaying Song title
            // Changing Button Image to pause image
            playButton.setImageResource(R.drawable.ic_profile_media_audio_pause_dialog);

            // set Progress bar values
            seekBar.setProgress(0);
            seekBar.setMax(100);

            // Updating progress bar
            updateProgressBarForDialog();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * Update timer on seekbar
     */
    public void updateProgressBarForDialog() {
        mHandler.postDelayed(mUpdateTimeTaskForDialog, 100);
    }

    /**
     * Background Runnable thread
     */
    private Runnable mUpdateTimeTaskForDialog = new Runnable() {
        public void run() {
            long totalDuration = mediaPlayer.getDuration();
            long currentDuration = mediaPlayer.getCurrentPosition();

            // Displaying Total Duration time
            txtEnd.setText("" + Util.getInstance(mactivity).milliSecondsToTimer(totalDuration));
            // Displaying time completed playing
            txtStart.setText("" + Util.getInstance(mactivity).milliSecondsToTimer(currentDuration));

            // Updating progress bar
            int progress = Util.getInstance(mactivity).getProgressPercentage(currentDuration, totalDuration);
            //Log.d("Progress", ""+progress);
            seekBar.setProgress(progress);

            // Running this thread after 100 milliseconds
            mHandler.postDelayed(this, 100);
        }
    };
    public void clearAudioSetData() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
        if (txtEnd != null) {
            txtEnd.setText("");
        }
        if (txtStart != null) {
            txtStart.setText("");
        }
        if (seekBar != null) {
            seekBar.setProgress(0);
        }
        Util.getInstance(mactivity).seconds = 0;
        Util.getInstance(mactivity).minute = 0;
        Util.getInstance(mactivity).hour = 0;
    }
}
