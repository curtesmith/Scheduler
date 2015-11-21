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

public class ContactsListAdapter extends BaseAdapter {
    Activity activity;
    List<ContactsListItem> list;
    OnCheckedChangeListener listener;

    public ContactsListAdapter(Activity activity, List<ContactsListItem> list, OnCheckedChangeListener listener) {
        this.activity = activity;
        this.list = list;
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

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
