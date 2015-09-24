package com.sunrise.interview.interviewspot.util;

import android.os.Environment;

import com.sunrise.interview.interviewspot.enity.JobsNearbyEnity;

/**
 * Created by donnv on 7/8/2015.
 */
public class CGlobal {
    public static final String VIDEO_HOME_PATH = Environment.getExternalStorageDirectory().getPath() + "/interviewspot/";

    public static final int LOG_OUT = 0;
    public static final int LOG_ON_FACEBOOK = 1;
    public static final int LOG_ON_GOOLE_PLUS = 2;
    public static final int LOG_ON_LINKED_IN = 3;


    public static String VIDEO_PREVIEW_PATH;

    public static String VIDEO_RECORD_PATH;

    public static int LOG_ON = LOG_OUT;

    public static JobsNearbyEnity jobsNearby = new JobsNearbyEnity();
}
