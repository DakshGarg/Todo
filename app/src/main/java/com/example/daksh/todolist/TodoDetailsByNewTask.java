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
import android.widget.AdapterView;
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
import java.util.Locale;

/**
 * Created by Daksh Garg on 6/24/2017.
 */

public class TodoDetailsByNewTask extends AppCompatActivity {

    EditText todoTitle;
    EditText todoDate;
    EditText todoTime;
    String title;
    String date_string;
    String time="";
    String category;
    Spinner spinner;
    ArrayAdapter<String> spinnerAdapter;
    ArrayList<String> spinnerArrayList;
    long longTime;
    Calendar calenderDate;
    RadioGroup radioGroup;
    RadioButton radioButton;
    String priority;
    int saveDate;
    int saveMonth;
    int saveYear;
    int saveHour;
    int saveMin;
    int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todo_details);
        setTitle("New Task");
        spinnerArrayList = new ArrayList<>();
        spinnerArrayList.add("Default");
        spinnerArrayList.add("Birthday");
        spinnerArrayList.add("Personal");
        spinnerArrayList.add("Shopping");
        spinnerArrayList.add("Wishlist");
        spinnerArrayList.add("Work");
        ///////////////////////////////
        calenderDate=Calendar.getInstance();
        saveDate=calenderDate.get(Calendar.DATE);
        saveMonth=calenderDate.get(Calendar.MONTH);
        saveYear=calenderDate.get(Calendar.YEAR);
        saveHour=calenderDate.get(Calendar.HOUR_OF_DAY);
        saveMin=calenderDate.get(Calendar.MINUTE);
        ///////////////////////////
        spinner = (Spinner) findViewById(R.id.spinner);
        spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerArrayList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        radioGroup=(RadioGroup)findViewById(R.id.priorityRadioGroup);
        radioButton = (RadioButton) findViewById(R.id.highPriority);
        priority="High";

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                int selected_id=radioGroup.getCheckedRadioButtonId();
                radioButton = (RadioButton) findViewById(selected_id);
                priority=radioButton.getText().toString();
            }
        });
        todoTitle = (EditText) findViewById(R.id.todoDetailTitleEditText);
        todoDate = (EditText) findViewById(R.id.todoDetailDateEditText);
        todoDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Calendar calendar = Calendar.getInstance();
                int month =saveMonth;
                int year = saveYear;
                int date = saveDate;

                showDatePicker(TodoDetailsByNewTask.this, year, month, date);


            }
        });

        todoTime = (EditText) findViewById(R.id.todoDetailTimeEditText);
        todoTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Calendar calendar =Calendar.getInstance();
                int hour=saveHour;
                int min=saveMin;
                showTimePicker(hour,min);
            }
        });
    }

    private void showTimePicker(final int initialHour, final int initialMin) {



        final TimePickerDialog timePickerDialog = new TimePickerDialog(TodoDetailsByNewTask.this, new TimePickerDialog.OnTimeSetListener() {
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
                String check=new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new java.util.Date (longTime));
                Log.i("check",check);
                time=new java.text.SimpleDateFormat("HH:mm").format(new java.util.Date (longTime));
                todoTime.setText(time);
            }
        },initialHour,initialMin,false);
        timePickerDialog.show();
    }

    public void showDatePicker(Context context, int initialYear, int initialMonth, int initialDate) {

        // Creating datePicker dialog object
        // It requires context and listener that is used when a date is selected by the user.

        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {

                    //This method is called when the user has finished selecting a date.
                    // Arguments passed are selected year, month and day
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
                            String check=new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new java.util.Date (longTime));
                            Log.i("check",check);
                        }

                        date_string = new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date (longTime));
                        todoDate.setText(date_string);
                    }
                }, initialYear, initialMonth, initialDate);

        //Call show() to simply show the dialog
        datePickerDialog.show();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_menu_by_new_task,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.save2)
        {
             title = todoTitle.getText().toString();
            if (title.trim().isEmpty()) {
                todoTitle.setError("can't be empty");
                return false;
            }

            date_string=todoDate.getText().toString();
            if(date_string.trim().isEmpty())
            {
                todoDate.setError("can't be empty");
                return false;
            }
            time=todoTime.getText().toString();
            if(time.trim().isEmpty())
            {
                todoTime.setError("can't be empty");
                return false;
            }

            category=spinner.getSelectedItem().toString();
            if(System.currentTimeMillis()>=longTime)
            {

                Toast.makeText(this,"Date or Time is not Entered Properly",Toast.LENGTH_SHORT).show();
                return false;
            }

            priority=radioButton.getText().toString();
            TodoOpenHelper todoOpenHelper=new TodoOpenHelper(TodoDetailsByNewTask.this);
            SQLiteDatabase database=todoOpenHelper.getWritableDatabase();
            ContentValues contentValues=new ContentValues();
            contentValues.put(TodoOpenHelper.TODO_TITLE,title);
            contentValues.put(TodoOpenHelper.TODO_DATE_TIME,longTime);
            contentValues.put(TodoOpenHelper.TODO_CATEGORY,category);
             contentValues.put(TodoOpenHelper.TODO_PRIORITY,priority);
            contentValues.put(TodoOpenHelper.TODO_ISCHECKED,"false");
            database.insert(TodoOpenHelper.TODO_TABLE_NAME,null,contentValues);
            Cursor cursor=database.query(TodoOpenHelper.TODO_TABLE_NAME,null,null,null,null,null,null);
            cursor.moveToLast();
            AlarmManager am=(AlarmManager)TodoDetailsByNewTask.this.getSystemService(Context.ALARM_SERVICE);
            Intent i=new Intent(TodoDetailsByNewTask.this,AlarmReceiver.class);
            i.putExtra("title",title);
            i.putExtra("time",time);
            i.putExtra("id",cursor.getInt(cursor.getColumnIndex(TodoOpenHelper.TODO_ID)));
            PendingIntent pendingIntent=PendingIntent.getBroadcast(TodoDetailsByNewTask.this,
                    cursor.getInt(cursor.getColumnIndex(TodoOpenHelper.TODO_ID)),i,0);
            am.set(AlarmManager.RTC_WAKEUP,longTime,pendingIntent);
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
        }
        return true;
    }


    @Override
    public void onBackPressed() {

        title = todoTitle.getText().toString();
        date_string = todoDate.getText().toString();
        time=todoTime.getText().toString();
        category=spinner.getSelectedItem().toString();

        if(title.trim().isEmpty() && date_string.trim().isEmpty() && time.trim().isEmpty() && category.trim().equals("Default")
                && priority.trim().equals("High")) {
            setResult(RESULT_CANCELED);
            finish();
        }
        else{
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Are you sure?");
            builder.setMessage("Quit without saving");
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
            AlertDialog dialog=builder.create();
            dialog.show();

        }
    }

}
