package com.example.taskmanager.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.EditText;


import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.taskmanager.R;

import java.util.Calendar;
import java.util.Locale;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //Lấy ngày hiện tại từ Calendar
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        //Năm, tháng, ngày hiện tại
        return new DatePickerDialog(requireContext(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        //Format ngày theo định dạng dd/MM/yyyy
        String selectedDate = String.format(Locale.getDefault(), "%02d/%02d/%d", day, month + 1, year);
        //Tìm EditText trong Activity
        EditText editTextDueDate = requireActivity().findViewById(R.id.editTextDueDate);
        //Cập nhật text của EditText với ngày đã chọn
        editTextDueDate.setText(selectedDate);
    }
}

