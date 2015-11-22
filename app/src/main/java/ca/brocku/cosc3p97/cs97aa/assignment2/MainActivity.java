package ca.brocku.cosc3p97.cs97aa.assignment2;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
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
        ReviewMeetingDetailsDialogFragment.ReviewMeetingDetailsDialogListener,
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

    public void loadPager() {
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
        SchedulerDbHelper dbHelper = new SchedulerDbHelper(this);

        switch (item.getItemId()) {
            case R.id.action_create:
                DialogFragment f = MeetingDetailsDialogFragment.newInstance();
                f.show(getSupportFragmentManager(), "Create Meeting");
                return true;

            case R.id.action_view_contacts:
                Intent intent = new Intent(Intent.ACTION_INSERT_OR_EDIT);
                intent.setType(ContactsContract.Contacts.CONTENT_ITEM_TYPE);
                startActivity(intent);
                return true;

            case R.id.action_push_today:
                Date today = new Date();
                dbHelper.push(today, getNextDate(today));
                dbHelper.close();
                loadPager();
                Toast.makeText(this, "Today's meetings have been pushed", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.action_delete_expired:
                dbHelper.deleteBefore(new Date());
                dbHelper.close();
                loadPager();
                Toast.makeText(this, "Expired meetings have been deleted", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.action_delete_today:
                dbHelper.delete(new Date());
                dbHelper.close();
                loadPager();
                Toast.makeText(this, "Today's meetings have been deleted", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.action_delete_all:
                dbHelper.deleteAll();
                dbHelper.close();
                loadPager();
                Toast.makeText(this, "All meetings have been deleted", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private Date getNextDate(Date fromDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fromDate);

        int daysToAdd;
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        switch (dayOfWeek) {
            case 1:
                daysToAdd = 6;
                break;
            case 6:
                daysToAdd = 3;
                break;
            default:
                daysToAdd = 1;
        }

        calendar.add(Calendar.DATE, daysToAdd);
        return calendar.getTime();
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

    @Override
    public void onReviewMeetingDialogPositiveClick() {
        Toast.makeText(this, "The meeting has been deleted", Toast.LENGTH_SHORT).show();
        loadPager();
    }
}
