package ca.brocku.cosc3p97.cs97aa.assignment2;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;


/**
 * A class which represents a custom pager adapter used by the view pager
 * of the Main activity to display DayFragments
 */
public class SchedulerPagerAdapter extends FragmentStatePagerAdapter {
    List<Fragment> fragments;


    /**
     * A custom constructor to store the list of fragments that will be
     * displayed in the view pager
     * @param fm A reference to the fragment manager
     * @param fragments A list of fragments to be used by the view pager
     */
    public SchedulerPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }


    /**
     *
     * @param i
     * @return
     */
    @Override
    public Fragment getItem(int i) {
        return fragments.get(i);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "OBJECT " + (position + 1);
    }
}
