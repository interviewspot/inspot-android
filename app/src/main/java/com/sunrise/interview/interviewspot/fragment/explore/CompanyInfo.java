package com.sunrise.interview.interviewspot.fragment.explore;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sunrise.interview.interviewspot.R;

/**
 * Created by jerry on 6/30/2015.
 */
public class CompanyInfo extends Fragment{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_ex_company_info, container, false);
        return rootView;
    }
}
