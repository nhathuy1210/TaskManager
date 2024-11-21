package com.example.taskmanager.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.taskmanager.R;
import com.example.taskmanager.activities.LoginActivity;
import com.example.taskmanager.database.DatabaseHelper;
import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment {
    private TextView textViewName, textViewEmail, textViewTaskCount;
    private Button buttonLogout;
    private DatabaseHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        dbHelper = new DatabaseHelper(getContext());
        initializeViews(view);
        loadUserData();
        setupLogoutButton();

        return view;
    }

    private void initializeViews(View view) {
        textViewName = view.findViewById(R.id.textViewName);
        textViewEmail = view.findViewById(R.id.textViewEmail);
        textViewTaskCount = view.findViewById(R.id.textViewTaskCount);
        buttonLogout = view.findViewById(R.id.buttonLogout);
    }

    private void loadUserData() {
        //Hiển thị thông tin người dùng
        textViewName.setText("Huy");
        textViewEmail.setText("Huy@example.com");
        //Tải số lượng task trong thread riêng
        new Thread(() -> {
            int taskCount = dbHelper.getTaskCount();
            getActivity().runOnUiThread(() ->
                    textViewTaskCount.setText("Total Tasks: " + taskCount)
            );
            //Cập nhật UI trên main thread
        }).start();
    }

    private void setupLogoutButton() {
        buttonLogout.setOnClickListener(v -> {
            //Chuyển về màn hình đăng nhập
            startActivity(new Intent(getActivity(), LoginActivity.class));
            getActivity().finish();
        });
    }
}
