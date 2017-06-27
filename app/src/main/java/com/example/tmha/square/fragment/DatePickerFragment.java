package com.example.tmha.square.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;

import com.example.tmha.square.listener.ListenerDatePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by tmha on 6/27/2017.
 */

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    ListenerDatePicker mListenerDatePicker;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        calendar.set(year, month, dayOfMonth);
        String date = format.format(calendar.getTime());
        mListenerDatePicker.onChangeTime(date);
    }

    public void setListenerDatePicker(ListenerDatePicker listenerDatePicker){
        mListenerDatePicker = listenerDatePicker;
    }
}
