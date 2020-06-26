package com.sarkarinaukri.adapter;

import android.content.Context;
import android.graphics.Color;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.sarkarinaukri.R;

import java.util.ArrayList;
import java.util.List;

/*
  Created by prashanth on 24/08/18.
 */

public class TabQnaAdapter extends FragmentStatePagerAdapter {

    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> tabNameList = new ArrayList<>();
    private Context context;

    public TabQnaAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    public void addFragment(Fragment fragment, String tabName) {
        mFragmentList.add(fragment);
        tabNameList.add(tabName);
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
        View view = LayoutInflater.from(context).inflate(R.layout.custom_tab_for_qna, null);
        TextView tabTextView = view.findViewById(R.id.tabTextView);
        View tabView = view.findViewById(R.id.view);
        tabView.setVisibility(View.GONE);
        tabTextView.setText(tabNameList.get(position));
        tabTextView.setTextColor(Color.parseColor("#c3c3c3"));
        // tabImageView.setImageResource(mFragmentIconList.get(position));
        return view;
    }

    public View getSelectedTabView(int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_tab_for_qna, null);
        TextView tabTextView = view.findViewById(R.id.tabTextView);
        View tabView = view.findViewById(R.id.view);
        tabView.setVisibility(View.VISIBLE);
        tabTextView.setText(tabNameList.get(position));
        tabTextView.setTextColor(Color.parseColor("#ffffff"));
        //tabImageView.setImageResource(mFragmentSelectedIconList.get(position));
        return view;
    }
}


