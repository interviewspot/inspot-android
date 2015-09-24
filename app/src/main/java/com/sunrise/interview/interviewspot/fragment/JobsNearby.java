package com.sunrise.interview.interviewspot.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.sunrise.interview.interviewspot.R;
import com.sunrise.interview.interviewspot.activity.Home;
import com.sunrise.interview.interviewspot.adapter.JobNearbyListAdapter;
import com.sunrise.interview.interviewspot.enity.JobsNearbyEnity;
import com.sunrise.interview.interviewspot.util.CGlobal;

import java.util.ArrayList;
import java.util.List;

public class JobsNearby extends Fragment {
    private ListView listViewJobsNearby;
    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_jobs_nearby, container, false);
        initListView();
        return rootView;
    }

    private void initListView() {
        List<JobsNearbyEnity> jobsList = new ArrayList<JobsNearbyEnity>();
        jobsList.add(new JobsNearbyEnity("DDB Philippines", "(35km from you)", "Philippines, Taguig City", "Data Analyst ", "$10,000 PHP - 12,000 PHP", "logo_ddb"));

        jobsList.add(new JobsNearbyEnity("VNN", "(10km from you)", "VietNam, HoChiMinh City", "Data Analyst ", "$10,000 PHP - 12,000 PHP", "logo_ddb"));

        listViewJobsNearby = (ListView) rootView.findViewById(R.id.lst_job_near_by);
        listViewJobsNearby.setAdapter(new JobNearbyListAdapter(rootView.getContext(), R.layout.custom_listview_jobs_near_by, jobsList));

        // Click event for single list row
        listViewJobsNearby.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (Home.mTabHost.getCurrentTab() != Home.mViewPager.getCurrentItem()) {
                    return;
                }
                JobsNearbyEnity o = (JobsNearbyEnity) parent.getItemAtPosition(position);
                Toast.makeText(rootView.getContext(), o.getComapny().toString(), Toast.LENGTH_SHORT).show();
                //
                Home.mTabHost.setCurrentTab(0);
                CGlobal.jobsNearby.setComapny(o.getComapny());
                CGlobal.jobsNearby.setKm(o.getKm());
                CGlobal.jobsNearby.setAddress(o.getAddress());
                CGlobal.jobsNearby.setRole(o.getRole());
                CGlobal.jobsNearby.setSalary(o.getSalary());
                CGlobal.jobsNearby.setLogo(o.getLogo());

                Explore.resetJob();
            }
        });
    }
}
