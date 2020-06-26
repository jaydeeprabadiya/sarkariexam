package com.sarkarinaukri.adapter;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.sarkarinaukri.R;

import java.util.ArrayList;
import java.util.List;

/*
  Created by prashanth on 24/08/18.
 */

public class TabAdapter extends FragmentStatePagerAdapter {

    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<Integer> mFragmentIconList = new ArrayList<>();
    private final List<Integer> mFragmentSelectedIconList = new ArrayList<>();
    private Context context;

    public TabAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    public void addFragment(Fragment fragment, int tabIcon, int tabSelectedIcon) {
        mFragmentList.add(fragment);
        mFragmentIconList.add(tabIcon);
        mFragmentSelectedIconList.add(tabSelectedIcon);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        //return mFragmentTitleList.get(position);
        return null;
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public View getTabView(int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_tab_for_job, null);
        ImageView tabImageView = view.findViewById(R.id.tabImageView);
        tabImageView.setImageResource(mFragmentIconList.get(position));
        return view;
    }

    public View getSelectedTabView(int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_tab_for_job, null);
        ImageView tabImageView = view.findViewById(R.id.tabImageView);
        tabImageView.setImageResource(mFragmentSelectedIconList.get(position));
        return view;
    }
}


