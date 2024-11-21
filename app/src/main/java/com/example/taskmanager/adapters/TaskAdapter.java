package com.example.taskmanager.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskmanager.R;
import com.example.taskmanager.activities.EditTaskActivity;
import com.example.taskmanager.database.DatabaseHelper;
import com.example.taskmanager.models.Task;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private Context context;
    private List<Task> taskList;
    private DatabaseHelper dbHelper;

    public TaskAdapter(Context context, List<Task> taskList) {
        this.context = context;
        this.taskList = taskList;
        this.dbHelper = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Tạo view mới từ layout item_task
        View view = LayoutInflater.from(context).inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    //Hiển thị dữ liệu task vào các view
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {

        //Click vào item: mở EditTaskActivity với thông tin task
        Task task = taskList.get(position);
        holder.titleTextView.setText(task.getTitle());
        holder.descriptionTextView.setText(task.getDescription());
        holder.dueDateTextView.setText(task.getDueDate());
        holder.checkBox.setChecked(task.isCompleted());

        holder.itemView.setOnClickListener(v -> {
            try {
                Intent intent = new Intent(context, EditTaskActivity.class);
                intent.putExtra("task_id", task.getId());
                intent.putExtra("task_title", task.getTitle());
                intent.putExtra("task_description", task.getDescription());
                intent.putExtra("task_due_date", task.getDueDate());
                intent.putExtra("task_category_id", task.getCategoryId());
                intent.putExtra("task_completed", task.isCompleted());
                context.startActivity(intent);
            } catch (Exception e) {
                Log.e("TaskAdapter", "Error launching EditTaskActivity: " + e.getMessage());
            }
        });

        //Thay đổi checkbox: cập nhật trạng thái task trong database
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            task.setCompleted(isChecked);
            dbHelper.updateTaskStatus(task.getId(), isChecked);
        });
    }

    @Override
    // số lượng task
    public int getItemCount() {
        return taskList.size();
    }

    // cập nhật danh sách task mới
    public void updateTasks(List<Task> newTasks) {
        taskList = newTasks;
        notifyDataSetChanged();
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, descriptionTextView, dueDateTextView;
        CheckBox checkBox;

        TaskViewHolder(@NonNull View itemView) {
            // mở EditTaskActivity với thông tin task
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            dueDateTextView = itemView.findViewById(R.id.dueDateTextView);
            //cập nhật trạng thái task trong database
            checkBox = itemView.findViewById(R.id.checkBox);
        }
    }
}

