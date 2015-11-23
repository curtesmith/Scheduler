package ca.brocku.cosc3p97.cs97aa.assignment2;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton.OnCheckedChangeListener;

import java.util.List;


/**
 * A class that defines a custom adapter for a list of contacts
 */
public class ContactsListAdapter extends BaseAdapter {
    Activity activity;
    List<ContactsListItem> list, selectedList;
    OnCheckedChangeListener listener;


    /**
     * Constuctor for this custom adapter
     * @param activity The activity associated with this adapter
     * @param list The data set that will be used as the source for this adapter
     * @param listener Object that will respond to the callback when items are selected
     *                 and deselected
     */
    public ContactsListAdapter(Activity activity, List<ContactsListItem> list, List<ContactsListItem> selectedList, OnCheckedChangeListener listener) {
        this.activity = activity;
        this.list = list;
        this.selectedList = selectedList;
        this.listener = listener;
    }

    /**
     * How many items are int he data represented by the adapter
     * @return the size of the list
     */
    @Override
    public int getCount() {
        return list.size();
    }


    /**
     * Get the data item associated with the specified position in the data set
     * @param index The position of the desired data
     * @return The data requested
     */
    @Override
    public Object getItem(int index) {
        return list.get(index);
    }


    /**
     * Get the rowid of the specified item in the data set
     * @param index The position of the desired data
     * @return The desired rowid
     */
    @Override
    public long getItemId(int index) {
        return index;
    }


    /**
     * Get a view that displays the data at the specified position in the data set
     * @param index The position of the desired data
     * @param view The old view to reuse if it is not null
     * @param viewGroup The parent that this view will be attached to
     * @return A view corresponding to the data at the specified position
     */
    @Override
    public View getView(int index, View view, ViewGroup viewGroup) {
        if(view == null) {
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.contacts_list_item, viewGroup, false);
        }

        CheckBox contact = (CheckBox) view.findViewById(R.id.contactCheckBox);
        contact.setText(list.get(index).name);
        contact.setOnCheckedChangeListener(listener);

        return view;
    }
}
