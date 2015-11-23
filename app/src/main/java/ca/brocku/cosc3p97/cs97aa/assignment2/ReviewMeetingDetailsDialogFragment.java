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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;


/**
 * A class to display an existing meeting in DialogFragment and to allow the user
 * to delete the meeting that is being reviewed
 */
public class ReviewMeetingDetailsDialogFragment extends DialogFragment {
    private View view;
    private int id;
    private ReviewMeetingDetailsDialogListener listener = null;


    /**
     * interface that defines the listener for the callback that will
     * be invoked when the positive button is clicked
     */
    public interface ReviewMeetingDetailsDialogListener {
        void onReviewMeetingDialogPositiveClick();
    }


    /**
     * Create a new instance of the DialogFragment
     * @return A reference to the instance that has been created
     */
    public static ReviewMeetingDetailsDialogFragment newInstance() {
        return new ReviewMeetingDetailsDialogFragment();
    }


    /**
     * Called when a DialogFragment is first attached to its context
     * @param context The context to attach to
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (ReviewMeetingDetailsDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    " must implement ReviewMeetingDetailsDialogListener");
        }
    }


    /**
     * Create the dialog and setup its properties
     * @param savedInstanceState The last saved instance of the fragment
     * @return A reference to the created DialogFragment
     */
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        id = getArguments().getInt("id");

        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.fragment_meeting_details, null);

        EditText title = (EditText) view.findViewById(R.id.titleEditText);
        title.setText(getArguments().getString("title"));
        title.setEnabled(false);

        TextView date = (TextView) view.findViewById(R.id.dateTextView);
        date.setText(getArguments().getString("date"));
        ImageButton datePicker = (ImageButton) view.findViewById(R.id.datePickerImageButton);
        datePicker.setVisibility(View.INVISIBLE);

        TextView time = (TextView) view.findViewById(R.id.timeTextView);
        time.setText(getArguments().getString("time"));
        ImageButton timePicker = (ImageButton) view.findViewById(R.id.timePickerImageButton);
        timePicker.setVisibility(View.INVISIBLE);

        Spinner duration = (Spinner) view.findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.durations_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        duration.setAdapter(adapter);
        duration.setSelection(getIndex(duration, getArguments().getString("duration")));
        duration.setEnabled(false);

        ListView contactsListView = (ListView) view.findViewById(R.id.contactsListView);

        ArrayAdapter<String> contactsAdapter = new ArrayAdapter<>(view.getContext(),
                android.R.layout.simple_list_item_1, android.R.id.text1, getArguments().getStringArrayList("invitees"));
        contactsListView.setAdapter(contactsAdapter);
        ImageButton contactPicker = (ImageButton) view.findViewById(R.id.contactsImageButton);
        contactPicker.setVisibility(View.INVISIBLE);

        builder.setView(view)
                .setPositiveButton(R.string.button_delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        SchedulerDbHelper db = new SchedulerDbHelper(getActivity());
                        db.delete(id);
                        db.close();

                        if (listener != null) {
                            listener.onReviewMeetingDialogPositiveClick();
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
     * Get the index of the selected value from the spinner
     * @param spinner The spinner that contains the selected value
     * @param selectedValue The selected value from which to find the index
     * @return The index of the item that matches the selectedValue passed as an argument
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

}
