package com.sunrise.interview.interviewspot.util.camera;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sunrise.interview.interviewspot.R;
import com.sunrise.interview.interviewspot.activity.VideoManager;
import com.sunrise.interview.interviewspot.util.CGlobal;
import com.sunrise.interview.interviewspot.util.CUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by donnv on 7/7/2015.
 */
public class VideoCaptureActivity extends ActionBarActivity {
    private Context myContext;
    private boolean hasCamera;
    private boolean onRecording;


    private Camera mCamera;
    private CameraPreview mPreview;
    private MediaRecorder mediaRecorder;
    private boolean cameraFront = false;
    private int cameraId;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_interview_recording);
        initActionBar();
        initUI();
        initialize();
    }

    private void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(getLayoutInflater().inflate(R.layout.custom_action_bar_video_interview_recording, null),
                new ActionBar.LayoutParams(
                        ActionBar.LayoutParams.FILL_PARENT,
                        ActionBar.LayoutParams.MATCH_PARENT,
                        Gravity.CENTER
                )
        );
    }

    private LinearLayout lnCameraPreview;
    private ImageButton btnCapture, btnSwitchCamera, btnPreview;
    private ProgressBar progressBarVideoTimeLine;
    private TextView tvVideoTimeLine, tvCompany_Salary;

    private void initUI() {
        lnCameraPreview = (LinearLayout) findViewById(R.id.ln_body_recording);
        btnCapture = (ImageButton) findViewById(R.id.btn_recording);
        btnSwitchCamera = (ImageButton) findViewById(R.id.btn_change_camera);
        progressBarVideoTimeLine = (ProgressBar) findViewById(R.id.progress_bar_video_interview_recording);
        tvVideoTimeLine = (TextView) findViewById(R.id.tv_time_video_interview_recording);
        tvCompany_Salary = (TextView) findViewById(R.id.tv_company_video_interview_recording);
        btnPreview = (ImageButton) findViewById(R.id.btn_replay_video_interview_record);
    }

    private Timer timer;
    private final static int INTERVAL = 1000;
    private final static int TIMEOUT = 60000;//60s
    private int elapsed;

    public void initialize() {
        CUtil.createDirIfNotExist(CGlobal.VIDEO_HOME_PATH);

        myContext = this;

        mPreview = new CameraPreview(this, cameraId, mCamera);
        lnCameraPreview.addView(mPreview);

        btnCapture.setOnTouchListener(touchListener);

        btnSwitchCamera.setOnClickListener(switchCameraListener);

        progressBarVideoTimeLine.setMax(TIMEOUT);
        progressBarVideoTimeLine.setProgress(0);

        timer = new Timer();
        elapsed = 0;
        displayTimeProgress(elapsed);

        tvCompany_Salary.setText(CGlobal.jobsNearby.getComapny() + " , " + CGlobal.jobsNearby.getRole());

        btnPreview.setOnClickListener(previewListener);
    }

    private void displayTimeProgress(final int milliseconds) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String str = "";
                str += CUtil.convertToTime(milliseconds);
                str += " /" + CUtil.convertToTime(TIMEOUT);
                tvVideoTimeLine.setText(str);
            }
        });
    }

    public void onResume() {
        super.onResume();
        if (!hasCamera(myContext)) {
            Toast.makeText(myContext, "Sorry, your phone does not have a camera!", Toast.LENGTH_LONG).show();
            return;
        }
        initCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // when on Pause, release camera in order to be used from other
        // applications
        releaseCamera();
    }

