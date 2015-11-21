package ca.brocku.cosc3p97.cs97aa.assignment2;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class MeetingDetailsDialogFragment extends DialogFragment
        implements View.OnClickListener, DatePickerFragment.DatePickerFragmentListener,
        TimePickerFragment.TimePickerFragmentListener, AdapterView.OnItemSelectedListener,
        ContactPickerDialogFragment.ContactPickerListener{

    private MeetingDetailsDialogListener listener;
    private View view;
    private String selectedDuration = "30";
    private List<ContactsListItem> selectedContacts = new ArrayList<>();

    private int[] TimeStringToInts(String timeString) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm");
        Date date = null;
        int[] ints = new int[2];
        try {
            date = timeFormat.parse(timeString);
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            ints[0] = c.get(Calendar.HOUR_OF_DAY);
            ints[1] = c.get(Calendar.MINUTE);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return ints;
    }


    private int[] DateStringToInts(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        int[] ints = new int[3];
        try {
            date = dateFormat.parse(dateString);
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            ints[0] = c.get(Calendar.YEAR);
            ints[1] = c.get(Calendar.MONTH) + 1;
            ints[2] = c.get(Calendar.DAY_OF_MONTH);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return ints;

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.datePickerImageButton) {
            TextView dateTextView = (TextView) this.view.findViewById(R.id.dateTextView);
            int[] ints = DateStringToInts(dateTextView.getText().toString());
            Bundle args = new Bundle();
            args.putInt("year", ints[0]);
            args.putInt("month", ints[1]);
            args.putInt("day", ints[2]);
            DatePickerFragment fragment = new DatePickerFragment();
            fragment.setListener(this);
            fragment.setArguments(args);
            fragment.show(getFragmentManager(), "Date picker");
        } else if (view.getId() == R.id.timePickerImageButton) {
            TextView timeTextView = (TextView) this.view.findViewById(R.id.timeTextView);
            int[] ints = TimeStringToInts(timeTextView.getText().toString());
            Bundle args = new Bundle();
            args.putInt("hour", ints[0]);
            args.putInt("minute", ints[1]);
            TimePickerFragment timePickerFragment = new TimePickerFragment();
            timePickerFragment.setListener(this);
            timePickerFragment.setArguments(args);
            timePickerFragment.show(getFragmentManager(), "Time picker");
        } else if(view.getId() == R.id.contactsImageButton) {
//            Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
//            startActivity(intent);
            ContactPickerDialogFragment contactPicker = ContactPickerDialogFragment.newInstance();
            contactPicker.setListener(this);
            contactPicker.show(getFragmentManager(), "Contact picker");
        }
    }

    @Override
    public void onDateSet(Date date) {
        TextView dateTextView = (TextView) view.findViewById(R.id.dateTextView);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateTextView.setText(dateFormat.format(date));
    }

    @Override
    public void onTimeSet(Date time) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("kk:mm");

        TextView timeTextView = (TextView) view.findViewById(R.id.timeTextView);
        timeTextView.setText(timeFormat.format(time));
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedDuration = (String) parent.getItemAtPosition(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        selectedDuration = "0";
    }

    @Override
    public void onContactsSelected(List<ContactsListItem> selectedContacts) {
        this.selectedContacts = selectedContacts;
        ListView contactsListView = (ListView) view.findViewById(R.id.contactsListView);
        ArrayAdapter<String> contactsAdapter = new ArrayAdapter<>(view.getContext(),
                android.R.layout.simple_list_item_1, android.R.id.text1, ContactsListItem.getNames(selectedContacts));
        contactsListView.setAdapter(contactsAdapter);
    }

    public interface MeetingDetailsDialogListener {
        void onDialogPositiveClick(String title);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (MeetingDetailsDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() +
                    " must implement MeetingDetailsDialogListener");
        }
    }


    static MeetingDetailsDialogFragment newInstance() {
        MeetingDetailsDialogFragment f = new MeetingDetailsDialogFragment();
        return f;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.fragment_meeting_details, null);

        ImageButton datePickerButton = (ImageButton) view.findViewById(R.id.datePickerImageButton);
        datePickerButton.setOnClickListener(this);

        ImageButton timePickerButton = (ImageButton) view.findViewById(R.id.timePickerImageButton);
        timePickerButton.setOnClickListener(this);

        ImageButton contactPickerButton = (ImageButton) view.findViewById(R.id.contactsImageButton);
        contactPickerButton.setOnClickListener(this);

        TextView dateTextView = (TextView) view.findViewById(R.id.dateTextView);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateTextView.setText(dateFormat.format(new Date()));

        TextView timeTextView = (TextView) view.findViewById(R.id.timeTextView);
        SimpleDateFormat timeFormat = new SimpleDateFormat("kk:mm");
        timeTextView.setText(timeFormat.format(new Date()));

        ListView contactsListView = (ListView) view.findViewById(R.id.contactsListView);

        ArrayAdapter<String> contactsAdapter = new ArrayAdapter<>(view.getContext(),
                android.R.layout.simple_list_item_1, android.R.id.text1, new String[] { "nobody"});
        contactsListView.setAdapter(contactsAdapter);


        Spinner durationSpinner = (Spinner) view.findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.durations_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        durationSpinner.setAdapter(adapter);
        durationSpinner.setSelection(getIndex(durationSpinner, selectedDuration));
        durationSpinner.setOnItemSelectedListener(this);

        builder.setView(view)
                .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String title = ((EditText) view.findViewById(R.id.titleEditText)).getText().toString();
                        String date = ((TextView) view.findViewById(R.id.dateTextView)).getText().toString();
                        String time = ((TextView) view.findViewById(R.id.timeTextView)).getText().toString();
                        String datetime = String.format("%s %s", date, time);

                        Integer duration = Integer.valueOf(selectedDuration);

                        SchedulerDbHelper db = new SchedulerDbHelper(getActivity());
                        db.insert(title, datetime, duration, selectedContacts);
                        db.close();


                        listener.onDialogPositiveClick(title);

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        MeetingDetailsDialogFragment.this.getDialog().cancel();
                    }
                });

        return builder.create();
    }

    private int getIndex(Spinner spinner, String selectedValue) {
        int index = -1;
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).equals(selectedValue)) {
                index = i;
            }
        }
        return index;
    }

}
