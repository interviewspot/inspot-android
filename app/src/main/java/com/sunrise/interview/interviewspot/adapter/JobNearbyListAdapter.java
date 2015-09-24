package com.sunrise.interview.interviewspot.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sunrise.interview.interviewspot.R;
import com.sunrise.interview.interviewspot.enity.JobsNearbyEnity;

import java.util.List;

/**
 * Created by donnv on 7/3/2015.
 */
public class JobNearbyListAdapter extends ArrayAdapter<JobsNearbyEnity> {
    private int resource;
    private LayoutInflater inflater;
    private Context context;

    public JobNearbyListAdapter(Context ctx, int resourceId, List<JobsNearbyEnity> objects) {
        super(ctx, resourceId, objects);
        resource = resourceId;
        inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        context = ctx;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = inflater.inflate(resource, parent, false);

        RelativeLayout relativeLayout = (RelativeLayout) convertView.findViewById(R.id.rlt_main_jobs_near_by);
        relativeLayout.setBackgroundResource(position % 2 == 0 ? R.drawable.list_selector_contacts : R.drawable.list_selector_jobs_near_by);

        JobsNearbyEnity enity = getItem(position);
        TextView enityName = (TextView) convertView.findViewById(R.id.tv_company_customlist_jobs_near_b);
        enityName.setText(enity.getComapny());

        TextView enityKm = (TextView) convertView.findViewById(R.id.tv_km_customlist_jobs_near_by);
        enityKm.setText(enity.getKm());

        TextView enityAddress = (TextView) convertView.findViewById(R.id.tv_address_customlist_jobs_near_by);
        enityAddress.setText(enity.getRole() + " | " + enity.getAddress());

        TextView enitySalary = (TextView) convertView.findViewById(R.id.tv_salary_customlist_jobs_near_by);
        enitySalary.setText(enity.getSalary());

        ImageView enityImage = (ImageView) convertView.findViewById(R.id.imv_logo_customlist_jobs_near_by);

        String uri = "drawable/" + enity.getLogo();
        int imageResource = context.getResources().getIdentifier(uri, null, context.getPackageName());
        Drawable image = context.getResources().getDrawable(imageResource);
        enityImage.setImageDrawable(image);

        return convertView;
    }
}
