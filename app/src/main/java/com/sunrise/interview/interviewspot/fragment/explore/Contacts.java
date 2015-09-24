package com.sunrise.interview.interviewspot.fragment.explore;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.sunrise.interview.interviewspot.R;
import com.sunrise.interview.interviewspot.adapter.ContactsListAdapter;
import com.sunrise.interview.interviewspot.enity.ContactsEnity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jerry on 6/30/2015.
 */
public class Contacts extends Fragment {
    private View rootView;

    private ListView listViewContacts;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_ex_contacts, container, false);
        initListView();
        return rootView;
    }

    private void initListView() {
        List<ContactsEnity> enityList = new ArrayList<ContactsEnity>();
        enityList.add(new ContactsEnity("James Wallace", "Hiring Manager", "avatar", "brazil"));

        listViewContacts = (ListView) rootView.findViewById(R.id.lst_ex_contacts);
        listViewContacts.setAdapter(new ContactsListAdapter(rootView.getContext(), R.layout.custom_listview_contacts, enityList));

        // Click event for single list row
        listViewContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ContactsEnity o = (ContactsEnity) parent.getItemAtPosition(position);
                Toast.makeText(rootView.getContext(), o.getName().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
