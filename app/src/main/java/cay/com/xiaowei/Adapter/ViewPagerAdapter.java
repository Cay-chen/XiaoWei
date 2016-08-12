package cay.com.xiaowei.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Cay on 2016/8/4.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter{
    private List<Fragment> fragmentList;

    public ViewPagerAdapter(FragmentManager fm, List<Fragment> fragmentList) {
        super(fm);
        this.fragmentList = fragmentList;
    }

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
    @Override
    public CharSequence getPageTitle(int position) {
        if (position==0) return "众创商城";
        else if (position==1) return "众创充值";
        else if (position ==2 ) return "个人中心";
        else return "";

    }


}
