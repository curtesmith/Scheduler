package ca.brocku.cosc3p97.cs97aa.assignment2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


/**
 * A class for creating and handling contact picker actions from the user
 */
public class ContactPickerDialogFragment extends DialogFragment implements OnCheckedChangeListener {
    private View view;
    private ListView contactsList;
    private List<ContactsListItem> selectedContacts = new ArrayList<>();
    private ContactPickerListener listener = null;


    /**
     * Interface that defines the callback method that will be invoked when the desired
     * contacts have been selected by the user
     */
    public interface ContactPickerListener {
        void onContactsSelected(List<ContactsListItem> selectedContacts);
    }


    /**
     * Careate a new instance of the DialogFragment
     * @return A reference to the new DialogFragment
     */
    public static ContactPickerDialogFragment newInstance() {
        return new ContactPickerDialogFragment();
    }


    /**
     * Save the reference to the object that will receive the callback when the
     * contacts have been selected
     * @param listener The reference to the object that will receive the callback
     */
    public void setListener(ContactPickerListener listener) {
        this.listener = listener;
    }


    /**
     * Create the dialog so that it can be displayed to the user
     * @param savedInstanceState The last saved instance of the fragment
     * @return A reference to the dialog
     */
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.contacts_list_view, null);

        setAdapter();

        builder.setView(view)
                .setPositiveButton(R.string.button_invite, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (listener != null) {
                            listener.onContactsSelected(selectedContacts);
                        }
                    }
                })
                .setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getDialog().cancel();
                    }
                });

        return builder.create();
    }


    /**
     * Get the contacts from the content provider, load the adapter and assign it
     * to the contacts listview
     */
    public void setAdapter() {
        contactsList = (ListView) view.findViewById(R.id.contactsList);
        ContactsListAdapter adapter = new ContactsListAdapter(getActivity(), getContacts(), selectedContacts, this);
        contactsList.setAdapter(adapter);
    }


    /**
     * Get the contacts available from the associated content provider
     * @return A list of contacts
     */
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


    /**
     * Callback invoked when a checkbox in the listview is selected or deselected.
     * @param compoundButton Checkbox button that was manipulated
     * @param isChecked True if the checkbox has been checked, otherwise false
     */
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        if(isChecked) {
            selectedContacts.add(new ContactsListItem(compoundButton.getText().toString()));
        } else {
            selectedContacts.remove(new ContactsListItem(compoundButton.getText().toString()));
        }
    }
}
