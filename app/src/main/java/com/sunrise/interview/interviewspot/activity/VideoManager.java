package com.sunrise.interview.interviewspot.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sunrise.interview.interviewspot.R;
import com.sunrise.interview.interviewspot.util.CGlobal;
import com.sunrise.interview.interviewspot.util.CUtil;
import com.sunrise.interview.interviewspot.util.quickAction.ActionItem;
import com.sunrise.interview.interviewspot.util.quickAction.QuickAction;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by donnv on 7/20/2015.
 */
public class VideoManager extends ActionBarActivity {
    //action id
    private static final int ID_OPEN = 1;
    private static final int ID_DELETE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initActionBar();
        initUI();
        initialize();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshListView();
    }

    public void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(getLayoutInflater().inflate(R.layout.custom_action_bar_video_manager, null),
                new ActionBar.LayoutParams(
                        ActionBar.LayoutParams.MATCH_PARENT,
                        ActionBar.LayoutParams.MATCH_PARENT,
                        Gravity.CENTER
                )
        );
    }

    ListView lstVideo;
    TextView tvNoVideosFound;
    ImageButton btnRecordNew;

    public void initUI() {
        setContentView(R.layout.activity_video_manager);
        lstVideo = (ListView) findViewById(R.id.lst_video_manager);
        tvNoVideosFound = (TextView) findViewById(R.id.tv_video_manager_no_videos_found);
        btnRecordNew = (ImageButton) findViewById(R.id.btn_plus_video_manager);
    }


    String[] videoFileList;

    public void initialize() {
        refreshListView();
        initQuickAction();
        btnRecordNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRecord();
            }
        });
    }

    private void refreshListView() {
        videoFileList = getVideoList();
        lstVideo.setAdapter(new MyThumbnaildapter(this, R.layout.custom_listview_video_manager, videoFileList));
        if (videoFileList != null && videoFileList.length > 0) {
            tvNoVideosFound.setVisibility(View.GONE);
        } else {
            tvNoVideosFound.setVisibility(View.VISIBLE);
        }
    }

    private String[] getVideoList() {
        CUtil.createDirIfNotExist(CGlobal.VIDEO_HOME_PATH);
        String[] fileList;
        File videoFiles = new File(CGlobal.VIDEO_HOME_PATH);
        if (videoFiles.isDirectory()) {
            fileList = videoFiles.list();
            return fileList;
        }
        return null;
    }

    public void initQuickAction() {
        ActionItem openItem = new ActionItem(ID_OPEN, "Open", getResources().getDrawable(R.drawable.ic_menu_open));
        ActionItem deleteItem = new ActionItem(ID_DELETE, "Delete", getResources().getDrawable(R.drawable.ic_menu_delete));


        //create QuickAction. Use QuickAction.VERTICAL or QuickAction.HORIZONTAL param to define layout
        //orientation
        final QuickAction quickAction = new QuickAction(this, QuickAction.VERTICAL);

        //add action items into QuickAction
        quickAction.addActionItem(openItem);
        quickAction.addActionItem(deleteItem);

        //Set listener for action item clicked
        quickAction.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
            @Override
            public void onItemClick(QuickAction source, int pos, int actionId) {
                ActionItem actionItem = quickAction.getActionItem(pos);
                switch (actionId) {
                    case ID_OPEN:
                        startActivity(new Intent(getApplicationContext(), VideoInterviewPreview.class));
                        break;
                    case ID_DELETE:
                        CUtil.deleteRecursive(new File(CGlobal.VIDEO_PREVIEW_PATH));
                        refreshListView();
                        break;
                    default:
                        break;
                }
                Toast.makeText(getApplicationContext(), actionItem.getTitle() + " selected", Toast.LENGTH_SHORT).show();
            }
        });

        //set listnener for on dismiss event, this listener will be called only if QuickAction dialog was dismissed
        //by clicking the area outside the dialog.
        quickAction.setOnDismissListener(new QuickAction.OnDismissListener() {
            @Override
            public void onDismiss() {
//                Toast.makeText(getApplicationContext(), "Dismissed", Toast.LENGTH_SHORT).show();
            }
        });

        //show on

        lstVideo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CGlobal.VIDEO_PREVIEW_PATH = CGlobal.VIDEO_HOME_PATH + videoFileList[i];
                quickAction.show(view);
            }
        });
    }

    public class MyThumbnaildapter extends ArrayAdapter<String> {
        private int resource;
        private Context context;

        public MyThumbnaildapter(Context context, int textViewResourceId,
                                 String[] objects) {
            super(context, textViewResourceId, objects);
            resource = textViewResourceId;
            this.context = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub

            View row = convertView;
            if (row == null) {
                LayoutInflater inflater = getLayoutInflater();
                row = inflater.inflate(resource, parent, false);
            }

            TextView textfilePath = (TextView) row.findViewById(R.id.FilePath);
            textfilePath.setText(videoFileList[position]);

            ImageView imageThumbnail = (ImageView) row.findViewById(R.id.Thumbnail);

            Bitmap bmThumbnail;
            bmThumbnail = ThumbnailUtils.createVideoThumbnail(CGlobal.VIDEO_HOME_PATH + videoFileList[position], MediaStore.Video.Thumbnails.MICRO_KIND);
            if (bmThumbnail != null) {
                imageThumbnail.setImageBitmap(bmThumbnail);
            }
            return row;
        }
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
                    refreshListView();
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

        File mediaStorage = new File(CGlobal.VIDEO_HOME_PATH);
        if (!mediaStorage.exists() &&
                !mediaStorage.mkdirs()) {
            return null;
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        File mediaFile = new File(mediaStorage, "VID_" + timeStamp + ".mp4");
        return Uri.fromFile(mediaFile);
    }
}
