package com.example.daksh.todolist;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Daksh Garg on 6/24/2017.
 */

public class TodoAdapter extends ArrayAdapter<Todo> {
    Context context;
    ArrayList<Todo> todoArrayList;
    OnListCheckBoxClickedListener listener;
    boolean flag;

    public void setOnListButtonClickedListener(OnListCheckBoxClickedListener listener){
        this.listener = listener;
    }


    public TodoAdapter(@NonNull Context context, @LayoutRes int resource, ArrayList<Todo> todoArrayList,boolean flag) {
        super(context, 0);
        this.context = context;
        this.todoArrayList = todoArrayList;
        this.flag=flag;
    }

    @Override
    public int getCount() {
        return todoArrayList.size();
    }

    static class TodoViewHolder {
        TextView TodoTitleTextView;
        TextView TodoDateTextView;
        TextView TodoCategoryTextView;
        TextView TodoPriorityTextView;
        CheckBox checkBox;

        TodoViewHolder(TextView TodoTitleTextView, TextView TodoDateTextView,TextView TodoCategoryTextView,
                       TextView TodoPriorityTextView,CheckBox checkBox) {
            this.TodoTitleTextView = TodoTitleTextView;
            this.TodoDateTextView = TodoDateTextView;
            this.TodoCategoryTextView = TodoCategoryTextView;
            this.TodoPriorityTextView=TodoPriorityTextView;
            this.checkBox=checkBox;


        }
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.listview_item, null);
            TextView TodoTitleTextView = (TextView) convertView.findViewById(R.id.todoTitleTextView);
            TextView TodoDateTextView = (TextView) convertView.findViewById(R.id.todoDateTextView);
            TextView TodoCategoryTextView = (TextView) convertView.findViewById(R.id.todoCategoryTextView);
            TextView TodoPriorityTextView=(TextView)convertView.findViewById(R.id.priorityTextView);
            CheckBox checkBox=(CheckBox)convertView.findViewById(R.id.checkBox);

            TodoViewHolder todoViewHolder = new TodoViewHolder(TodoTitleTextView, TodoDateTextView, TodoCategoryTextView
                    ,TodoPriorityTextView,checkBox);
            convertView.setTag(todoViewHolder);
        }
        final TodoViewHolder todoViewHolder = (TodoViewHolder) convertView.getTag();

        if(todoViewHolder.checkBox.isChecked())
        {
            todoViewHolder.checkBox.setChecked(false);
        }
        Todo t = todoArrayList.get(position);
        long longTime=t.date_time;
        todoViewHolder.TodoTitleTextView.setText(t.title);
        if (t.time.equals("") || t.time == null)
            todoViewHolder.TodoDateTextView.setText(t.date);
        else {
            todoViewHolder.TodoDateTextView.setText(t.date + ", " + t.time);
        }


        todoViewHolder.TodoCategoryTextView.setText(t.category);
        if(t.priority.equals("High"))
        {
            todoViewHolder.TodoPriorityTextView.setBackgroundResource(R.color.high);
        }
        else if(t.priority.equals("Medium"))
        {
            todoViewHolder.TodoPriorityTextView.setBackgroundResource(R.color.medium);
        }
        else{
            todoViewHolder.TodoPriorityTextView.setBackgroundResource(R.color.low);
        }
        if(System.currentTimeMillis()>longTime)
        {
            todoViewHolder.TodoDateTextView.setTextColor(ContextCompat.getColor(context, R.color.red));
            todoViewHolder.TodoTitleTextView.setTextColor(ContextCompat.getColor(context, R.color.greyy));
            todoViewHolder.TodoPriorityTextView.setTextColor(ContextCompat.getColor(context, R.color.greyy));
        }
        else{
            todoViewHolder.TodoDateTextView.setTextColor(ContextCompat.getColor(context, R.color.blue));
            todoViewHolder.TodoTitleTextView.setTextColor(ContextCompat.getColor(context, R.color.black));
            todoViewHolder.TodoPriorityTextView.setTextColor(ContextCompat.getColor(context, R.color.greyy));
        }
        todoViewHolder.checkBox.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                listener.listCheckBoxClicked(position);

            }
        });
        if(flag)
        {
            todoViewHolder.checkBox.setChecked(true);
        }
        return convertView;
    }


    interface OnListCheckBoxClickedListener {

        void listCheckBoxClicked(int position);
    }
}