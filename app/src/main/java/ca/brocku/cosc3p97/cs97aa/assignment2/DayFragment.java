package ca.brocku.cosc3p97.cs97aa.assignment2;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import java.util.Date;
import java.util.List;

public class DayFragment extends Fragment {
    private Activity activity;
    private Date date;
    private List<MeetingsListItem> meetings;


    /**
     * Every Fragment class must have an empty default constructor
     */
    public DayFragment() { }


    /**
     * Since the default constructor must be empty this method will serve as a custom factory
     * to create new instances of this class.
     * @param main A reference to the main activity
     * @param date The date value that is associated with this fragment
     * @return An reference to the DayFragment that was created
     */
    public static DayFragment newInstance(Activity main, Date date) {
        DayFragment fragment = new DayFragment();
        fragment.setActivity(main);
        fragment.setDate(date);
        return fragment;
    }


    /**
     * Set the private activity field.
     * @param activity The activity reference that will be saved in the private activity field
     */
    public void setActivity(Activity activity) {
        this.activity = activity;
    }


    /**
     * Set the private date field.
     * @param date The date value that will be saved in the private date field
     */
    public void setDate(Date date) {
        this.date = date;
    }


    /**
     * Create the fragment view and attach event listeners
     * @param inflater used to build the view from the associated layout
     * @param container The container of the fragment
     * @param savedInstanceState The last saved instance of the fragment
     * @return The newly created view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_day, container, false);
        ListView listView = (ListView) view.findViewById(R.id.meetings_list);
        MeetingsListAdapter adapter = new MeetingsListAdapter(activity, selectMeetings(date));
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                MeetingsListItem item = meetings.get(position);
                ReviewMeetingDetailsDialogFragment f = ReviewMeetingDetailsDialogFragment.newInstance();
                Bundle args = new Bundle();
                args.putInt("id", item.id);
                args.putString("title", item.title);
                args.putString("date", item.date);
                args.putString("time", item.time);
                args.putString("duration", item.duration);
                args.putStringArrayList("invitees", item.invitees);
                f.setArguments(args);
                f.show(getFragmentManager(), "Review Meeting");
            }
        });

        return view;
    }


    /**
     * Retrieve the meetings from the database matching on the meeting date parameter.
     *
     * @param date The date that the selection query will use to find matching meetings
     * @return returns a list of meetings that match the requested date
     */
    private List<MeetingsListItem> selectMeetings(Date date) {
        SchedulerDbHelper db = new SchedulerDbHelper(getContext());
        meetings = db.selectFromMeetings(DateHelper.formatShortDate(date));
        db.close();
        return meetings;
    }
}