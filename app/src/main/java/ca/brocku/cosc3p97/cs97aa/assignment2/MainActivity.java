package ca.brocku.cosc3p97.cs97aa.assignment2;

import android.content.Intent;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements MeetingDetailsDialogFragment.MeetingDetailsDialogListener,
        ReviewMeetingDetailsDialogFragment.ReviewMeetingDetailsDialogListener,
        ViewPager.OnPageChangeListener {

    private int currentPage = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        setSupportActionBar((Toolbar) findViewById(R.id.my_toolbar));
        setTitle(DateHelper.getTodayLongDate());
        loadPager();
    }


    public void loadPager() {
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        SchedulerPagerAdapter adapter = new SchedulerPagerAdapter(getSupportFragmentManager(), getFragments());
        pager.setAdapter(adapter);
        pager.setCurrentItem(currentPage);
        pager.addOnPageChangeListener(this);
    }


    private List<Fragment> getFragments() {
        List<Fragment> list = new ArrayList<>();
        list.add(DayFragment.newInstance(this, DateHelper.getToday()));
        list.add(DayFragment.newInstance(this, DateHelper.getTomorrow()));
        return list;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
                Date fromDate = new Date();
                dbHelper.push(fromDate, DateHelper.getNextDate(fromDate));
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


    @Override
    public void onDialogPositiveClick() {
        Toast.makeText(this, "The meeting has been created", Toast.LENGTH_SHORT).show();
        setTitle(DateHelper.getTodayLongDate());
        loadPager();
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        //ignore
    }


    @Override
    public void onPageSelected(int position) {
        currentPage = position;

        if (position == 0) {
            setTitle(DateHelper.getTodayLongDate());
        } else {
            setTitle(DateHelper.getTomorrowLongDate());
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
