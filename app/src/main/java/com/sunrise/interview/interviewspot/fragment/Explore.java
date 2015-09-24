package com.sunrise.interview.interviewspot.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.sunrise.interview.interviewspot.R;
import com.sunrise.interview.interviewspot.activity.VideoManager;
import com.sunrise.interview.interviewspot.adapter.MyPageAdapter;
import com.sunrise.interview.interviewspot.fragment.explore.CompanyInfo;
import com.sunrise.interview.interviewspot.fragment.explore.Contacts;
import com.sunrise.interview.interviewspot.fragment.explore.JobInfo;
import com.sunrise.interview.interviewspot.util.CGlobal;
import com.sunrise.interview.interviewspot.util.MyTabFactory;

import java.util.ArrayList;
import java.util.List;

public class Explore extends Fragment implements TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener {
    private MyPageAdapter pageAdapter;
    private ViewPager mViewPager;
    private TabHost mTabHost;
    private View rootView;
    private Button btnApply, btnReject, btnShortlist, btnShare;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_explore, container, false);

        mViewPager = (ViewPager) rootView.findViewById(R.id.viewpager_explore);

        // Tab Initialization
        initialiseTabHost();

        // Fragments and ViewPager Initialization
        List<Fragment> fragments = getFragments();
        pageAdapter = new MyPageAdapter(getActivity().getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(pageAdapter);
        mViewPager.setOnPageChangeListener(this);
        mViewPager.setOffscreenPageLimit(fragments.size());
        initFooter();
        initial();
        return rootView;
    }

    public static TextView tvCompany, tvKm, tvRole, tvAddress, tvSalary;

    private void initial() {
        tvCompany = (TextView) rootView.findViewById(R.id.tv_company_explore);
        tvKm = (TextView) rootView.findViewById(R.id.tv_km__explore);
        tvRole = (TextView) rootView.findViewById(R.id.tv_role_explore);
        tvAddress = (TextView) rootView.findViewById(R.id.tv_address_explore);
        tvSalary = (TextView) rootView.findViewById(R.id.tv_salary_explore);
        resetJob();
    }

    public static void resetJob() {
        tvCompany.setText(CGlobal.jobsNearby.getComapny());
        tvKm.setText(CGlobal.jobsNearby.getKm());
        tvRole.setText(CGlobal.jobsNearby.getRole());
        tvAddress.setText(CGlobal.jobsNearby.getAddress());
        tvSalary.setText(CGlobal.jobsNearby.getSalary());
    }

    // Manages the Tab changes, synchronizing it with Pages
    public void onTabChanged(String tag) {
        int pos = this.mTabHost.getCurrentTab();
        this.mViewPager.setCurrentItem(pos);
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
        List<Fragment> fList;
        fList = new ArrayList<Fragment>();

        // TODO Put here your Fragments
        fList.add(new JobInfo());
        fList.add(new CompanyInfo());
        fList.add(new Contacts());

        return fList;
    }

    // Tabs Creation
    private void initialiseTabHost() {
        mTabHost = (TabHost) rootView.findViewById(android.R.id.tabhost);
        mTabHost.setup();

        // TODO Put here your Tabs
        TabHost.TabSpec tabSpec1 = createTabSpec(rootView.getContext(), "Tab1", getString(R.string.home_tab_job_info));
        AddTab(rootView.getContext(), this.mTabHost, tabSpec1);

        TabHost.TabSpec tabSpec2 = createTabSpec(rootView.getContext(), "Tab2", getString(R.string.home_tab_company_info));
        AddTab(rootView.getContext(), this.mTabHost, tabSpec2);

        TabHost.TabSpec tabSpec3 = createTabSpec(rootView.getContext(), "Tab3", getString(R.string.home_tab_contacts));
        AddTab(rootView.getContext(), this.mTabHost, tabSpec3);

        mTabHost.setOnTabChangedListener(this);
    }

    // Method to add a TabHost
    private void AddTab(Context context, TabHost tabHost, TabHost.TabSpec tabSpec) {
        tabSpec.setContent(new MyTabFactory(context));
        tabHost.addTab(tabSpec);
    }

    private TabHost.TabSpec createTabSpec(Context context, String tag, String text) {
        TabHost.TabSpec tabSpec = mTabHost.newTabSpec(tag);
        View view = createTabView(context, text);
        tabSpec.setIndicator(view);
        return tabSpec;
    }

    private View createTabView(Context context, String tabText) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_tab_explore, null, false);
        TextView tv = (TextView) view.findViewById(R.id.tabTitleText);
        tv.setText(tabText);
        return view;
    }

    private LinearLayout lnFooter;
    private TranslateAnimation anim;
    boolean onAnim;
    boolean isShowFooter;

    private void actionFooter(boolean isShow) {
        isShowFooter = isShow;
        int translationY_From, translationY_To;
        if (isShow) {
            translationY_From = lnFooter.getHeight();
            translationY_To = 0;
        } else {
            translationY_From = 0;
            translationY_To = lnFooter.getHeight();
        }
        anim = new TranslateAnimation(0, 0, translationY_From,
                translationY_To);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                onAnim = true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                onAnim = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        anim.setFillAfter(true);
        anim.setDuration(100);
        lnFooter.startAnimation(anim);
    }

    /**
     * Footer
     */
    private void initFooter() {
        lnFooter = (LinearLayout) rootView.findViewById(R.id.ln_footer_explore);
        isShowFooter = true;
        actionFooter(isShowFooter);

        btnApply = (Button) rootView.findViewById(R.id.cmd_1_footer_explore);
        btnReject = (Button) rootView.findViewById(R.id.cmd_2_footer_explore);
        btnShortlist = (Button) rootView.findViewById(R.id.cmd_3_footer_explore);
        btnShare = (Button) rootView.findViewById(R.id.cmd_4_footer_explore);

        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(rootView.getContext(), VideoManager.class));
            }
        });
        btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        btnShortlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }
}
