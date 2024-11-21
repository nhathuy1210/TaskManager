package com.example.taskmanager.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.taskmanager.R;
import com.example.taskmanager.database.DatabaseHelper;
import com.example.taskmanager.models.User;

public class RegisterActivity extends AppCompatActivity {
    private EditText editTextName, editTextEmail, editTextPassword, editTextConfirmPassword;
    private Button buttonRegister;
    private TextView textViewLogin;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        dbHelper = new DatabaseHelper(this);
        initializeViews();
        setupListeners();
    }

    private void initializeViews() {
        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        buttonRegister = findViewById(R.id.buttonRegister);
        textViewLogin = findViewById(R.id.textViewLogin);
    }

    private void setupListeners() {
        buttonRegister.setOnClickListener(v -> attemptRegistration()); //Nút đăng ký
        textViewLogin.setOnClickListener(v -> {
            //Chuyển sang đăng nhập
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        });
    }

    private void attemptRegistration() {
        //Lấy thông tin từ các trường nhập liệu
        String name = editTextName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String confirmPassword = editTextConfirmPassword.getText().toString().trim();

        //Kiểm tra tính hợp lệ của dữ liệu
        if (validateInput(name, email, password, confirmPassword)) {
            User user = new User(0, name, email);
            user.setPassword(password);

            long result = dbHelper.addUser(user);
            if (result != -1) {
                Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show();
                //Sang Trang Login
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Registration failed. Please try again.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean validateInput(String name, String email, String password, String confirmPassword) {
        if (name.isEmpty()) {
            editTextName.setError("Name is required");
            return false;
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Enter a valid email address");
            return false;
        }

        if (password.isEmpty() || password.length() < 6) {
            editTextPassword.setError("Password must be at least 6 characters");
            return false;
        }

        if (!password.equals(confirmPassword)) {
            editTextConfirmPassword.setError("Passwords do not match");
            return false;
        }

        return true;
    }
}