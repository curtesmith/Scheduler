package ca.brocku.cosc3p97.cs97aa.assignment2;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * A custom adapter used to load the list of meetings for a given day as
 * used by the DayFragment class
 */
public class MeetingsListAdapter extends BaseAdapter {
    private List<MeetingsListItem> meetingsList;
    private Activity activity;


    /**
     * Constructor to create a new instance of the class
     * @param activity
     * @param list
     */
    public MeetingsListAdapter(Activity activity, List<MeetingsListItem> list) {
        this.activity = activity;
        meetingsList = list;
    }


    /**
     * Retrieve the number of items in the list that the adapter is serving
     * @return The size of the list
     */
    @Override
    public int getCount() {
        return meetingsList.size();
    }


    /**
     * Retrieve the item specified by the index passed as a parameter
     * @param index The index of the desired item
     * @return The item as an object
     */
    @Override
    public Object getItem(int index) {
        return meetingsList.get(index); //return the item from the list at position indexed by i
    }


    /**
     * Retrieve the id of the item indexed by the parameter passed as an integer
     * @param i The index of the item
     * @return The id of the item as an long type
     */
    @Override
    public long getItemId(int i) {
        return i; // just return the value that is passed
    }


    /**
     * Retrieve the view that will describe the contents of a single item in this ListView
     * @param index The index of the item that will be inserted into the view
     * @param view The reference to the view used by this list item
     * @param viewGroup The reference to the viewgroup used by this list item
     * @return a reference to the list item view
     */
    @Override
    public View getView(int index, View view, ViewGroup viewGroup) {
        if(view == null) {
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_item, viewGroup, false);
        }

        TextView time = (TextView) view.findViewById(R.id.time);
        TextView title = (TextView) view.findViewById(R.id.title);
        TextView duration = (TextView) view.findViewById(R.id.duration);

        time.setText(meetingsList.get(index).time);
        title.setText(meetingsList.get(index).title);
        duration.setText(meetingsList.get(index).duration);

        return view;
    }
}
