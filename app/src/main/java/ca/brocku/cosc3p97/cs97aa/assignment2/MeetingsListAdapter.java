package ca.brocku.cosc3p97.cs97aa.assignment2;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;


public class MeetingsListAdapter extends BaseAdapter {
    private List<MeetingsListItem> meetingsList;
    private Activity activity;

    public MeetingsListAdapter(Activity activity, List<MeetingsListItem> list) {
        this.activity = activity;
        meetingsList = list;
    }

    @Override
    public int getCount() {
        return meetingsList.size();
    }

    @Override
    public Object getItem(int index) {
        return meetingsList.get(index); //return the item from the list at position indexed by i
    }

    @Override
    public long getItemId(int i) {
        return i; // just return the value that is passed since ListView just uses this for internal reasons
    }

    @Override
    public View getView(int index, View view, ViewGroup viewGroup) {
        if(view == null) {

            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_item, viewGroup, false);
        }

        TextView time = (TextView) view.findViewById(R.id.time);
        TextView title = (TextView) view.findViewById(R.id.title);

        time.setText(meetingsList.get(index).time);
        title.setText(meetingsList.get(index).title);

        return view;
    }
}
