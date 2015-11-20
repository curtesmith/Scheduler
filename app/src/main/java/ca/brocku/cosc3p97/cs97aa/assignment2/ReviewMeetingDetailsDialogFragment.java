package ca.brocku.cosc3p97.cs97aa.assignment2;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class ReviewMeetingDetailsDialogFragment extends DialogFragment {
    private View view;
    String selectedDuration = "30";
    private int id;
    private ReviewMeetingDetailsDialogListener listener = null;

    public interface ReviewMeetingDetailsDialogListener {
        void onReviewMeetingDialogPositiveClick();
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (ReviewMeetingDetailsDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() +
                    " must implement ReviewMeetingDetailsDialogListener");
        }
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        id = getArguments().getInt("id");

        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.fragment_meeting_details, null);

        EditText title = (EditText) view.findViewById(R.id.titleEditText);
        title.setText(getArguments().getString("title"));

        TextView date = (TextView) view.findViewById(R.id.dateTextView);
        date.setText(getArguments().getString("date"));

        TextView time = (TextView) view.findViewById(R.id.timeTextView);
        time.setText(getArguments().getString("time"));

        Spinner duration = (Spinner) view.findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.durations_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        duration.setAdapter(adapter);
        duration.setSelection(getIndex(duration, getArguments().getString("duration")));

        builder.setView(view)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
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
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getDialog().cancel();
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

    public static ReviewMeetingDetailsDialogFragment newInstance() {
        ReviewMeetingDetailsDialogFragment f = new ReviewMeetingDetailsDialogFragment();
        return f;
    }
}
