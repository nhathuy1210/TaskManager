package com.example.taskmanager.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

import com.example.taskmanager.R;
import com.example.taskmanager.database.DatabaseHelper;
import com.example.taskmanager.fragments.DatePickerFragment;
import com.example.taskmanager.models.Task;



public class EditTaskActivity extends AppCompatActivity {
    private EditText editTextTitle, editTextDescription, editTextDueDate;
    private Button buttonSave;
    private DatabaseHelper dbHelper;
    private int taskId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        dbHelper = new DatabaseHelper(this);
        initializeViews();
        loadTaskData();
        setupListeners();
    }

    //Tìm và gán các view từ layout vào biến tương ứng thông qua findViewById
    private void initializeViews() {
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextDueDate = findViewById(R.id.editTextDueDate);
        buttonSave = findViewById(R.id.buttonSave);
    }

    //Lấy dữ liệu task từ Intent được truyền vào
    //Gán các giá trị vào các trường tương ứng (title, description, due date)
    private void loadTaskData() {
        Intent intent = getIntent();
        taskId = intent.getIntExtra("task_id", -1);
        editTextTitle.setText(intent.getStringExtra("task_title"));
        editTextDescription.setText(intent.getStringExtra("task_description"));
        editTextDueDate.setText(intent.getStringExtra("task_due_date"));
    }

    private void setupListeners() {
        //gọi hàm saveTask()
        buttonSave.setOnClickListener(v -> saveTask());
        //hiển thị date picker
        editTextDueDate.setOnClickListener(v -> showDatePicker());

        //Nút Delete: hiển thị dialog xác nhận xóa task
        Button buttonDelete = findViewById(R.id.buttonDelete);
        buttonDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Delete Task")
                    .setMessage("Are you sure you want to delete this task?")
                    .setPositiveButton("Delete", (dialog, which) -> {
                        dbHelper.deleteTask(taskId);
                        finish();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });
    }

    //Lấy dữ liệu từ các trường nhập liệu
    //Kiểm tra tính hợp lệ của dữ liệu
    private void saveTask() {
        String title = editTextTitle.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        String dueDate = editTextDueDate.getText().toString().trim();

        //Nếu hợp lệ: cập nhật task trong database và đóng màn hình
        if (validateInput(title, description, dueDate)) {
            Task task = new Task(taskId, title, description, dueDate, 1, false);
            dbHelper.updateTask(task);
            finish();
        }
    }

    private boolean validateInput(String title, String description, String dueDate) {

        //Kiểm tra các điều kiện:
        //Title không được trống
        //Due date không được trống
        if (title.isEmpty()) {
            editTextTitle.setError("Title is required");
            return false;
        }
        if (dueDate.isEmpty()) {
            editTextDueDate.setError("Due date is required");
            return false;
        }
        return true;
    }

    //Hiển thị dialog chọn ngày thông qua DatePickerFragment
    private void showDatePicker() {
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.show(getSupportFragmentManager(), "datePicker");
    }
}