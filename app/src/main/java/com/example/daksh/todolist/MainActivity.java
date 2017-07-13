package com.example.daksh.todolist;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.view.View;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements TodoAdapter.OnListCheckBoxClickedListener {

    ListView listView;
    ArrayList<Todo> itemList;
    TodoAdapter listAdapter;
    EditText searchBar;
    FloatingActionButton floatingActionButton;
    Spinner spinner;
    ArrayList<String> spinnerArrayList;
    ArrayAdapter<String> spinnerAdapter;
    Toolbar toolbar;
    String category;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_collapsing_toolbar);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("");
        spinner=(Spinner)findViewById(R.id.selectedCategories);
        spinnerArrayList=new ArrayList<>();
        spinnerArrayList.add("All Lists");
        spinnerArrayList.add("Default");
        spinnerArrayList.add("Birthday");
        spinnerArrayList.add("Personal");
        spinnerArrayList.add("Shopping");
        spinnerArrayList.add("Wishlist");
        spinnerArrayList.add("Work");
        spinnerAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, spinnerArrayList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0 )
                {
                    addNewTask();
                }
                else{
                  category=spinner.getSelectedItem().toString();
                  //  Log.i("check",category);
                    displayParticularCategoryItems(category);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        floatingActionButton=(FloatingActionButton)findViewById(R.id.newTaskButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainActivity.this,TodoDetailsByNewTask.class);
                startActivityForResult(i,IntentConstant.FROMNEWTASK);
            }
        });

     //  searchBar = (EditText) findViewById(R.id.searchBar);

        listView = (ListView) findViewById(R.id.listView);
        itemList=new ArrayList<>();


        listAdapter = new TodoAdapter(this, R.layout.listview_item, itemList,false);
        listAdapter.setOnListButtonClickedListener(this);


       listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, final View view, final int position, long id) {

                AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Delete");
                builder.setMessage("Are you sure you want to delete this task?");

                builder.setPositiveButton("YES", new AlertDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                     TodoOpenHelper todoOpenHelper=new TodoOpenHelper(MainActivity.this);
                      final  SQLiteDatabase database=todoOpenHelper.getWritableDatabase();
                        database.delete(TodoOpenHelper.TODO_TABLE_NAME,TodoOpenHelper.TODO_ID+"="+(itemList.get(position).id),null);
                        alarmCancel(itemList.get(position).id);
                        addNewTask();
                    }
                });
                builder.setNegativeButton("CANCEL", new AlertDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });


                AlertDialog dialog=builder.create();
                dialog.show();

                return true;
            }
        });
        listView.setAdapter(listAdapter);

//        searchBar.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                //Log.i("check","1");
//                listAdapter.getFilter().filter(s);
//                // Log.i("check","2");
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i=new Intent(MainActivity.this,TodoDetails.class);
                i.putExtra(IntentConstant.TODO_POSITION,itemList.get(position).id);
                startActivityForResult(i,IntentConstant.FROMEXITINGTASK);
            }
        });


    }


     @Override
    protected void onStart() {
        addNewTask();
        super.onStart();
    }


    private void displayParticularCategoryItems(String category1) {
        itemList.clear();
        TodoOpenHelper todoOpenHelper=new TodoOpenHelper(this);
        SQLiteDatabase database=todoOpenHelper.getReadableDatabase();
        Cursor cursor;
        Log.i("check","1");
        String arr[]={category1};
        cursor=database.query(TodoOpenHelper.TODO_TABLE_NAME,null,
                TodoOpenHelper.TODO_ISCHECKED+"='false' AND "+todoOpenHelper.TODO_PRIORITY+"='High' AND "
                +todoOpenHelper.TODO_CATEGORY+"=?",arr,null,null,null);
        Log.i("check","10");
        while(cursor.moveToNext())
        {
            Log.i("check","2");
            int id=cursor.getInt(cursor.getColumnIndex(TodoOpenHelper.TODO_ID));
            String title=cursor.getString(cursor.getColumnIndex(TodoOpenHelper.TODO_TITLE));
            long date_time=cursor.getLong(cursor.getColumnIndex(TodoOpenHelper.TODO_DATE_TIME));
            String date= new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date (date_time));
            String time=new java.text.SimpleDateFormat("HH:mm").format(new java.util.Date (date_time));
//            String date=cursor.getString(cursor.getColumnIndex(TodoOpenHelper.TODO_DATE));
//            String time=cursor.getString(cursor.getColumnIndex(TodoOpenHelper.TODO_TIME));
            String category=cursor.getString(cursor.getColumnIndex(TodoOpenHelper.TODO_CATEGORY));
            String priority=cursor.getString(cursor.getColumnIndex(TodoOpenHelper.TODO_PRIORITY));
            Todo t=new Todo(id,title,date,time,category,priority,date_time);
            itemList.add(t);

        }
        Log.i("check","1");
        cursor=database.query(TodoOpenHelper.TODO_TABLE_NAME,null,
                TodoOpenHelper.TODO_ISCHECKED+"='false' AND "+todoOpenHelper.TODO_PRIORITY+"='Medium' AND "
                        +todoOpenHelper.TODO_CATEGORY+"=?",arr,null,null,null);
        while(cursor.moveToNext())
        {
            int id=cursor.getInt(cursor.getColumnIndex(TodoOpenHelper.TODO_ID));
            String title=cursor.getString(cursor.getColumnIndex(TodoOpenHelper.TODO_TITLE));
            long date_time=cursor.getLong(cursor.getColumnIndex(TodoOpenHelper.TODO_DATE_TIME));
            String date= new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date (date_time));
            String time=new java.text.SimpleDateFormat("HH:mm").format(new java.util.Date (date_time));
