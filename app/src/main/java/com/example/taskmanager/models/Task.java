package com.example.taskmanager.models;

public class Task {
    private int id;
    private String title;
    private String description;
    private String dueDate;
    private int categoryId;
    private boolean completed;

    public Task(int id, String title, String description, String dueDate, int categoryId, boolean completed) {
        // các tham số
        this.id = id;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.categoryId = categoryId;
        this.completed = completed;
    }

    // Các phương thức getter:
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getDueDate() { return dueDate; }
    public void setDueDate(String dueDate) { this.dueDate = dueDate; }

    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }

    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }
}
