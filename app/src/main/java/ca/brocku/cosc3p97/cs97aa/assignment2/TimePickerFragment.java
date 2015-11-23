package ca.brocku.cosc3p97.cs97aa.assignment2;


import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import java.util.Date;

/**
 * A class to build and handle time picker actions from the user
 */
public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    private TimePickerFragmentListener listener;


    /**
     * An interface to define the listener for when the time has been set by the user
     */
    public interface TimePickerFragmentListener {
        void onTimeSet(Date time);
    }


    /**
     * Set the listener for when the time has been selected by the user
     * @param listener A reference to the object that will receive the callback
     */
    public void setListener(TimePickerFragmentListener listener) {
        this.listener = listener;
    }


    /**
     * Build the time picker dialog
     * @param savedInstanceState A set of arguments passed from the caller
     * @return A instance of the time picker dialog
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int hour = getArguments().getInt("hour");
        int minute = getArguments().getInt("minute");

        return new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
    }


    /**
     * Callback invoked when a time has been selected by the user from the time picker dialog
     * @param timePicker The time picker dialog
     * @param hour The hour value selected
     * @param minute The minute value selected
     */
    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        if (listener == null) {
            return;
        }

        listener.onTimeSet(DateHelper.timeFromInts(hour, minute));
    }
}