//    @SuppressLint("SdCardPath")
//    private boolean prepareMediaRecorder() {
//
//        mediaRecorder = new MediaRecorder();
//
//        try {
//            mCamera.unlock();
//        } catch (Exception ex) {
//            return false;
//        }
//
//        try {
//            // adjust the camera the way you need
//            mediaRecorder.setCamera(mCamera);
//
//            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
//            mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
//
//            mediaRecorder.setProfile(CamcorderProfile
//                    .get(CamcorderProfile.QUALITY_LOW));
//
//            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
//
//
//            mCamera = Camera.open();
//            Camera.Parameters params = mCamera.getParameters();
//            List<Camera.Size> sizes = params.getSupportedPictureSizes();
//            // See which sizes the camera supports and choose one of those
//            Camera.Size mSize = sizes.get(0);
//            params.setPictureSize(mSize.width, mSize.height);
//            mCamera.setParameters(params);
//
//            mediaRecorder.setOrientationHint(90);
//        } catch (Exception ex) {
//
//        }
//        lVideoFileFullPath = CGlobal.cVideoFilePath + String.valueOf(System.currentTimeMillis()) + ".3gp";
//        mediaRecorder.setOutputFile(lVideoFileFullPath);
//        mediaRecorder.setMaxDuration(600000); // Set max duration 60 sec.
//        mediaRecorder.setMaxFileSize(50000000); // Set max file size 50M
//
//        try {
//            mediaRecorder.prepare();
//        } catch (IllegalStateException e) {
//            releaseMediaRecorder();
//            return false;
//        } catch (IOException e) {
//            releaseMediaRecorder();
//            return false;
//        }
//        return true;
//    }

    private final int cMaxRecordDurationInMs = 30000;
    private final long cMaxFileSizeInBytes = 5000000;
    private final int cFrameRate = 20;
    private File prRecordedFile;

    @SuppressLint("SdCardPath")
    private boolean prepareMediaRecorder() {
        mediaRecorder = new MediaRecorder();

        try {
            mCamera.unlock();
        } catch (Exception ex) {
            return false;
        }

        try {
            // adjust the camera the way you need
            mediaRecorder.setCamera(mCamera);

            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
            mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
            //
            //set the file output format: 3gp or mp4
            //state: Initialized=>DataSourceConfigured
            String lDisplayMsg = "Current container format: ";
            if (CConstant.puContainerFormat == SettingsDialog.cpu3GP) {
                lDisplayMsg += "3GP\n";
                CGlobal.VIDEO_RECORD_PATH = ".3gp";
                mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            } else if (CConstant.puContainerFormat == SettingsDialog.cpuMP4) {
                lDisplayMsg += "MP4\n";
                CGlobal.VIDEO_RECORD_PATH = ".mp4";
                mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            } else {
                lDisplayMsg += "3GP\n";
                CGlobal.VIDEO_RECORD_PATH = ".3gp";
                mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            }
            //the encoders:
            //audio: AMR-NB
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
            //video: H.263, MP4-SP, or H.264
            //prMediaRecorder.setVideoEncoder(VideoEncoder.H263);
            //prMediaRecorder.setVideoEncoder(VideoEncoder.MPEG_4_SP);
            lDisplayMsg += "Current encoding format: ";
            if (CConstant.puEncodingFormat == SettingsDialog.cpuH263) {
                lDisplayMsg += "H263\n";
                mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H263);
            } else if (CConstant.puEncodingFormat == SettingsDialog.cpuMP4_SP) {
                lDisplayMsg += "MPEG4-SP\n";
                mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);
            } else if (CConstant.puEncodingFormat == SettingsDialog.cpuH264) {
                lDisplayMsg += "H264\n";
                mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
            } else {
                lDisplayMsg += "H263\n";
                mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H263);
            }
            //set resolution
            if (CConstant.puResolutionChoice == SettingsDialog.cpuRes176) {
                mediaRecorder.setVideoSize(176, 144);
            } else if (CConstant.puResolutionChoice == SettingsDialog.cpuRes320) {
                mediaRecorder.setVideoSize(320, 240);
            } else if (CConstant.puResolutionChoice == SettingsDialog.cpuRes720) {
                mediaRecorder.setVideoSize(720, 480);
            }
            Toast.makeText(myContext, lDisplayMsg, Toast.LENGTH_LONG).show();
