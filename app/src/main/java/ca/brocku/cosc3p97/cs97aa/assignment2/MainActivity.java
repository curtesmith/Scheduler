package ca.brocku.cosc3p97.cs97aa.assignment2;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.widget.Toast.LENGTH_SHORT;

public class MainActivity extends AppCompatActivity implements DayFragment.OnFragmentInteractionListener {

    SchedulerDbHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        setTitle(getTodaysDate());

        List<Fragment> fragments = getFragments();
        SchedulerPagerAdapter adapter = new SchedulerPagerAdapter(getSupportFragmentManager(), fragments);
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);
    }


    private String getTodaysDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM d, yyyy");
        return simpleDateFormat.format(new Date());
    }


    private List<Fragment> getFragments() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        List<Fragment> list = new ArrayList<>();
        try {
            list.add(DayFragment.newInstance(this, dateFormat.parse("2015-11-17")));
            list.add(DayFragment.newInstance(this, dateFormat.parse("2015-11-18")));
        } catch (ParseException e) {
            e.printStackTrace();
        }
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
                Toast.makeText(this, "create function has not yet been implemented", LENGTH_SHORT).show();
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
}
