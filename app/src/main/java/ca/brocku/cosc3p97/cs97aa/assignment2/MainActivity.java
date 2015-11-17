package ca.brocku.cosc3p97.cs97aa.assignment2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import static android.widget.Toast.LENGTH_SHORT;

public class MainActivity extends AppCompatActivity {

    SchedulerDbHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        setTitle(getTodaysDate());
        fillListView();
    }


    private String getTodaysDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM d, yyyy");
        return simpleDateFormat.format(new Date());
    }


    private void fillListView() {
        createDatabase();

        ListView listView = (ListView) findViewById(R.id.meetings_list);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
               android.R.layout.simple_list_item_1, android.R.id.text1, selectTodaysMeetings());

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String message = "You clicked the item at position " + position + ", with id " + id;
                Toast.makeText(adapterView.getContext(), message, LENGTH_SHORT).show();
            }
        });
    }


    private void createDatabase() {
        db = new SchedulerDbHelper(this);
    }


    private String[] selectTodaysMeetings() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return db.selectFromMeetings(format.format(new Date()));
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
}
