package com.sunrise.interview.interviewspot.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.sunrise.interview.interviewspot.R;
import com.sunrise.interview.interviewspot.util.CGlobal;

import java.io.File;

/**
 * Created by donnv on 7/8/2015.
 */
public class VideoInterviewPreview extends ActionBarActivity {
    private LinearLayout btnBack;
    private LinearLayout btnCancel;
    private Button btnSubmit;
    private ProgressBar progressBarVideoTimeLine;
    private TextView tvVideoTimeLine;
    private TextView tvCompany, tvRole;
    private VideoView myVideoView;
    private ImageButton btnReplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_interview_preview);
        initActionBar();
        initUI();
    }

    public void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(getLayoutInflater().inflate(R.layout.custom_action_bar_video_interview_preview, null),
                new ActionBar.LayoutParams(
                        ActionBar.LayoutParams.MATCH_PARENT,
                        ActionBar.LayoutParams.MATCH_PARENT,
                        Gravity.CENTER
                )
        );
    }

    public void initUI() {
        btnBack = (LinearLayout) findViewById(R.id.ln_back_record_video_interview_preview);
        btnBack.setOnClickListener(new BtnBackOnClickListener());

        btnCancel = (LinearLayout) findViewById(R.id.ln_cancel_record_video_interview_preview);
        btnCancel.setOnClickListener(new BtnCancelOnClickListener());

        btnSubmit = (Button) findViewById(R.id.btn_submit_video_interview_preview);
        btnSubmit.setTransformationMethod(null);
        btnSubmit.setOnClickListener(new BtnSubmitlOnClickListener());

        btnReplay = (ImageButton) findViewById(R.id.btn_replay_video_interview_preview);
        btnReplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!myVideoView.isPlaying()) {
                    myVideoView.start();
                    btnReplay.setVisibility(View.GONE);
                }
            }
        });

        tvCompany = (TextView) findViewById(R.id.tv_company_video_interview_preview);
        tvCompany.setText(CGlobal.jobsNearby.getComapny());

        tvRole = (TextView) findViewById(R.id.tv_role_video_interview_preview);
        tvRole.setText(CGlobal.jobsNearby.getRole());

        myVideoView = (VideoView) findViewById(R.id.ln_body_preview);
        String SrcPath = CGlobal.VIDEO_PREVIEW_PATH;
        myVideoView.setVideoPath(SrcPath);

//        String videoToPlay = "http://bffmedia.com/bigbunny.mp4";
//        Uri videoUri = Uri.parse(videoToPlay);
//        myVideoView.setVideoURI(videoUri);

        myVideoView.requestFocus();
        myVideoView.start();
        btnReplay.setVisibility(View.GONE);

        progressBarVideoTimeLine = (ProgressBar) findViewById(R.id.progress_bar_video_interview_preview);

        tvVideoTimeLine = (TextView) findViewById(R.id.tv_time_video_interview_preview);

        mHandler = new Handler();
        updateProgressBar();

    }

    private void updateUI() {
        int pos = 0, dura = 0;
        if (myVideoView != null) {
            pos = myVideoView.getCurrentPosition();
            dura = myVideoView.getDuration();
        }
        if (!myVideoView.isPlaying()) {
            btnReplay.setVisibility(View.VISIBLE);
        } else {
            btnReplay.setVisibility(View.GONE);
        }
        progressBarVideoTimeLine.setMax(dura);
        progressBarVideoTimeLine.setProgress(pos);
        displayTime(pos, dura);
        // Running this thread after 100 milliseconds
        mHandler.postDelayed(mUpdateTimeTask, 10);
    }

    Handler mHandler;

    public void updateProgressBar() {
        mHandler.postDelayed(mUpdateTimeTask, 10);
    }

    /**
     * Background Runnable thread
     */
    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            updateUI();
        }
    };


    private class BtnBackOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            startRecord();
        }
    }

    private class BtnCancelOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            finish();
        }
    }

    private class BtnSubmitlOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Toast.makeText(getApplicationContext(), "Submit success", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), VideoInterviewRecording.class));
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(mUpdateTimeTask);
    }

    private void displayTime(final int milliseconds, int maxMilliseconds) {
        String str = "";
        int seconds = (int) (milliseconds / 1000) % 60;
        int minutes = (int) ((milliseconds / (1000 * 60)) % 60);
        str = (minutes >= 10 ? minutes : ("0" + minutes)) + ":" + (seconds >= 10 ? seconds : ("0" + seconds));
        seconds = (int) (maxMilliseconds / 1000) % 60;
        minutes = (int) ((maxMilliseconds / (1000 * 60)) % 60);
        str += " /" + (minutes >= 10 ? minutes : ("0" + minutes)) + ":" + (seconds >= 10 ? seconds : ("0" + seconds));
        tvVideoTimeLine.setText(str);
    }

    //Re-record
    public static final int REQUEST_VIDEO_CAPTURED = 101;

    private void startRecord() {
        startActivityForResult(createTakeVideoIntent(), REQUEST_VIDEO_CAPTURED);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_VIDEO_CAPTURED:
                    myVideoView.stopPlayback();
                    myVideoView.setVideoPath(CGlobal.VIDEO_PREVIEW_PATH);
                    myVideoView.start();
                    ;
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Create intent to take video.
     */
    Uri uri;

    private Intent createTakeVideoIntent() {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        uri = getOutputVideoUri();  // create a file to save the video in specific folder
        if (uri != null) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        }
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1); // set the video image quality to high
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 60);
        return intent;
    }

    private Uri getOutputVideoUri() {
        if (Environment.getExternalStorageState() == null) {
            return null;
        }

        File mediaStorage = new File(CGlobal.VIDEO_PREVIEW_PATH);
        if (!mediaStorage.exists() &&
                !mediaStorage.mkdirs()) {
            return null;
        }
        File mediaFile = new File(CGlobal.VIDEO_PREVIEW_PATH);
        return Uri.fromFile(mediaFile);
    }
}
