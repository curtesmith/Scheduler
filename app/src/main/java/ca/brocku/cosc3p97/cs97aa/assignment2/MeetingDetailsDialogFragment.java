package ca.brocku.cosc3p97.cs97aa.assignment2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A class that defines the DialogFragment which will contain the user
 * interface that will be responsible for accepting the user input
 * necessary to create a new meeting.
 */
public class MeetingDetailsDialogFragment extends DialogFragment
        implements View.OnClickListener,
        DatePickerFragment.DatePickerFragmentListener,
        TimePickerFragment.TimePickerFragmentListener,
        AdapterView.OnItemSelectedListener,
        ContactPickerDialogFragment.ContactPickerListener {

    private MeetingDetailsDialogListener listener;
    private View view;
    private String selectedDuration = "30";
    private List<ContactsListItem> selectedContacts = new ArrayList<>();


    /**
     * Instantiates a new dialog fragment
     * @return A reference to the instance of the new dialog fragment
     */
    static MeetingDetailsDialogFragment newInstance() {
        return new MeetingDetailsDialogFragment();
    }


    /**
     * Interface that defines the listener for when the positive button is clicked
     */
    public interface MeetingDetailsDialogListener {
        void onDialogPositiveClick();
    }


    /**
     * Builds the DialogFragment
     * @param savedInstanceState The last saved instance of the fragment
     * @return An instance of the new DialogFragment
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.fragment_meeting_details, null);

        buildDatePicker(view, this);
        buildTimePicker(view, this);
        buildContactsPicker(view, this);
        buildDurationSpinner(view, this);

        builder.setView(view)
                .setPositiveButton(R.string.button_create, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String title = ((EditText) view.findViewById(R.id.titleEditText)).getText().toString();
                        String date = ((TextView) view.findViewById(R.id.dateTextView)).getText().toString();
                        String time = ((TextView) view.findViewById(R.id.timeTextView)).getText().toString();
                        Integer duration = Integer.valueOf(selectedDuration);

                        SchedulerDbHelper db = new SchedulerDbHelper(getActivity());
                        db.insert(title, date, time, duration, selectedContacts);
                        db.close();

                        listener.onDialogPositiveClick();
                    }
                })
                .setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        MeetingDetailsDialogFragment.this.getDialog().cancel();
                    }
                });

        return builder.create();
    }


    /**
     * build the date picker set of widgets that will present the selected date as well as
     * provide a button that will allow the user to launch a date picker dialog to select an
     * alternative date
     * @param view The view of this DialogFragment
     * @param listener A listener for the date picker image button
     */
    private void buildDatePicker(View view, View.OnClickListener listener) {
        TextView dateTextView = (TextView) view.findViewById(R.id.dateTextView);
        dateTextView.setText(DateHelper.formatShortDate(new Date()));
        ImageButton datePickerButton = (ImageButton) view.findViewById(R.id.datePickerImageButton);
        datePickerButton.setOnClickListener(listener);
    }

    /**
     * build the time picker set of widgets that will present the selected time as well as
     * provide a button that will allow the user to launch a time picker dialog to select an
     * alternative time
     * @param view The view of this DialogFragment
     * @param listener A listener for the time picker image button
     */
    private void buildTimePicker(View view, View.OnClickListener listener) {
        TextView timeTextView = (TextView) view.findViewById(R.id.timeTextView);
        timeTextView.setText(DateHelper.formatTime(new Date()));
        ImageButton timePickerButton = (ImageButton) view.findViewById(R.id.timePickerImageButton);
        timePickerButton.setOnClickListener(listener);
    }


    /**
     * build the contacts picker set of widgets that will present the selected contacts as well as
     * provide a button that will allow the user to launch a contacts picker dialog to select an
     * alternative contacts
     * @param view The view of this DialogFragment
     * @param listener A listener for the contact picker image button
     */
    private void buildContactsPicker(View view, View.OnClickListener listener) {
        ListView contactsListView = (ListView) view.findViewById(R.id.contactsListView);
        ArrayAdapter<String> contactsAdapter = new ArrayAdapter<>(view.getContext(),
                android.R.layout.simple_list_item_1, android.R.id.text1, new String[]{"nobody"});
        contactsListView.setAdapter(contactsAdapter);

        ImageButton contactPickerButton = (ImageButton) view.findViewById(R.id.contactsImageButton);
        contactPickerButton.setOnClickListener(listener);
    }

    /**
     * build the duration spinner widget that will present the selected duration as well as
     * provide a means for the use to select an alternative duration
     * @param view The view of this DialogFragment
     * @param listener A listener for the spinner
     */
    private void buildDurationSpinner(View view, AdapterView.OnItemSelectedListener listener) {
        Spinner durationSpinner = (Spinner) view.findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.durations_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        durationSpinner.setAdapter(adapter);
        durationSpinner.setSelection(getIndex(durationSpinner, selectedDuration));
        durationSpinner.setOnItemSelectedListener(listener);
    }


    /**
     * Retrieve the index of the selected value from the spinner
     * @param spinner The spinner that has been selected
     * @param selectedValue The value that was selected from the spinner's values
     * @return The index of the selected value
     */
    private int getIndex(Spinner spinner, String selectedValue) {
        int index = -1;
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).equals(selectedValue)) {
                index = i;
            }
        }
        return index;
    }


    /**
     * The callback method invoked when implementing the View.OnClickListener interface
     * @param view A reference to the view that was clicked by the user and invoked the callback
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.datePickerImageButton:
                showDatePicker();
                break;
            case R.id.timePickerImageButton:
                showTimePicker();
                break;
            case R.id.contactsImageButton:
                showContactsPicker();
                break;
        }
    }


    /**
     * Prepare then show the date picker to the user
     */
    private void showDatePicker() {
        TextView dateTextView = (TextView) view.findViewById(R.id.dateTextView);
        int[] ints = DateHelper.splitDate(dateTextView.getText().toString());
        Bundle args = new Bundle();
        args.putInt("year", ints[DateHelper.YEAR_INDEX]);
        args.putInt("month", ints[DateHelper.MONTH_INDEX]);
        args.putInt("day", ints[DateHelper.DAY_INDEX]);
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setListener(this);
        fragment.setArguments(args);
        fragment.show(getFragmentManager(), "Date picker");
    }


    /**
     * Prepare then show the time picker to the user
     */
    private void showTimePicker() {
        TextView timeTextView = (TextView) view.findViewById(R.id.timeTextView);
        int[] ints = DateHelper.splitTime(timeTextView.getText().toString());
        Bundle args = new Bundle();
        args.putInt("hour", ints[DateHelper.HOUR_INDEX]);
        args.putInt("minute", ints[DateHelper.MINUTE_INDEX]);
        TimePickerFragment timePickerFragment = new TimePickerFragment();
        timePickerFragment.setListener(this);
        timePickerFragment.setArguments(args);
        timePickerFragment.show(getFragmentManager(), "Time picker");
    }


    /**
     * Prepare then show the contacts picker to the user
     */
    private void showContactsPicker() {
        ContactPickerDialogFragment contactPicker = ContactPickerDialogFragment.newInstance();
        contactPicker.setListener(this);
        contactPicker.show(getFragmentManager(), "Contact picker");
    }


    /**
     * Callback method in the DatePickerFragmentListener interface invoked when a date
     * has been selected from the date picker by the user
     * @param date The date that was selected by the user from the date picker
     */
    @Override
    public void onDateSet(Date date) {
        ((TextView) view.findViewById(R.id.dateTextView)).setText(DateHelper.formatShortDate(date));
    }


    /**
     * Callback method in the TimePickerFragmentListener interface invoked when a time
     * has been selected from the time picker by the user
     * @param time The time that was selected by the user from the time picker
     */
    @Override
    public void onTimeSet(Date time) {
        ((TextView) view.findViewById(R.id.timeTextView)).setText(DateHelper.formatTime(time));
    }


    /**
     * Callback method in the spinner's AdapterView interface invoked when an item has
     * been selected from the spinner
     * @param parent The parent which is an container that holds all of the items of the list
     * @param view The item selected
     * @param position The position of the view in the adapter
     * @param id The row id of teh item that is selected
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedDuration = (String) parent.getItemAtPosition(position);
    }


    /**
     * Callback method in the spinner's AdapterView interface invoked when the selection
     * disappears from this view
     * @param adapterView The spinner's adapterview
     */
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        selectedDuration = "0";
    }


    /**
     * Callback method in the ContactPickerListener interface invoked when the user has completed
     * selection of the desired contacts from the contact picker
     * @param selectedContacts A list of contacts that were selected by the user
     */
    @Override
    public void onContactsSelected(List<ContactsListItem> selectedContacts) {
        this.selectedContacts = selectedContacts;
        ListView contactsListView = (ListView) view.findViewById(R.id.contactsListView);
        ArrayAdapter<String> contactsAdapter = new ArrayAdapter<>(view.getContext(),
                android.R.layout.simple_list_item_1, android.R.id.text1, ContactsListItem.getNames(selectedContacts));
        contactsListView.setAdapter(contactsAdapter);
    }


    /**
     * Called when the DialogFragment is first attached to the context
     * @param context The context associated with the DialogFragment
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (MeetingDetailsDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    " must implement MeetingDetailsDialogListener");
        }
    }
}