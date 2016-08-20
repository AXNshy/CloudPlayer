package com.axnshy.cloudmusic.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by axnshy on 16/8/18.
 */
public class MyFragmentPagerAdapter extends FragmentPagerAdapter {


    private List<String> mItems = new ArrayList<String>(
            Arrays.asList(
                    "动态柱状图","圆形进度条","自定义图片"
            ));

    private List<Fragment> fragments;

    public MyFragmentPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fragments=fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mItems.get(position);
    }
}
