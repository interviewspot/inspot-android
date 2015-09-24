package com.sunrise.interview.interviewspot.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sunrise.interview.interviewspot.R;
import com.sunrise.interview.interviewspot.enity.DrawerSpinnerEnity;

public class CustomDrawerSpinnerListAdapter extends ArrayAdapter<DrawerSpinnerEnity> {


    public CustomDrawerSpinnerListAdapter(Context context, int layoutResourceID,
                                          int textViewResourceId, List<DrawerSpinnerEnity> spinnerDataList) {
        super(context, layoutResourceID, textViewResourceId, spinnerDataList);


        this.context = context;
        this.layoutResID = layoutResourceID;
        this.spinnerData = spinnerDataList;

    }

    Context context;
    int layoutResID;
    List<DrawerSpinnerEnity> spinnerData;

    public CustomDrawerSpinnerListAdapter(Context context, int layoutResourceID,
                                          List<DrawerSpinnerEnity> spinnerDataList) {
        super(context, layoutResourceID, spinnerDataList);

        this.context = context;
        this.layoutResID = layoutResourceID;
        this.spinnerData = spinnerDataList;

    }


    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        return getCustomView(position, convertView, parent);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        return getCustomView(position, convertView, parent);
    }


    public View getCustomView(int position, View convertView, ViewGroup parent) {


        View row = convertView;
        SpinnerHolder holder;


        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();

            row = inflater.inflate(layoutResID, parent, false);
            holder = new SpinnerHolder();

            holder.userImage = (ImageView) row.findViewById(R.id.left_pic);
            holder.name = (TextView) row.findViewById(R.id.text_main_name);
            holder.email = (TextView) row.findViewById(R.id.sub_text_email);

            row.setTag(holder);
        } else {
            holder = (SpinnerHolder) row.getTag();

        }

        DrawerSpinnerEnity spinnerItem = spinnerData.get(position);

        holder.userImage.setImageDrawable(row.getResources().getDrawable(spinnerItem.getDrawableResID()));
        holder.name.setText(spinnerItem.getName());
        holder.email.setText(spinnerItem.getEmail());

        return row;

    }

    private static class SpinnerHolder {
        ImageView userImage;
        TextView name, email;

    }
}
