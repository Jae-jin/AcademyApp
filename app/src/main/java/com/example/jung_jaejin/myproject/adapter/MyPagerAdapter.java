package com.example.jung_jaejin.myproject.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.jung_jaejin.myproject.fragment.Fragment_one;
import com.example.jung_jaejin.myproject.fragment.Fragment_three;
import com.example.jung_jaejin.myproject.fragment.Fragment_two;

public class MyPagerAdapter extends FragmentPagerAdapter {
    int mNumOfTabs;

    public MyPagerAdapter(FragmentManager fm, int mNumOfTabs) {
        super(fm);
        this.mNumOfTabs = mNumOfTabs;
    }

    @Override
    public Fragment getItem(int position){
        switch (position) {
            case 0:
                Fragment_one tab1 = new Fragment_one();
                return tab1;
            case 1:
                Fragment_two tab2 = new Fragment_two();
                return tab2;
            case 2:
                Fragment_three tab3 = new Fragment_three();
                return tab3;
            default:
                return null;
        }
        //return null;
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
