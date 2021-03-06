package ca.brocku.cosc3p97.cs97aa.assignment2;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

/**
 * A class for the main activity of the application
 */
public class MainActivity extends AppCompatActivity
        implements MeetingDetailsDialogFragment.MeetingDetailsDialogListener,
        ReviewMeetingDetailsDialogFragment.ReviewMeetingDetailsDialogListener,
        ViewPager.OnPageChangeListener {

    private int currentPage = 0;


    /**
     * Callback invoked with the activity is created
     * @param savedInstanceState Previously saved instance of this activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        setSupportActionBar((Toolbar) findViewById(R.id.my_toolbar));
        setTitle(DateHelper.getTodayLongDate());
        loadPager();
    }


    /**
     * Load the viewpager with a list of the fragments where each element contains a list
     * of the meetings for a calendar day
     */
    public void loadPager() {
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        SchedulerPagerAdapter adapter = new SchedulerPagerAdapter(getSupportFragmentManager(), getFragments());
        pager.setAdapter(adapter);
        pager.setCurrentItem(currentPage);
        pager.addOnPageChangeListener(this);
    }


    /**
     * Build a list of fragments for the viewpager where each fragment contains a list
     * of the meetings for a calendar day
     * @return
     */
    private List<Fragment> getFragments() {
        List<Fragment> list = new ArrayList<>();
        list.add(DayFragment.newInstance(this, DateHelper.getToday()));
        list.add(DayFragment.newInstance(this, DateHelper.getTomorrow()));
        return list;
    }


    /**
     * Callback method used to populate the menu
     * @param menu The menu reference associated with this activity
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    /**
     * Callback method invoked when an option is selected from the menu
     * of this activity
     * @param item A reference to the item selected by the user
     * @return A boolean result
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final SchedulerDbHelper dbHelper = new SchedulerDbHelper(this);

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
                new AlertDialog.Builder(this)
                        .setTitle(R.string.confirm_title)
                        .setMessage(R.string.message_confirm_push_today)
                        .setPositiveButton(R.string.button_push_today, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Date fromDate = new Date();
                                dbHelper.push(fromDate, DateHelper.getNextDate(fromDate));
                                dbHelper.close();
                                loadPager();
                                Toast.makeText(getApplicationContext(), R.string.message_push, Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton(R.string.button_cancel, null)
                        .show();
                ;
                return true;

            case R.id.action_delete_expired:
                new AlertDialog.Builder(this)
                        .setTitle(R.string.confirm_title)
                        .setMessage(R.string.message_confirm_delete_expired)
                        .setPositiveButton(R.string.button_delete_expired, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dbHelper.deleteBefore(new Date());
                                dbHelper.close();
                                loadPager();
                                Toast.makeText(getApplicationContext(), R.string.message_expired, Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton(R.string.button_cancel, null)
                        .show();
                return true;

            case R.id.action_delete_today:
                new AlertDialog.Builder(this)
                        .setTitle(R.string.confirm_title)
                        .setMessage(R.string.message_confirm_delete_today)
                        .setPositiveButton(R.string.button_delete_today, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dbHelper.delete(new Date());
                                dbHelper.close();
                                loadPager();
                                Toast.makeText(getApplicationContext(), R.string.message_delete_today, Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton(R.string.button_cancel, null)
                        .show();
                return true;

            case R.id.action_delete_all:
                new AlertDialog.Builder(this)
                        .setTitle(R.string.confirm_title)
                        .setMessage(R.string.message_confirm_delete_all)
                        .setPositiveButton(R.string.button_delete_all, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dbHelper.deleteAll();
                                dbHelper.close();
                                loadPager();
                                Toast.makeText(getApplicationContext(), R.string.message_delete_all, Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton(R.string.button_cancel, null)
                        .show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    /**
     * Callback invoked when the MeetingDetailsDialogFragment has its positive button
     * clicked to indicate a meeting has been created
     */
    @Override
    public void onDialogPositiveClick() {
        Toast.makeText(this, R.string.message_created, Toast.LENGTH_SHORT).show();
        setTitle(DateHelper.getTodayLongDate());
        loadPager();
    }


    /**
     * Callback method invoked when the viewpager onPageScrolled event is fired.
     * For the purposes of this application it is being ignored.
     * @param position Position
     * @param positionOffset Offset
     * @param positionOffsetPixels OffsetPixels
     */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        //ignore
    }


    /**
     * Callback invoked with the viewpage has been navigated to a new page
     * @param position The index of the new page
     */
    @Override
    public void onPageSelected(int position) {
        currentPage = position;

        if (position == 0) {
            setTitle(DateHelper.getTodayLongDate());
        } else {
            setTitle(DateHelper.getTomorrowLongDate());
        }
    }


    /**
     * Callback method invoked when the viewpager onPageScrollStateChanged event
     * is fired. For the purposes of this application it is being ignored.
     * @param state State
     */
    @Override
    public void onPageScrollStateChanged(int state) {
        //ignore
    }


    /**
     * Callback method invoked when the ReviewMeetingDetailsDialogFragment has
     * its positive button clicked to indicate that a meeting has been deleted
     */
    @Override
    public void onReviewMeetingDialogPositiveClick() {
        Toast.makeText(this, R.string.message_deleted, Toast.LENGTH_SHORT).show();
        loadPager();
    }
}
