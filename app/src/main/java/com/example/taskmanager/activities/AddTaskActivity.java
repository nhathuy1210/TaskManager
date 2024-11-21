package com.example.taskmanager.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.taskmanager.R;
import com.example.taskmanager.database.DatabaseHelper;
import com.example.taskmanager.models.Task;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class AddTaskActivity extends AppCompatActivity {
    private EditText titleEdit, descriptionEdit;
    private TextView dueDateText;
    private DatabaseHelper dbHelper;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        //Khởi tạo đối tượng DatabaseHelper và Calendar
        dbHelper = new DatabaseHelper(this);
        calendar = Calendar.getInstance();

        //Tìm và gán các view từ layout vào biến tương ứng (titleEdit, descriptionEdit, dueDateText, các button)
        titleEdit = findViewById(R.id.editTextTitle);
        descriptionEdit = findViewById(R.id.editTextDescription);
        dueDateText = findViewById(R.id.textViewDueDate);
        Button buttonSetDate = findViewById(R.id.buttonSetDate);
        Button buttonSave = findViewById(R.id.buttonSave);

        //Thiết lập sự kiện click cho các nút:
        buttonSetDate.setOnClickListener(v -> showDatePickerDialog());
        buttonSave.setOnClickListener(v -> saveTask());
    }

    //Hiển thị dialog chọn ngày tháng
    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    updateDateDisplay();
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    //Gọi updateDateDisplay() để hiển thị ngày đã chọn
    private void updateDateDisplay() {
        //Tạo định dạng ngày tháng theo kiểu "dd/MM/yyyy"
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        dueDateText.setText(dateFormat.format(calendar.getTime()));
    }

    private void saveTask() {
        //Lấy thông tin từ các trường nhập liệu (title, description, dueDate)
        String title = titleEdit.getText().toString().trim();
        String description = descriptionEdit.getText().toString().trim();
        String dueDate = dueDateText.getText().toString();

        //Kiểm tra title không được để trống
        if (title.isEmpty()) {
            titleEdit.setError("Title is required");
            return;
        }

        //Tạo đối tượng Task mới với thông tin đã nhập
        Task task = new Task(0, title, description, dueDate, 1, false);
        long id = dbHelper.addTask(task);


        //Thêm task vào database thông qua dbHelper
        if (id > 0) {
            Toast.makeText(this, "Task saved successfully", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Error saving task", Toast.LENGTH_SHORT).show();
        }
    }
}