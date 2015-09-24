package com.sunrise.interview.interviewspot.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.sunrise.interview.interviewspot.R;
import com.sunrise.interview.interviewspot.adapter.CustomDrawerListAdapter;
import com.sunrise.interview.interviewspot.adapter.MyPageAdapter;
import com.sunrise.interview.interviewspot.enity.DrawerEnity;
import com.sunrise.interview.interviewspot.fragment.Explore;
import com.sunrise.interview.interviewspot.fragment.JobsNearby;
import com.sunrise.interview.interviewspot.fragment.MyJobs;
import com.sunrise.interview.interviewspot.util.CGlobal;
import com.sunrise.interview.interviewspot.util.MyTabFactory;
import com.sunrise.interview.interviewspot.util.socialNetwork.GooglePlusProcessor;

import java.util.ArrayList;
import java.util.List;

public class Home extends ActionBarActivity implements OnTabChangeListener, OnPageChangeListener {

    MyPageAdapter pageAdapter;
    public static ViewPager mViewPager;
    public static TabHost mTabHost;
    //
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    CustomDrawerListAdapter adapter;

    private List<DrawerEnity> dataList;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mViewPager = (ViewPager) findViewById(R.id.viewpager);

        initTabHost();

        initViewPager();

        initDrawer(savedInstanceState);

        progressDialog = ProgressDialog.show(Home.this, getString(R.string.app_name), " Loading. Please wait ... ", true);
        progressDialog.dismiss();

