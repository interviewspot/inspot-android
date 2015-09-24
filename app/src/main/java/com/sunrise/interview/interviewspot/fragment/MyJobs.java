package com.sunrise.interview.interviewspot.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.sunrise.interview.interviewspot.activity.Home;
import com.sunrise.interview.interviewspot.R;
import com.sunrise.interview.interviewspot.activity.VideoInterviewRecording;

public class MyJobs extends Fragment {
    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_my_jobs, container, false);
        initUI();
        return rootView;
    }

    private void initUI() {

    }
}
