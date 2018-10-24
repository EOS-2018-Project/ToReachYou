package threeq.toreachyou;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import threeq.toreachyou.tag.Company;
import threeq.toreachyou.tag.Family;
import threeq.toreachyou.tag.Friend;
import threeq.toreachyou.tag.Lover;

public class MainTabPagerAdapter extends FragmentStatePagerAdapter {


    private final static int TAB_COUNT = 4;// 탭 개수
    private Context mContext;
    private int[] mTabTitle = {R.string.family, R.string.friend, R.string.lover, R.string.company};


    public MainTabPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.mContext = context;
        }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Family family = new Family();
                return family;
            case 1:
                Friend friend = new Friend();
                return friend;
            case 2:
                Lover lover = new Lover();
                return lover;
            default:
                Company company = new Company();
                return company;

        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getString(mTabTitle[position]);
    }

    @Override
    public int getCount() {
        return TAB_COUNT;
    }
}
