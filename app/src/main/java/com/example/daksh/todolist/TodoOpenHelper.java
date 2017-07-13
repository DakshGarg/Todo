package com.example.daksh.todolist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Daksh Garg on 6/28/2017.
 */

public class TodoOpenHelper extends SQLiteOpenHelper {

    public static final String TODO_TABLE_NAME = "Todo";
    public static final String TODO_TITLE = "title";
    public static final String TODO_DATE = "date";
    public static final String TODO_CATEGORY = "category";
    public static final String TODO_ID="_id";
    public static final String TODO_TIME="time";
    public static final String TODO_ISCHECKED="isChecked";
    public static final String TODO_PRIORITY="priority";
    public static final String TODO_DATE_TIME="date_time";





    public TodoOpenHelper(Context context) {
        super(context, "Todo.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String query="CREATE TABLE "+ TODO_TABLE_NAME+" ( "+TODO_ID + " integer primary key autoincrement, "+ TODO_TITLE
                +" text, "+TODO_DATE_TIME+" integer, "+TODO_CATEGORY+" text, "+TODO_PRIORITY+" text, "+TODO_ISCHECKED
                +" text);";
        db.execSQL(query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
