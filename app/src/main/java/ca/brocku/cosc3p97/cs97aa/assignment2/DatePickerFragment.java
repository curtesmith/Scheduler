package ca.brocku.cosc3p97.cs97aa.assignment2;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Date;

/**
 * A class for creating and handling date picker actions from the user
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    DatePickerFragmentListener listener;


    /**
     * interface that defines the listener for when a date has been selected
     * by the user
     */
    public interface DatePickerFragmentListener {
        void onDateSet(Date date);
    }


    /**
     * Assign the listener to the onDateSet event
     * @param listener The listener that will accept the callback
     */
    public void setListener(DatePickerFragmentListener listener) {
        this.listener = listener;
    }


    /**
     * Build the data picker dialog and provide it with an initial value
     * @param savedInstanceState Bundle of data passed from the caller
     * @return The new date picker dialog
     */
    @NonNull
    @Override
    public Dialog onCreateDialog (Bundle savedInstanceState) {
        int year, month, day;

        year = getArguments().getInt("year");
        month = getArguments().getInt("month");
        day = getArguments().getInt("day");

        return new DatePickerDialog(getActivity(), this, year, month-1, day);
    }


    /**
     * Callback from DatePickerDialog invoked with the user has selected a date
     * @param datePicker A reference to the date picker
     * @param year The year value chosen
     * @param month The month value chosen
     * @param day The day value chosen
     */
    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        if(listener == null) {
            return;
        }

        listener.onDateSet(DateHelper.dateFromInts(year, month, day));
    }
}
