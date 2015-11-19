package ca.brocku.cosc3p97.cs97aa.assignment2;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class DayFragment extends Fragment {
    private Activity activity;
    private Date date;


    private OnFragmentInteractionListener mListener;

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
                String message = "You clicked the item at position " + position + ", with id " + id;
                Toast.makeText(adapterView.getContext(), message, Toast.LENGTH_SHORT).show();
            }
        });

        db.close();

        return view;
    }

    private List<MeetingsListItem> selectMeetings(SchedulerDbHelper db, Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return db.selectFromMeetings(format.format(date));
    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
