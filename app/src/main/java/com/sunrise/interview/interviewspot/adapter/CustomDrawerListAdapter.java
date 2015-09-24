package com.sunrise.interview.interviewspot.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.sunrise.interview.interviewspot.R;
import com.sunrise.interview.interviewspot.enity.DrawerEnity;
import com.sunrise.interview.interviewspot.enity.DrawerSpinnerEnity;

import java.util.ArrayList;
import java.util.List;

public class CustomDrawerListAdapter extends ArrayAdapter<DrawerEnity> {

    Context context;
    List<DrawerEnity> drawerItemList;
    int layoutResID;

    public CustomDrawerListAdapter(Context context, int layoutResourceID,
                                   List<DrawerEnity> listItems) {
        super(context, layoutResourceID, listItems);
        this.context = context;
        this.drawerItemList = listItems;
        this.layoutResID = layoutResourceID;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub


        DrawerItemHolder drawerHolder;
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            drawerHolder = new DrawerItemHolder();

            view = inflater.inflate(layoutResID, parent, false);
            drawerHolder.ItemName = (TextView) view
                    .findViewById(R.id.drawer_itemName);
            drawerHolder.icon = (ImageView) view.findViewById(R.id.drawer_icon);

            drawerHolder.spinner = (Spinner) view
                    .findViewById(R.id.drawerSpinner);

            drawerHolder.title = (TextView) view.findViewById(R.id.drawerTitle);

            drawerHolder.headerLayout = (LinearLayout) view
                    .findViewById(R.id.headerLayout);
            drawerHolder.itemLayout = (LinearLayout) view
                    .findViewById(R.id.itemLayout);
            drawerHolder.spinnerLayout = (LinearLayout) view
                    .findViewById(R.id.spinnerLayout);

            view.setTag(drawerHolder);

        } else {
            drawerHolder = (DrawerItemHolder) view.getTag();

        }

        DrawerEnity dItem = (DrawerEnity) this.drawerItemList.get(position);

        if (dItem.isSpinner()) {
            drawerHolder.headerLayout.setVisibility(LinearLayout.INVISIBLE);
            drawerHolder.itemLayout.setVisibility(LinearLayout.INVISIBLE);
            drawerHolder.spinnerLayout.setVisibility(LinearLayout.VISIBLE);

            List<DrawerSpinnerEnity> userList = new ArrayList<DrawerSpinnerEnity>();

            userList.add(new DrawerSpinnerEnity(R.drawable.user1, "Ahamed Ishak",
                    "ishak@gmail.com"));
            userList.add(new DrawerSpinnerEnity(R.drawable.user2, "Brain Jekob",
                    "brain.j@gmail.com"));

            CustomDrawerSpinnerListAdapter adapter = new CustomDrawerSpinnerListAdapter(context,
                    R.layout.custom_drawer_spinner_item, userList);

            drawerHolder.spinner.setAdapter(adapter);

            drawerHolder.spinner
                    .setOnItemSelectedListener(new OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> arg0,
                                                   View arg1, int arg2, long arg3) {
                            // TODO Auto-generated method stub
//                            Toast.makeText(context, "User Changed",
//                                    Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> arg0) {
                            // TODO Auto-generated method stub

                        }
                    });

        } else if (dItem.getTitle() != null) {
            drawerHolder.headerLayout.setVisibility(LinearLayout.VISIBLE);
            drawerHolder.itemLayout.setVisibility(LinearLayout.INVISIBLE);
            drawerHolder.spinnerLayout.setVisibility(LinearLayout.INVISIBLE);
            drawerHolder.title.setText(dItem.getTitle());
            Log.d("Getview", "Passed4");
        } else {

            drawerHolder.headerLayout.setVisibility(LinearLayout.INVISIBLE);
            drawerHolder.spinnerLayout.setVisibility(LinearLayout.INVISIBLE);
            drawerHolder.itemLayout.setVisibility(LinearLayout.VISIBLE);

            drawerHolder.icon.setImageDrawable(view.getResources().getDrawable(
                    dItem.getImgResID()));
            drawerHolder.ItemName.setText(dItem.getItemName());
            Log.d("Getview", "Passed5");
        }
        return view;
    }

    private static class DrawerItemHolder {
        TextView ItemName, title;
        ImageView icon;
        LinearLayout headerLayout, itemLayout, spinnerLayout;
        Spinner spinner;
    }
}