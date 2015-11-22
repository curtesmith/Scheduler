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


    public static DayFragment newInstance(Activity main, Date date) {
        DayFragment fragment = new DayFragment();
        fragment.setActivity(main);
        fragment.setDate(date);
        return fragment;
    }


    public void setActivity(Activity activity) {
        this.activity = activity;
    }


    public void setDate(Date date) {
        this.date = date;
    }


    public DayFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_day, container, false);

        SchedulerDbHelper db = new SchedulerDbHelper(getContext());

        ListView listView = (ListView) view.findViewById(R.id.meetings_list);

        MeetingsListAdapter adapter = new MeetingsListAdapter(activity, selectMeetings(db, date));

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

        db.close();

        return view;
    }


    private List<MeetingsListItem> selectMeetings(SchedulerDbHelper db, Date date) {
        meetings = db.selectFromMeetings(DateHelper.formatShortDate(date));
        return meetings;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }

}