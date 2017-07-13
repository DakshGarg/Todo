package com.example.daksh.todolist;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Daksh Garg on 6/24/2017.
 */

public class TodoDetails extends AppCompatActivity {
    EditText todoTitle;
    EditText todoDate;
    EditText todoTime;
    String title;
    String date_string;
    String time;
    String category;
    String priority;
    Spinner spinner;
    int spinner_item_position;
    ArrayAdapter<String> spinnerAdapter;
    ArrayList<String> spinnerArrayList;
    int id;
    String newtitle;
    String newdate_string;
    String newtime;
    String newcategory;
    String newPriority;
    RadioGroup radioGroup;
    RadioButton radioButton;
    int saveDate;
    int saveMonth;
    int saveYear;
    int saveHour;
    int saveMin;
    int count;
    long longTime;
    Calendar calenderDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todo_details);
        setTitle("Task");
        spinnerArrayList = new ArrayList<>();
        spinnerArrayList.add("Default");
        spinnerArrayList.add("Birthday");
        spinnerArrayList.add("Personal");
        spinnerArrayList.add("Shopping");
        spinnerArrayList.add("Wishlist");
        spinnerArrayList.add("Work");

        spinner = (Spinner) findViewById(R.id.spinner);
        spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerArrayList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        todoTitle = (EditText) findViewById(R.id.todoDetailTitleEditText);
        todoDate = (EditText) findViewById(R.id.todoDetailDateEditText);
        todoDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  Calendar calendar = Calendar.getInstance();
                int month =saveMonth;
                int year =saveYear;
                int date = saveDate;

                showDatePicker(TodoDetails.this, year, month, date);


            }
        });
        todoTime = (EditText) findViewById(R.id.todoDetailTimeEditText);
        todoTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //   Calendar calendar = Calendar.getInstance();
                int hour=saveHour;
                int min=saveMin;
                showTimePicker(hour, min);
            }
        });
        spinner = (Spinner) findViewById(R.id.spinner);
        Intent i = getIntent();
        id = i.getIntExtra(IntentConstant.TODO_POSITION, -1);

        radioGroup = (RadioGroup) findViewById(R.id.priorityRadioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                int selected_id = radioGroup.getCheckedRadioButtonId();
                radioButton = (RadioButton) findViewById(selected_id);

            }
        });

        updateTodoList();


    }

    private void showTimePicker(final int initialHour, final int initialMin) {

        TimePickerDialog timePickerDialog = new TimePickerDialog(TodoDetails.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Calendar calendar=Calendar.getInstance();
                saveHour=hourOfDay;
                saveMin=minute;
                if(calenderDate!=null){
                    calendar.set(calenderDate.get(Calendar.YEAR),calenderDate.get(Calendar.MONTH), calenderDate.get(Calendar.DATE), hourOfDay, minute, 0);
                    calenderDate = null;
                }
                else {
                    count=0;
                    calendar.set(saveYear,saveMonth,saveDate,hourOfDay, minute,0);
                }

                longTime = calendar.getTime().getTime();
                newtime=new java.text.SimpleDateFormat("HH:mm").format(new java.util.Date (longTime));
                todoTime.setText(newtime);
            }
        }, initialHour, initialMin, false);
        timePickerDialog.show();
    }

    public void showDatePicker(Context context, int initialYear, int initialMonth, int initialDate) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {


                    @Override
                    public void onDateSet(DatePicker datepicker, int year, int month, int datee) {
                        Calendar calendar = Calendar.getInstance();

                        calendar.set(year, month, datee);
                        calenderDate = calendar;
                        saveDate=datee;
                        saveMonth=month;
                        saveYear=year;
                        if(count==0)
                        {
                            calendar.set(saveYear,saveMonth,saveDate,saveHour, saveMin,0);
                            longTime=calendar.getTime().getTime();
                        }
                        newdate_string = new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date (longTime));
                        todoDate.setText(newdate_string);
                    }
                }, initialYear, initialMonth, initialDate);

        datePickerDialog.show();

    }


    private void updateTodoList() {

        TodoOpenHelper todoOpenHelper = new TodoOpenHelper(this);
        SQLiteDatabase database = todoOpenHelper.getWritableDatabase();
        Cursor cursor = database.query(TodoOpenHelper.TODO_TABLE_NAME, null, TodoOpenHelper.TODO_ID + "=" + id, null, null, null, null);
        cursor.moveToNext();
        title = cursor.getString(cursor.getColumnIndex(TodoOpenHelper.TODO_TITLE));
        long date_time=cursor.getLong(cursor.getColumnIndex(TodoOpenHelper.TODO_DATE_TIME));
        longTime=date_time;
        date_string= new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date (date_time));
        time=new java.text.SimpleDateFormat("HH:mm").format(new java.util.Date (date_time));
        ////////////////////////////////////////////////
        String datePartition[]=date_string.split("/");
        String timePartition[]=time.split(":");
        saveDate=Integer.parseInt(datePartition[0]);
        saveMonth=Integer.parseInt(datePartition[1])-1;
        saveYear=Integer.parseInt(datePartition[2]);
        saveHour=Integer.parseInt(timePartition[0]);
        saveMin=Integer.parseInt(timePartition[1]);

        category = cursor.getString(cursor.getColumnIndex(TodoOpenHelper.TODO_CATEGORY));
        priority = cursor.getString(cursor.getColumnIndex(TodoOpenHelper.TODO_PRIORITY));
        todoTitle.setText(title);
        todoDate.setText(date_string);
        todoTime.setText(time);
        spinner_item_position = 0;
        for (int i = 0; i < spinnerArrayList.size(); i++) {
            if (category.equals(spinnerArrayList.get(i))) {
                break;
            }
            spinner_item_position++;
        }
        spinner.setSelection(spinner_item_position);

        if (priority.equals("High")) {
            radioButton = (RadioButton) findViewById(R.id.highPriority);
            radioButton.setChecked(true);
        } else if (priority.equals("Medium")) {
            radioButton = (RadioButton) findViewById(R.id.mediumPriority);
            radioButton.setChecked(true);
        } else {
            radioButton = (RadioButton) findViewById(R.id.lowPriority);
            radioButton.setChecked(true);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.save) {
            newtitle = todoTitle.getText().toString();
            if (newtitle.trim().isEmpty()) {
                todoTitle.setError("can't be empty");
                return false;
            }

            newdate_string = todoDate.getText().toString();
            if (newdate_string.trim().isEmpty()) {
                todoDate.setError("can't be empty");
                return false;
            }
            newtime = todoTime.getText().toString();
            if(newtime.trim().isEmpty())
            {
                todoTime.setError("can't be empty");
                return  false;
            }

            newcategory = spinner.getSelectedItem().toString();
            newPriority = radioButton.getText().toString();
            if(System.currentTimeMillis()>=longTime)
            {

                Toast.makeText(this,"Date or Time is not Entered Properly",Toast.LENGTH_SHORT).show();
                return false;
            }
            AlarmManager am=(AlarmManager)TodoDetails.this.getSystemService(Context.ALARM_SERVICE);

            Intent i=new Intent(TodoDetails.this,AlarmReceiver.class);
            i.putExtra("title",newtitle);
            i.putExtra("time",newtime);
            i.putExtra("id",id);
            PendingIntent pendingIntent=PendingIntent.getBroadcast(TodoDetails.this,id,i,PendingIntent.FLAG_UPDATE_CURRENT);

            am.set(AlarmManager.RTC,longTime,pendingIntent);


            TodoOpenHelper todoOpenHelper = new TodoOpenHelper(TodoDetails.this);
            SQLiteDatabase database = todoOpenHelper.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(TodoOpenHelper.TODO_TITLE, newtitle);
            contentValues.put(TodoOpenHelper.TODO_DATE_TIME,longTime);
            contentValues.put(TodoOpenHelper.TODO_CATEGORY, newcategory);
            contentValues.put(TodoOpenHelper.TODO_PRIORITY, newPriority);
            database.update(TodoOpenHelper.TODO_TABLE_NAME, contentValues, TodoOpenHelper.TODO_ID + "=" + id, null);
            setResult(RESULT_OK);
            finish();
        }

        if (item.getItemId() == R.id.deleteTask) {

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Are you sure?");
                builder.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        TodoOpenHelper todoOpenHelper = new TodoOpenHelper(TodoDetails.this);
                        SQLiteDatabase database = todoOpenHelper.getWritableDatabase();
                        database.delete(TodoOpenHelper.TODO_TABLE_NAME, TodoOpenHelper.TODO_ID + "=" + id, null);

                        AlarmManager am=(AlarmManager)TodoDetails.this.getSystemService(Context.ALARM_SERVICE);
                        Intent i=new Intent(TodoDetails.this,AlarmReceiver.class);
                        PendingIntent pendingIntent=PendingIntent.getBroadcast(TodoDetails.this,id,i,0);
                        am.cancel(pendingIntent);
                        setResult(RESULT_OK);
                        finish();
                    }
                });

                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }


        return true;
    }

    @Override
    public void onBackPressed() {
        newtitle=todoTitle.getText().toString();
        newcategory = spinner.getSelectedItem().toString();
        newPriority=radioButton.getText().toString();
        newtime=todoTime.getText().toString();
        if (newtitle.trim().equals(title)& newdate_string == null && (newtime.equals(time)) && newcategory.equals(category) && newPriority.equals(priority)) {
            setResult(RESULT_OK);
            finish();
        }
        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Are you sure you want to Quit?");
            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {



                    setResult(RESULT_CANCELED);
                    finish();
                }
            });

            builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();

        }
    }
}
