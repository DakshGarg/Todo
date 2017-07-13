package com.example.daksh.todolist;

/**
 * Created by Daksh Garg on 6/24/2017.
 */

public class Todo {

    int id;
    String title;
    String date;
    String time;
    String category;
    String priority;
    long date_time;


    public Todo(int id, String title, String date, String time, String category,String priority,long date_time) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.time = time;
        this.category = category;
        this.priority=priority;
        this.date_time=date_time;
    }
}
