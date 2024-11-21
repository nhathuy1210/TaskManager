package com.example.taskmanager.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.taskmanager.models.Task;
import com.example.taskmanager.models.User;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "taskmanager.db";
    private static final int DATABASE_VERSION = 1;

    // Tables
    public static final String TABLE_TASKS = "tasks";
    public static final String TABLE_USERS = "users";

    // Common columns
    public static final String COLUMN_ID = "_id";

    // Task table columns
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_DUE_DATE = "due_date";
    public static final String COLUMN_CATEGORY_ID = "category_id";
    public static final String COLUMN_COMPLETED = "completed";

    // User table columns
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PASSWORD = "password";

    // Create tasks
    private static final String CREATE_TASKS_TABLE = "CREATE TABLE " + TABLE_TASKS + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_TITLE + " TEXT NOT NULL, "
            + COLUMN_DESCRIPTION + " TEXT, "
            + COLUMN_DUE_DATE + " TEXT, "
            + COLUMN_CATEGORY_ID + " INTEGER, "
            + COLUMN_COMPLETED + " INTEGER DEFAULT 0)";

    //// Create users
    private static final String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
            + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_NAME + " TEXT, "
            + COLUMN_EMAIL + " TEXT UNIQUE, "
            + COLUMN_PASSWORD + " TEXT)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TASKS_TABLE);
        db.execSQL(CREATE_USERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    // lấy tất cả task
    public List<Task> getAllTasks() {
        List<Task> taskList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_TASKS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Task task = new Task(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DUE_DATE)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY_ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_COMPLETED)) == 1
                );
                taskList.add(task);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return taskList;
    }

    //lấy task theo ID
    public Task getTask(int taskId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_TASKS, null,
                COLUMN_ID + " = ?",
                new String[]{String.valueOf(taskId)},
                null, null, null);

        Task task = null;
        if (cursor.moveToFirst()) {
            task = new Task(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DUE_DATE)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY_ID)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_COMPLETED)) == 1
            );
        }
        cursor.close();
        return task;
    }

    //kiểm tra đăng nhập
    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_USER_ID};
        String selection = COLUMN_EMAIL + " = ? AND " + COLUMN_PASSWORD + " = ?";
        String[] selectionArgs = {email, password};
        Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        return count > 0;
    }

    //lấy thông tin user
    public User getUser(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, null,
                COLUMN_EMAIL + " = ?", new String[]{email},
                null, null, null);

        User user = null;
        if (cursor.moveToFirst()) {
            user = new User(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL))
            );
        }
        cursor.close();
        return user;
    }

    // đếm task của user
    public int getUserTaskCount(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_TASKS +
                        " WHERE " + COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(userId)});

        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }

    //thêm task mới
    public long addTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_TITLE, task.getTitle());
        values.put(COLUMN_DESCRIPTION, task.getDescription());
        values.put(COLUMN_DUE_DATE, task.getDueDate());
        values.put(COLUMN_CATEGORY_ID, task.getCategoryId());
        values.put(COLUMN_COMPLETED, task.isCompleted() ? 1 : 0);

        return db.insert(TABLE_TASKS, null, values);
    }

    //cập nhật task
    public boolean updateTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, task.getTitle());
        values.put(COLUMN_DESCRIPTION, task.getDescription());
        values.put(COLUMN_DUE_DATE, task.getDueDate());
        values.put(COLUMN_COMPLETED, task.isCompleted());

        return db.update(TABLE_TASKS, values,
                COLUMN_ID + " = ?",
                new String[]{String.valueOf(task.getId())}) > 0;
    }

    //cập nhật trạng thái task
    public void updateTaskStatus(int taskId, boolean isCompleted) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_COMPLETED, isCompleted ? 1 : 0);

        db.update(TABLE_TASKS, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(taskId)});
    }

    //tìm kiếm task
    public List<Task> searchTasks(String query) {
        List<Task> taskList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String searchQuery = "SELECT * FROM " + TABLE_TASKS +
                " WHERE " + COLUMN_TITLE + " LIKE ? OR " +
                COLUMN_DESCRIPTION + " LIKE ?";
        String[] selectionArgs = {"%" + query + "%", "%" + query + "%"};

        Cursor cursor = db.rawQuery(searchQuery, selectionArgs);

        if (cursor.moveToFirst()) {
            do {
                Task task = new Task(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DUE_DATE)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY_ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_COMPLETED)) == 1
                );
                taskList.add(task);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return taskList;
    }

    //lấy task theo ngày
    public List<Task> getTasksForDate(String date) {
        List<Task> taskList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_TASKS, null,
                COLUMN_DUE_DATE + " = ?",
                new String[]{date},
                null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Task task = new Task(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DUE_DATE)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY_ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_COMPLETED)) == 1
                );
                taskList.add(task);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return taskList;
    }

    //xóa task
    public boolean deleteTask(int taskId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete(TABLE_TASKS, COLUMN_ID + " = ?",
                new String[]{String.valueOf(taskId)});
        return rowsAffected > 0;
    }


    //thêm user mới
    public long addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_NAME, user.getName());
        values.put(COLUMN_EMAIL, user.getEmail());
        values.put(COLUMN_PASSWORD, user.getPassword());

        return db.insert(TABLE_USERS, null, values);
    }

    //đếm số task
    public int getTaskCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_TASKS, null);

        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }
}