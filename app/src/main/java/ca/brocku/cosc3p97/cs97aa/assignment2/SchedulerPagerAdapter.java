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
     * Retrieve the item desired as specified by the index parameter
     * @param index The index of the desired item in the fragments list
     * @return the desired fragment object
     */
    @Override
    public Fragment getItem(int index) {
        return fragments.get(index);
    }


    /**
     * Retrieve the number of items in the fragment list
     * @return the count as an integer
     */
    @Override
    public int getCount() {
        return fragments.size();
    }


    /**
     * Retrieve the page title
     * @param position The position in the list
     * @return The title of the page associated with the position passed as an argument
     */
    @Override
    public CharSequence getPageTitle(int position) {
        return "FRAGMENT " + (position + 1);
    }
}