//            String date=cursor.getString(cursor.getColumnIndex(TodoOpenHelper.TODO_DATE));
//            String time=cursor.getString(cursor.getColumnIndex(TodoOpenHelper.TODO_TIME));
            String category=cursor.getString(cursor.getColumnIndex(TodoOpenHelper.TODO_CATEGORY));
            String priority=cursor.getString(cursor.getColumnIndex(TodoOpenHelper.TODO_PRIORITY));
            Todo t=new Todo(id,title,date,time,category,priority,date_time);
            itemList.add(t);

        }
        cursor=database.query(TodoOpenHelper.TODO_TABLE_NAME,null,
                TodoOpenHelper.TODO_ISCHECKED+"='false' AND "+todoOpenHelper.TODO_PRIORITY+"='Low' AND "
                +TodoOpenHelper.TODO_CATEGORY+"=?",arr,null,null,null);
        while(cursor.moveToNext())
        {
            int id=cursor.getInt(cursor.getColumnIndex(TodoOpenHelper.TODO_ID));
            String title=cursor.getString(cursor.getColumnIndex(TodoOpenHelper.TODO_TITLE));
            long date_time=cursor.getLong(cursor.getColumnIndex(TodoOpenHelper.TODO_DATE_TIME));
            String date= new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date (date_time));
            String time=new java.text.SimpleDateFormat("HH:mm").format(new java.util.Date (date_time));
//            String date=cursor.getString(cursor.getColumnIndex(TodoOpenHelper.TODO_DATE));
//            String time=cursor.getString(cursor.getColumnIndex(TodoOpenHelper.TODO_TIME));
            String category=cursor.getString(cursor.getColumnIndex(TodoOpenHelper.TODO_CATEGORY));
            String priority=cursor.getString(cursor.getColumnIndex(TodoOpenHelper.TODO_PRIORITY));
            Todo t=new Todo(id,title,date,time,category,priority,date_time);
            itemList.add(t);

        }
        listAdapter.notifyDataSetChanged();

    }
 ////////addNewTask() METHOD
     void addNewTask() {

        itemList.clear();
        TodoOpenHelper todoOpenHelper=new TodoOpenHelper(this);
        SQLiteDatabase database=todoOpenHelper.getReadableDatabase();
        Cursor cursor;
        cursor=database.query(TodoOpenHelper.TODO_TABLE_NAME,null,
                TodoOpenHelper.TODO_ISCHECKED+"='false' AND "+todoOpenHelper.TODO_PRIORITY+"='High'", null,null,null,null);
        while(cursor.moveToNext())
        {
            int id=cursor.getInt(cursor.getColumnIndex(TodoOpenHelper.TODO_ID));
            String title=cursor.getString(cursor.getColumnIndex(TodoOpenHelper.TODO_TITLE));
            long date_time=cursor.getLong(cursor.getColumnIndex(TodoOpenHelper.TODO_DATE_TIME));
            String date= new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date (date_time));
            String time=new java.text.SimpleDateFormat("HH:mm").format(new java.util.Date (date_time));
            //String date=cursor.getString(cursor.getColumnIndex(TodoOpenHelper.TODO_DATE));
            //String time=cursor.getString(cursor.getColumnIndex(TodoOpenHelper.TODO_TIME));
            String category=cursor.getString(cursor.getColumnIndex(TodoOpenHelper.TODO_CATEGORY));
            String priority=cursor.getString(cursor.getColumnIndex(TodoOpenHelper.TODO_PRIORITY));
            Todo t=new Todo(id,title,date,time,category,priority,date_time);
            itemList.add(t);

        }
        cursor=database.query(TodoOpenHelper.TODO_TABLE_NAME,null,
                TodoOpenHelper.TODO_ISCHECKED+"='false' AND "+todoOpenHelper.TODO_PRIORITY+"='Medium'", null,null,null,null);
        while(cursor.moveToNext())
        {
            int id=cursor.getInt(cursor.getColumnIndex(TodoOpenHelper.TODO_ID));
            String title=cursor.getString(cursor.getColumnIndex(TodoOpenHelper.TODO_TITLE));
            long date_time=cursor.getLong(cursor.getColumnIndex(TodoOpenHelper.TODO_DATE_TIME));
            String date= new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date (date_time));
            String time=new java.text.SimpleDateFormat("HH:mm").format(new java.util.Date (date_time));
