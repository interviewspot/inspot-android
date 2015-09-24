package com.sunrise.interview.interviewspot.fragment.explore;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sunrise.interview.interviewspot.R;

/**
 * Created by jerry on 6/30/2015.
 */
public class JobInfo extends Fragment {
    private View rootView;
    public static TextView tv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_ex_job_info, container, false);
        tv = (TextView) rootView.findViewById(R.id.tv_job_info);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public static void setInfo(String info) {
        tv.setText(info);
    }
}
