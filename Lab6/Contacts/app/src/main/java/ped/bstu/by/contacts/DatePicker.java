package ped.bstu.by.contacts;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.widget.Button;
import android.widget.TextView;

import ped.bstu.by.contacts.DatePicker;

/**
 * Created by Egor on 11.10.2017.
 */

public class DatePicker extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    @TargetApi(Build.VERSION_CODES.N)
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        Dialog picker = new DatePickerDialog(getActivity(), this, year, month, day);
        picker.setTitle(getResources().getString(R.string.choose_date));

        return picker;
    }

    @Override
    public void onStart() {
        super.onStart();
        Button nButton = ((AlertDialog) getDialog()).getButton(DialogInterface.BUTTON_POSITIVE);
        nButton.setText(getResources().getString(R.string.ready));
    }

    @Override
    public void onDateSet(android.widget.DatePicker datePicker, int year, int month, int day) {
        TextView tv = (TextView) getActivity().findViewById(R.id.textViewDate);
        tv.setText(day + "-" + month + "-" + year);
    }

    public void show(FragmentManager supportFragmentManager, String datePicker) {
        DatePicker dp = new DatePicker();
        dp.show(getFragmentManager(), "datePicker");
    }
}