//            String date=cursor.getString(cursor.getColumnIndex(TodoOpenHelper.TODO_DATE));
//            String time=cursor.getString(cursor.getColumnIndex(TodoOpenHelper.TODO_TIME));
            String category=cursor.getString(cursor.getColumnIndex(TodoOpenHelper.TODO_CATEGORY));
            String priority=cursor.getString(cursor.getColumnIndex(TodoOpenHelper.TODO_PRIORITY));
            Todo t=new Todo(id,title,date,time,category,priority,date_time);
            itemList.add(t);

        }
        cursor=database.query(TodoOpenHelper.TODO_TABLE_NAME,null,
                TodoOpenHelper.TODO_ISCHECKED+"='false' AND "+todoOpenHelper.TODO_PRIORITY+"='Low'", null,null,null,null);
        while(cursor.moveToNext())
        {
            int id=cursor.getInt(cursor.getColumnIndex(TodoOpenHelper.TODO_ID));
            String title=cursor.getString(cursor.getColumnIndex(TodoOpenHelper.TODO_TITLE));
            long date_time=cursor.getLong(cursor.getColumnIndex(TodoOpenHelper.TODO_DATE_TIME));
            String date= new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date (date_time));
            String time=new java.text.SimpleDateFormat("HH:mm").format(new java.util.Date (date_time));
//            String date=cursor.getString(cursor.getColumnIndex(TodoOpenHelper.TODO_DATE));
//            String time=cursor.getString(cursor.getColumnIndex(TodoOpenHelper.TODO_TIME));
            String category=cursor.getString(cursor.getColumnIndex(TodoOpenHelper.TODO_CATEGORY));
            String priority=cursor.getString(cursor.getColumnIndex(TodoOpenHelper.TODO_PRIORITY));
            Todo t=new Todo(id,title,date,time,category,priority,date_time);
            itemList.add(t);

        }
        listAdapter.notifyDataSetChanged();


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode==IntentConstant.FROMNEWTASK){
            if(resultCode==RESULT_OK){
                addNewTask();
            }
            else if(resultCode==RESULT_CANCELED){

            }

        }
        else if(requestCode==IntentConstant.FROMEXITINGTASK){
            if(resultCode==RESULT_OK){
                addNewTask();
            }
            else if(resultCode==RESULT_CANCELED){

            }
        }

    }

    @Override
    public void listCheckBoxClicked(int position) {

        int id=itemList.get(position).id;
        TodoOpenHelper todoOpenHelper=new TodoOpenHelper(this);
        SQLiteDatabase database=todoOpenHelper.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(TodoOpenHelper.TODO_ISCHECKED,"true");
        database.update(TodoOpenHelper.TODO_TABLE_NAME, cv, TodoOpenHelper.TODO_ID + "=" + id, null);
        alarmCancel(id);
        Toast.makeText(this,"Task Finished",Toast.LENGTH_SHORT).show();
        addNewTask();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        if(id==R.id.emailus)
        {
          Intent i=new Intent();
            i.setAction(Intent.ACTION_SENDTO);
            Uri uri=Uri.parse("mailto:gargdaksh28@gmail.com");
            i.putExtra(Intent.EXTRA_SUBJECT,"Feedback");
            i.setData(uri);
            startActivity(i);
        }

        if(id==R.id.tasksfinished)
        {
            Intent intent=new Intent(this,ListViewOfCompletedTask.class);
            startActivityForResult(intent,10);
        }

        return true;
    }

    public void alarmCancel(int id){
        AlarmManager am=(AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
        Intent i=new Intent(this,AlarmReceiver.class);
        PendingIntent pendingIntent=PendingIntent.getBroadcast(this,id,i,0);
        am.cancel(pendingIntent);
    }


}
