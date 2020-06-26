package com.sarkarinaukri.qnaModule.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.sarkarinaukri.qnaModule.fragment.MyAnswerFragment;
import com.sarkarinaukri.qnaModule.fragment.MyQuestionFragment;
import com.sarkarinaukri.qnaModule.fragment.QNAListFragment;

/**
 * Created by anurag on 7/30/2016.
 */
public class QnaPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    String source;

    public QnaPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }


    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                QNAListFragment tab1 = new QNAListFragment();
                return tab1;
            case 1:
                MyQuestionFragment tab2 = new MyQuestionFragment();
                return tab2;
            case 2:
                MyAnswerFragment tab3 = new MyAnswerFragment();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
