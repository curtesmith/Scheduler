package ca.brocku.cosc3p97.cs97aa.assignment2;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    DatePickerFragmentListener listener;


    public interface DatePickerFragmentListener {
        void onDateSet(Date date);
    }


    public void setListener(DatePickerFragmentListener listener) {
        this.listener = listener;
    }


    @Override
    public Dialog onCreateDialog (Bundle savedInstanceState) {
        int year, month, day;

        year = getArguments().getInt("year");
        month = getArguments().getInt("month");
        day = getArguments().getInt("day");

        return new DatePickerDialog(getActivity(), this, year, month-1, day);
    }


    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        if(listener == null) {
            return;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-M-d");
        String dateString = String.format("%d-%d-%d", year, month+1, day);
        Date date = null;
        try {
            listener.onDateSet(dateFormat.parse(dateString));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
