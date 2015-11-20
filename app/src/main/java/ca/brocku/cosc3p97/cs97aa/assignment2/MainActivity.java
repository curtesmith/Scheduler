package ca.brocku.cosc3p97.cs97aa.assignment2;


import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements DayFragment.OnFragmentInteractionListener,
        MeetingDetailsDialogFragment.MeetingDetailsDialogListener,
ViewPager.OnPageChangeListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);

        setSupportActionBar(myToolbar);
        setTitle(getTodaysDate());

        loadPager();

    }

    private void loadPager() {
        List<Fragment> fragments = getFragments();
        SchedulerPagerAdapter adapter = new SchedulerPagerAdapter(getSupportFragmentManager(), fragments);
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.addOnPageChangeListener(this);
        pager.setAdapter(adapter);
    }


    private String getTodaysDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM d, yyyy");
        return simpleDateFormat.format(new Date());
    }


    private String getTomorrowsDate() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, 1);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d, yyyy");
        return dateFormat.format(c.getTime());
    }


    private List<Fragment> getFragments() {
        List<Fragment> list = new ArrayList<>();

        Calendar c = Calendar.getInstance();
        list.add(DayFragment.newInstance(this, c.getTime()));
        c.add(Calendar.DATE, 1);
        list.add(DayFragment.newInstance(this, c.getTime()));

        return list;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_create:
                DialogFragment f = MeetingDetailsDialogFragment.newInstance();
                f.show(getSupportFragmentManager(), "Create Meeting");
                return true;

            case R.id.action_settings:
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onDialogPositiveClick(String title) {
        Toast.makeText(this, "The meeting has been created", Toast.LENGTH_SHORT).show();
        loadPager();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        //ignore
    }

    @Override
    public void onPageSelected(int position) {
        if(position == 0) {
            setTitle(getTodaysDate());
        } else {
            setTitle(getTomorrowsDate());
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        //ignore
    }
}
