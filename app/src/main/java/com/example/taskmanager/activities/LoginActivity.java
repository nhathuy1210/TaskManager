package com.example.taskmanager.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.taskmanager.R;
import com.example.taskmanager.database.DatabaseHelper;

public class LoginActivity extends AppCompatActivity {
    private EditText emailEdit, passwordEdit;
    private DatabaseHelper dbHelper;
    private SharedPreferences preferences; //lưu trữ trạng thái đăng nhập

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbHelper = new DatabaseHelper(this);
        preferences = getSharedPreferences("TaskManager", MODE_PRIVATE);

        emailEdit = findViewById(R.id.editTextEmail);
        passwordEdit = findViewById(R.id.editTextPassword);
        Button loginButton = findViewById(R.id.buttonLogin);
        TextView registerText = findViewById(R.id.textViewRegister);

        loginButton.setOnClickListener(v -> loginUser());
        registerText.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
    }


    //Lấy thông tin đăng nhập từ người dùng:
    //Email và mật khẩu từ các trường nhập liệu
    private void loginUser() {
        String email = emailEdit.getText().toString().trim();
        String password = passwordEdit.getText().toString().trim();

        //Kiểm tra thông tin đăng nhập qua dbHelper
        if (dbHelper.checkUser(email, password)) {
            //Lưu trạng thái đăng nhập vào SharedPreferences
            preferences.edit().putBoolean("isLoggedIn", true).apply();
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        } else {
            Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show();
        }
    }
}