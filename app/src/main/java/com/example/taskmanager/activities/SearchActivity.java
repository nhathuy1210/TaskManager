package com.example.taskmanager.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskmanager.R;
import com.example.taskmanager.adapters.TaskAdapter;
import com.example.taskmanager.database.DatabaseHelper;
import com.example.taskmanager.models.Task;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    private EditText searchEditText;
    private RecyclerView recyclerView; //hiển thị kết quả tìm kiếm
    private TaskAdapter taskAdapter; //bộ điều khiển hiển thị task
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        dbHelper = new DatabaseHelper(this);
        searchEditText = findViewById(R.id.searchEditText);
        recyclerView = findViewById(R.id.searchRecyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        taskAdapter = new TaskAdapter(this, new ArrayList<>());
        recyclerView.setAdapter(taskAdapter);

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {} //không xử lý

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //gọi hàm searchTasks() với từ khóa mới
                searchTasks(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
            //không xử lý
        });
    }

    private void searchTasks(String query) {
        //Nhận từ khóa tìm kiếm
        List<Task> searchResults = dbHelper.searchTasks(query);
        //kết quả tìm kiếm
        taskAdapter.updateTasks(searchResults);
    }
}