        CGlobal.jobsNearby.setComapny("DDB Philippines");
        CGlobal.jobsNearby.setKm("(35km from you)");
        CGlobal.jobsNearby.setAddress("Philippines, Taguig City");
        CGlobal.jobsNearby.setRole("Data Analyst");
        CGlobal.jobsNearby.setSalary("$10,000 PHP - 12,000 PHP");
        CGlobal.jobsNearby.setLogo("logo_ddb");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_logout:
                if (CGlobal.LOG_ON == CGlobal.LOG_ON_GOOLE_PLUS) {
                    new GooglePlusProcessor(this) {
                        @Override
                        public void updateUI(boolean isSignedIn) {
                            if (isSignedIn) {
                                onClick(GooglePlusProcessor.ACTION_REVOKE_ACCESS);
                                progressDialog.show();
                            } else {
                                CGlobal.LOG_ON = CGlobal.LOG_OUT;
                                Toast.makeText(getApplicationContext(), "User is logout!", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                startActivity(new Intent(getApplicationContext(), Login.class));
                                finish();
                            }
                        }
                    }.onStart();
                } else if (CGlobal.LOG_ON == CGlobal.LOG_ON_FACEBOOK) {
                    AccessToken accessToken = AccessToken.getCurrentAccessToken();
                    if (accessToken != null) {
                        LoginManager.getInstance().logOut();
                        startActivity(new Intent(getApplicationContext(), Login.class));
                        finish();
                    }
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Tabs Creation
     */

    private void initTabHost() {
        mTabHost = (TabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup();

        // TODO Put here your Tabs
        TabHost.TabSpec tabSpec1 = createTabSpec("Tab1", getString(R.string.home_tab_explore));
        AddTab(this, this.mTabHost, tabSpec1);

        TabHost.TabSpec tabSpec2 = createTabSpec("Tab2", getString(R.string.home_tab_jobs_near_by));
        AddTab(this, this.mTabHost, tabSpec2);

        TabHost.TabSpec tabSpec3 = createTabSpec("Tab3", getString(R.string.home_tab_my_jobs));
        AddTab(this, this.mTabHost, tabSpec3);

        mTabHost.setOnTabChangedListener(this);
    }

    // Method to add a TabHost
    private void AddTab(Context context, TabHost tabHost, TabHost.TabSpec tabSpec) {
        tabSpec.setContent(new MyTabFactory(context));
        tabHost.addTab(tabSpec);
    }

    private TabHost.TabSpec createTabSpec(String tag, String text) {
        TabHost.TabSpec tabSpec = mTabHost.newTabSpec(tag);
        View view = createTabView(getApplicationContext(), text);
        tabSpec.setIndicator(view);
        return tabSpec;
    }

    private View createTabView(Context context, String tabText) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_tab_home, null, false);
        TextView tv = (TextView) view.findViewById(R.id.tabTitleText);
        tv.setText(tabText);
        return view;
    }

    // Manages the Tab changes, synchronizing it with Pages
    public void onTabChanged(String tag) {
        int pos = this.mTabHost.getCurrentTab();
        this.mViewPager.setCurrentItem(pos);
    }

    /**
     * ViewPager Creation
     */

    private void initViewPager() {
        // Fragments and ViewPager Initialization
        List<Fragment> fragments = getFragments();
        pageAdapter = new MyPageAdapter(getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(pageAdapter);
        mViewPager.setOnPageChangeListener(Home.this);
        mViewPager.setOffscreenPageLimit(fragments.size());
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
    }

    // Manages the Page changes, synchronizing it with Tabs
    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
        int pos = this.mViewPager.getCurrentItem();
        this.mTabHost.setCurrentTab(pos);
    }

    @Override
    public void onPageSelected(int arg0) {
    }

    private List<Fragment> getFragments() {
        List<Fragment> fList = new ArrayList<Fragment>();
        // TODO Put here your Fragments
        fList.add(new Explore());
        fList.add(new JobsNearby());
        fList.add(new MyJobs());

        return fList;
    }

    /**
     * Drawer Creation
     */

    private void initDrawer(Bundle savedInstanceState) {
        dataList = new ArrayList<DrawerEnity>();
        mTitle = mDrawerTitle = getTitle();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
                GravityCompat.START);

        // Add Drawer Item to dataList
        dataList.add(new DrawerEnity(true)); // adding a spinner to the list

        dataList.add(new DrawerEnity("My Favorites")); // adding a header to the
        // list
        dataList.add(new DrawerEnity("Message", R.drawable.ic_action_email));
        dataList.add(new DrawerEnity("Likes", R.drawable.ic_action_good));
        dataList.add(new DrawerEnity("Games", R.drawable.ic_action_gamepad));
        dataList.add(new DrawerEnity("Lables", R.drawable.ic_action_labels));

        dataList.add(new DrawerEnity("Main Options"));// adding a header to the
        // list
        dataList.add(new DrawerEnity("Search", R.drawable.ic_action_search));
        dataList.add(new DrawerEnity("Cloud", R.drawable.ic_action_cloud));
        dataList.add(new DrawerEnity("Camara", R.drawable.ic_action_camera));
        dataList.add(new DrawerEnity("Video", R.drawable.ic_action_video));
        dataList.add(new DrawerEnity("Groups", R.drawable.ic_action_group));
        dataList.add(new DrawerEnity("Import & Export",
                R.drawable.ic_action_import_export));

        dataList.add(new DrawerEnity("Other Option")); // adding a header to the
        // list
        dataList.add(new DrawerEnity("About", R.drawable.ic_action_about));
        dataList.add(new DrawerEnity("Settings", R.drawable.ic_action_settings));
        dataList.add(new DrawerEnity("Help", R.drawable.ic_action_help));

        adapter = new CustomDrawerListAdapter(this, R.layout.custom_drawer_item,
                dataList);

        mDrawerList.setAdapter(adapter);

        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open,
                R.string.drawer_close) {
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to
                // onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to
                // onPrepareOptionsMenu()
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {

            if (dataList.get(0).isSpinner()
                    & dataList.get(1).getTitle() != null) {
                SelectItem(2);
            } else if (dataList.get(0).getTitle() != null) {
                SelectItem(1);
            } else {
                SelectItem(0);
            }
        }
    }

    public void SelectItem(int possition) {
        mDrawerList.setItemChecked(possition, true);
        setTitle(dataList.get(possition).getItemName());
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private class DrawerItemClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            if (dataList.get(position).getTitle() == null) {
                SelectItem(position);
            }
        }
    }
}
