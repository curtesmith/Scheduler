package ca.brocku.cosc3p97.cs97aa.assignment2;


import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    private TimePickerFragmentListener listener;

    public interface TimePickerFragmentListener {
        void onTimeSet(Date time);
    }

    public void setListener(TimePickerFragmentListener listener) {
        this.listener = listener;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int hour = getArguments().getInt("hour");
        int minute = getArguments().getInt("minute");

        return new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
    }


    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        if (listener == null) {
            return;
        }

        String time = String.format("%d:%d", hour, minute);
        SimpleDateFormat timeFormat = new SimpleDateFormat("k:m");

        try {
            listener.onTimeSet(timeFormat.parse(time));
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}
