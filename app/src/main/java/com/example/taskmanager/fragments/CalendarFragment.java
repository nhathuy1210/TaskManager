package com.example.taskmanager.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskmanager.R;
import com.example.taskmanager.adapters.TaskAdapter;
import com.example.taskmanager.database.DatabaseHelper;
import com.example.taskmanager.models.Task;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CalendarFragment extends Fragment {
    private CalendarView calendarView;
    private RecyclerView recyclerView; // hiển thị danh sách task
    private TaskAdapter taskAdapter; // quản lý hiển thị task
    private DatabaseHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        calendarView = view.findViewById(R.id.calendarView);
        recyclerView = view.findViewById(R.id.recyclerViewTasks);
        dbHelper = new DatabaseHelper(getContext());

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        taskAdapter = new TaskAdapter(getContext(), new ArrayList<>());
        recyclerView.setAdapter(taskAdapter);

        calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
            //Format ngày được chọn theo định dạng dd/MM/yyyy
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, dayOfMonth);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String selectedDate = dateFormat.format(calendar.getTime());

            //Lấy danh sách task của ngày đó từ database
            List<Task> tasksForDate = dbHelper.getTasksForDate(selectedDate);
            //Cập nhật TaskAdapter với danh sách task mới
            taskAdapter.updateTasks(tasksForDate);
        });

        return view;
    }
}