//            //set camera
//            mCamera = Camera.open();
//            Camera.Parameters params = mCamera.getParameters();
//            List<Camera.Size> sizes = params.getSupportedPictureSizes();
//            // See which sizes the camera supports and choose one of those
//            Camera.Size mSize = sizes.get(0);
//            params.setPictureSize(mSize.width, mSize.height);
//            mCamera.setParameters(params);
            mediaRecorder.setPreviewDisplay(mPreview.getHolder().getSurface());
            try {
                if (cameraFront) {
                    int ot = this.getResources().getConfiguration().orientation;
                    if (ot == Configuration.ORIENTATION_LANDSCAPE) {
                        mediaRecorder.setOrientationHint(180);
                    } else {
                        mediaRecorder.setOrientationHint(270);
                    }
                } else {
                    mediaRecorder.setOrientationHint(90);
                }
            } catch (Exception ex) {
                //
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        //set out-put file
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        CGlobal.VIDEO_RECORD_PATH = CGlobal.VIDEO_HOME_PATH + "VID_" + timeStamp + CGlobal.VIDEO_RECORD_PATH;
        prRecordedFile = new File(CGlobal.VIDEO_RECORD_PATH);
        mediaRecorder.setOutputFile(prRecordedFile.getPath());
        //set max size
        mediaRecorder.setMaxDuration(600000); // Set max duration 60 sec.
        mediaRecorder.setMaxFileSize(50000000); // Set max file size 50M

        try {
            mediaRecorder.prepare();
        } catch (Exception e) {
            releaseMediaRecorder();
            e.printStackTrace();
        }
        return true;
    }

    private void doneRecording() {
        boolean _success = false;
        if (mediaRecorder != null) {
            try {
                mediaRecorder.stop(); // stop the recording
                _success = true;
            } catch (Exception ex) {
                _success = false;
            }
        }
        releaseMediaRecorder(); // release the MediaRecorder object
        timer.cancel();//cancel count_down
        if (_success) {
            Toast.makeText(getApplicationContext(), "Sending...interview video", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), VideoManager.class));
            finish();
        } else {
            Toast.makeText(getApplicationContext(), "Can't record video", Toast.LENGTH_SHORT).show();
            CUtil.deleteRecursive(new File(CGlobal.VIDEO_RECORD_PATH));
            startActivity(new Intent(getApplicationContext(), VideoManager.class));
            finish();
        }
    }

    private void releaseMediaRecorder() {
        if (mediaRecorder != null) {
            mediaRecorder.reset(); // clear recorder configuration
            mediaRecorder.release(); // release the recorder object
            mediaRecorder = null;
            if (mCamera != null) {
                mCamera.lock(); // lock camera for later use
            }
        }
    }

    /**
     * Camera
     */

    private void initCamera() {
        if (mCamera == null) {
            // if the front facing camera does not exist
            if (findFrontFacingCamera() < 0) {
                Toast.makeText(this, "No front facing camera found.", Toast.LENGTH_LONG).show();
                LinearLayout lnSwitchCamera = (LinearLayout) findViewById(R.id.ln_change_camera);
                lnSwitchCamera.setVisibility(View.GONE);

            }
            mCamera = Camera.open(findBackFacingCamera());
            if (mPreview.refreshCamera(mCamera)) {
                btnPreview.setVisibility(View.GONE);
            } else {
                btnPreview.setVisibility(View.VISIBLE);
            }
        }
        onRecording = false;
    }

    private boolean hasCamera(Context context) {
        // check if the device has camera
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            hasCamera = true;
        } else {
            hasCamera = false;
        }
        return hasCamera;
    }

    private int findFrontFacingCamera() {
        int cameraId = -1;
        // Search for the front facing camera
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                cameraId = i;
                cameraFront = true;
                break;
            }
        }
        this.cameraId = cameraId;
        return cameraId;
    }

    private int findBackFacingCamera() {
        int cameraId = -1;
        // Search for the back facing camera
        // get the number of cameras
        int numberOfCameras = Camera.getNumberOfCameras();
        // for every camera check
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                cameraId = i;
                cameraFront = false;
                break;
            }
        }
        this.cameraId = cameraId;
        return cameraId;
    }

    public void switchCamera() {
        // if the camera preview is the front
        if (cameraFront) {
            int cameraId = findBackFacingCamera();
            if (cameraId >= 0) {
                // open the backFacingCamera
                mCamera = Camera.open(cameraId);
                // refresh the preview
                if (mPreview.refreshCamera(mCamera)) {
                    btnPreview.setVisibility(View.GONE);
                } else {
                    btnPreview.setVisibility(View.VISIBLE);
                }
            }
        } else {
            int cameraId = findFrontFacingCamera();
            if (cameraId >= 0) {
                // open the backFacingCamera
                mCamera = Camera.open(cameraId);
                // refresh the preview
                if (mPreview.refreshCamera(mCamera)) {
                    btnPreview.setVisibility(View.GONE);
                } else {
                    btnPreview.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private void releaseCamera() {
        // stop and release camera
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }

    View.OnClickListener switchCameraListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // get the number of cameras
            if (!onRecording) {
                int camerasNumber = Camera.getNumberOfCameras();
                if (camerasNumber > 1) {
                    // release the old camera instance
                    // switch camera, from the front and the back and vice versa
                    releaseCamera();
                    switchCamera();
                } else {
                    Toast.makeText(myContext, "Sorry, your phone has only one camera!", Toast.LENGTH_LONG).show();
                }
            }
        }
    };

    View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (!hasCamera) {
                return false;
            }
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                btnCapture.setPressed(true);
                if (onRecording) {
                    return false;
                }
                if (elapsed >= TIMEOUT) {
                    return false;
                }
                if (!prepareMediaRecorder()) {
                    Toast.makeText(getApplicationContext(), "Fail in prepareMediaRecorder()!\n - Ended -", Toast.LENGTH_LONG).show();
                }
                // work on UiThread for better performance
                runOnUiThread(new Runnable() {
                    public void run() {
                        // If there are stories, add them to the table
                        try {
                            mediaRecorder.start();
                            onRecording = true;
                            TimerTask task = new TimerTask() {
                                @Override
                                public void run() {
                                    elapsed += INTERVAL;
                                    progressBarVideoTimeLine.setProgress(elapsed);
                                    displayTimeProgress(elapsed);
                                    if (elapsed >= TIMEOUT) {
                                        doneRecording();
                                        return;
                                    }
                                    //if(some other conditions)
                                    //   this.cancel();
                                }
                            };
                            timer.scheduleAtFixedRate(task, INTERVAL, INTERVAL);

                        } catch (final Exception ex) {
                            // Log.i("---","Exception in thread");
                        }
                    }
                });
                return true;
            } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                btnCapture.setPressed(false);
                if (onRecording) {
                    doneRecording();
                }
                return true;
            } else {
                return false;
            }
        }
    };

    View.OnClickListener previewListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mPreview.refreshCamera(mCamera)) {
                btnPreview.setVisibility(View.GONE);
            } else {
                btnPreview.setVisibility(View.VISIBLE);
            }
        }
    };
}
