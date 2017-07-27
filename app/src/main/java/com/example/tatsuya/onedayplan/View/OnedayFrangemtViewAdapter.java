package com.example.tatsuya.onedayplan.View;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

/**
 * Created by tatsuya on 2017/06/01.
 */

public class OnedayFrangemtViewAdapter extends FragmentPagerAdapter {
    public OnedayFrangemtViewAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return OnedayFragmentView.newInstance(position);
    }

    @Override
    public int getCount() {
        return 5;
    }



    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){

            case 0 :
                return "月曜日";
            case 1 :
                return "火曜日";
            case 2 :
                return "水曜日";
            case 3 :
                return "木曜日";
            case 4 :
                return "金曜日";
        }
        return "";
    }
}
