package com.example.taskmanager.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskmanager.R;
import com.example.taskmanager.adapters.TaskAdapter;
import com.example.taskmanager.database.DatabaseHelper;
import com.example.taskmanager.fragments.CalendarFragment;
import com.example.taskmanager.fragments.ProfileFragment;
import com.example.taskmanager.models.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView; // hiển thị danh sách task
    private TaskAdapter taskAdapter; //bộ điều khiển hiển thị task
    private DatabaseHelper dbHelper;
    private FloatingActionButton fabAddTask;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);
        recyclerView = findViewById(R.id.recyclerView);
        fabAddTask = findViewById(R.id.fabAddTask);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        //Cấu hình RecyclerView với LinearLayoutManager và TaskAdapter
        //Tải danh sách task
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        taskAdapter = new TaskAdapter(this, new ArrayList<>());
        recyclerView.setAdapter(taskAdapter);

        loadTasks();

        fabAddTask.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddTaskActivity.class);
            startActivity(intent);
        });

        // chuyển đổi giữa các màn hình (Home, Calendar, Profile)
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();

            if (itemId == R.id.navigation_calendar) {
                selectedFragment = new CalendarFragment();
                showFragment(selectedFragment);
            } else if (itemId == R.id.navigation_profile) {
                selectedFragment = new ProfileFragment();
                showFragment(selectedFragment);
            } else if (itemId == R.id.navigation_home) {
                hideFragments();
            }
            return true;
        });

    }

    //Hiển thị fragment được chọn
    private void showFragment(Fragment fragment) {
        recyclerView.setVisibility(View.GONE);
        fabAddTask.setVisibility(View.GONE);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    //Hiển thị lại RecyclerView và nút thêm task
    //Xóa fragment hiện tại
    private void hideFragments() {
        recyclerView.setVisibility(View.VISIBLE);
        fabAddTask.setVisibility(View.VISIBLE);
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (currentFragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .remove(currentFragment)
                    .commit();
        }
    }

    //Tải danh sách task trong thread riêng
    //Cập nhật UI trên main thread
    private void loadTasks() {
        new Thread(() -> {
            List<Task> tasks = dbHelper.getAllTasks();
            runOnUiThread(() -> taskAdapter.updateTasks(tasks));
        }).start();
    }


    @Override
    //Tải lại danh sách task khi activity được resume
    protected void onResume() {
        super.onResume();
        loadTasks();
    }

    @Override
    // tạo menu chính
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    //xử lý khi chọn mục tìm kiếm
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_search) {
            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
