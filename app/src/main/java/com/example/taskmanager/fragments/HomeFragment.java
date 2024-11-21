package com.example.taskmanager.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskmanager.R;
import com.example.taskmanager.activities.AddTaskActivity;
import com.example.taskmanager.adapters.TaskAdapter;
import com.example.taskmanager.database.DatabaseHelper;
import com.example.taskmanager.models.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;
    private TaskAdapter taskAdapter;
    private DatabaseHelper dbHelper;
    private FloatingActionButton fabAddTask;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        dbHelper = new DatabaseHelper(getContext());
        recyclerView = view.findViewById(R.id.recyclerView);
        fabAddTask = view.findViewById(R.id.fabAddTask);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //Khởi tạo TaskAdapter
        taskAdapter = new TaskAdapter(getContext(), new ArrayList<>());
        recyclerView.setAdapter(taskAdapter);

        //Tải danh sách task
        loadTasks();

        //sự kiện click nút thêm task
        fabAddTask.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddTaskActivity.class);
            startActivity(intent);
        });

        return view;
    }

    private void loadTasks() {
        //Tải task trong thread riêng
        new Thread(() -> {
            List<Task> tasks = dbHelper.getAllTasks();
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> taskAdapter.updateTasks(tasks));
            }
        }).start();
    }

    @Override
    public void onResume() {
        //Tải lại danh sách task khi Fragment được resume
        super.onResume();
        loadTasks();
    }
}
