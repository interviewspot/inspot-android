package com.sunrise.interview.interviewspot.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sunrise.interview.interviewspot.R;
import com.sunrise.interview.interviewspot.enity.ContactsEnity;
import com.sunrise.interview.interviewspot.util.CUtil;

import java.util.List;

/**
 * Created by jerry on 7/2/2015.
 */
public class ContactsListAdapter extends ArrayAdapter<ContactsEnity> {
    private int resource;
    private LayoutInflater inflater;
    private Context context;

    public ContactsListAdapter(Context ctx, int resourceId, List<ContactsEnity> objects) {
        super(ctx, resourceId, objects);
        resource = resourceId;
        inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        context = ctx;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = inflater.inflate(resource, parent, false);
        ContactsEnity enity = getItem(position);
        TextView legendName = (TextView) convertView.findViewById(R.id.legendName);
        legendName.setText(enity.getName());

        TextView legendBorn = (TextView) convertView.findViewById(R.id.legendBorn);
        legendBorn.setText(enity.getNick());

        ImageView legendImage = (ImageView) convertView.findViewById(R.id.legendImage);
//        String uri = "drawable/" + Legend.getImage();
//        int imageResource = context.getResources().getIdentifier(uri, null, context.getPackageName());
//        Drawable image = context.getResources().getDrawable(imageResource);
//        legendImage.setImageDrawable(image);


        Drawable image = context.getResources().getDrawable(R.drawable.avatar);
        Bitmap _bitmap = ((BitmapDrawable) image).getBitmap();
        _bitmap = CUtil.getCircleBitmap(_bitmap);
        legendImage.setImageBitmap(_bitmap);

        ImageView NationImage = (ImageView) convertView.findViewById(R.id.Nation);
//        uri = "drawable/" + Legend.getNation();
//        imageResource = context.getResources().getIdentifier(uri, null, context.getPackageName());
//        image = context.getResources().getDrawable(imageResource);
        image = context.getResources().getDrawable(R.drawable.ic_menu_start_conversation);
        NationImage.setImageDrawable(image);

        return convertView;
    }
}
