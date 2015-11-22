package ca.brocku.cosc3p97.cs97aa.assignment2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class ContactPickerDialogFragment extends DialogFragment implements OnCheckedChangeListener {
    private View view;
    private ListView contactsList;
    private List<ContactsListItem> selectedContacts = new ArrayList<ContactsListItem>();
    private ContactPickerListener listener = null;


    public interface ContactPickerListener {
        void onContactsSelected(List<ContactsListItem> selectedContacts);
    }


    public void setListener(ContactPickerListener listener) {
        this.listener = listener;
    }


    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.contacts_list_view, null);

        setup();

        builder.setView(view)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (listener != null) {
                            listener.onContactsSelected(selectedContacts);
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getDialog().cancel();
                    }
                });

        return builder.create();
    }

    public void setup() {
        contactsList = (ListView) view.findViewById(R.id.contactsList);
        ContactsListAdapter adapter = new ContactsListAdapter(getActivity(), getContacts(), this);
        contactsList.setAdapter(adapter);
    }

    private List<ContactsListItem> getContacts() {
        List<ContactsListItem> contacts = new ArrayList<>();

        Cursor c = getActivity().getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        c.moveToFirst();
        while (!c.isAfterLast()) {
            contacts.add(new ContactsListItem(c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))));
            c.moveToNext();
        }

        return contacts;
    }


    public static ContactPickerDialogFragment newInstance() {
        ContactPickerDialogFragment f = new ContactPickerDialogFragment();
        return f;
    }


    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        Toast.makeText(getContext(), "on check fired with value " + isChecked, Toast.LENGTH_SHORT ).show();
        if(isChecked) {
            selectedContacts.add(new ContactsListItem(compoundButton.getText().toString()));
        } else {
            selectedContacts.remove(new ContactsListItem(compoundButton.getText().toString()));
        }
    }
}
