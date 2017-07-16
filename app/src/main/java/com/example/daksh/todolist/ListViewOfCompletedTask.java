package com.example.daksh.todolist;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;


import java.util.ArrayList;

public class ListViewOfCompletedTask extends AppCompatActivity implements TodoAdapter.OnListCheckBoxClickedListener {
    ListView listView;
    ArrayList<Todo> itemList;
    TodoAdapter listAdapter;
    Toolbar toolbar;
    Cursor cursor;
    SQLiteDatabase database;
    TodoOpenHelper todoOpenHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view_of_completed_task);
        toolbar=(Toolbar)findViewById(R.id.toolbarr);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Finished");
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.backspace));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(11);
                finish();
            }
        });

        listView=(ListView)findViewById(R.id.listViewOfCompletedTask);
        itemList=new ArrayList<>();
        listAdapter=new TodoAdapter(this,R.layout.listview_item,itemList,true);
        listAdapter.setOnListButtonClickedListener(this);
        listView.setAdapter(listAdapter);
        addCompletedTask();

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder=new AlertDialog.Builder(ListViewOfCompletedTask.this);
                builder.setTitle("Delete");
                builder.setMessage("Are you sure you want to delete this task?");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        TodoOpenHelper todoOpenHelper=new TodoOpenHelper(ListViewOfCompletedTask.this);
                        SQLiteDatabase database=todoOpenHelper.getWritableDatabase();

                        database.delete(TodoOpenHelper.TODO_TABLE_NAME,TodoOpenHelper.TODO_ID+"="+(itemList.get(position).id),null);
                        addCompletedTask();
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
                return true;
            }
        });

        }


        private void addCompletedTask() {
        itemList.clear();
         todoOpenHelper=new TodoOpenHelper(this);
         database=todoOpenHelper.getReadableDatabase();

        cursor=database.query(TodoOpenHelper.TODO_TABLE_NAME,null
                ,TodoOpenHelper.TODO_ISCHECKED+"='true' AND "+todoOpenHelper.TODO_PRIORITY+"='High'",null,null,null,null);
        while(cursor.moveToNext())
        {
            int id=cursor.getInt(cursor.getColumnIndex(TodoOpenHelper.TODO_ID));
            String title=cursor.getString(cursor.getColumnIndex(TodoOpenHelper.TODO_TITLE));
            long date_time=cursor.getLong(cursor.getColumnIndex(TodoOpenHelper.TODO_DATE_TIME));
            String date= new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date (date_time));
            String time=new java.text.SimpleDateFormat("HH:mm").format(new java.util.Date (date_time));
            String category=cursor.getString(cursor.getColumnIndex(TodoOpenHelper.TODO_CATEGORY));
            String priority=cursor.getString(cursor.getColumnIndex(TodoOpenHelper.TODO_PRIORITY));
            Todo t=new Todo(id,title,date,time,category,priority,date_time);
            itemList.add(t);

        }
        cursor=database.query(TodoOpenHelper.TODO_TABLE_NAME,null
                ,TodoOpenHelper.TODO_ISCHECKED+"='true' AND "+todoOpenHelper.TODO_PRIORITY+"='Medium'",null,null,null,null);
        while(cursor.moveToNext())
        {
            int id=cursor.getInt(cursor.getColumnIndex(TodoOpenHelper.TODO_ID));
            String title=cursor.getString(cursor.getColumnIndex(TodoOpenHelper.TODO_TITLE));
            long date_time=cursor.getLong(cursor.getColumnIndex(TodoOpenHelper.TODO_DATE_TIME));
            String date= new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date (date_time));
            String time=new java.text.SimpleDateFormat("HH:mm").format(new java.util.Date (date_time));
            String category=cursor.getString(cursor.getColumnIndex(TodoOpenHelper.TODO_CATEGORY));
            String priority=cursor.getString(cursor.getColumnIndex(TodoOpenHelper.TODO_PRIORITY));
            Todo t=new Todo(id,title,date,time,category,priority,date_time);
            itemList.add(t);

        }
        cursor=database.query(TodoOpenHelper.TODO_TABLE_NAME,null
                ,TodoOpenHelper.TODO_ISCHECKED+"='true' AND "+todoOpenHelper.TODO_PRIORITY+"='Low'",null,null,null,null);
        while(cursor.moveToNext())
        {
            int id=cursor.getInt(cursor.getColumnIndex(TodoOpenHelper.TODO_ID));
            String title=cursor.getString(cursor.getColumnIndex(TodoOpenHelper.TODO_TITLE));
            long date_time=cursor.getLong(cursor.getColumnIndex(TodoOpenHelper.TODO_DATE_TIME));
            String date= new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date (date_time));
            String time=new java.text.SimpleDateFormat("HH:mm").format(new java.util.Date (date_time));
            String category=cursor.getString(cursor.getColumnIndex(TodoOpenHelper.TODO_CATEGORY));
            String priority=cursor.getString(cursor.getColumnIndex(TodoOpenHelper.TODO_PRIORITY));
            Todo t=new Todo(id,title,date,time,category,priority,date_time);
            itemList.add(t);

        }
            if(listAdapter.getCount()==0){
                listView.setBackgroundResource(R.drawable.finished);
            }
            else{
                listView.setBackgroundResource(0);
            }
        listAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.finished_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();

        if(id==R.id.deleteAllTask)
        {
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setTitle("Delete");
            builder.setMessage("Are you sure you want to delete All Finished Task?");
            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    todoOpenHelper=new TodoOpenHelper(ListViewOfCompletedTask.this);
                    database=todoOpenHelper.getWritableDatabase();
                    cursor=database.query(TodoOpenHelper.TODO_TABLE_NAME,null,TodoOpenHelper.TODO_ISCHECKED+"='true'",
                            null,null,null,null);
                    while(cursor.moveToNext())
                    {
                        database.delete(TodoOpenHelper.TODO_TABLE_NAME,null,null);
                    }
                    addCompletedTask();
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
        return true;
    }

    @Override
    public void listCheckBoxClicked(int position) {
        int id=itemList.get(position).id;
        String  title=itemList.get(position).title;
        String time=itemList.get(position).time;
        long date_time=itemList.get(position).date_time;
        if(System.currentTimeMillis()<date_time) {
            alarmSet(id, date_time, title, time);
        }
        todoOpenHelper=new TodoOpenHelper(this);
        database=todoOpenHelper.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(TodoOpenHelper.TODO_ISCHECKED,"false");
        database.update(TodoOpenHelper.TODO_TABLE_NAME, cv, TodoOpenHelper.TODO_ID + "=" + id, null);
        Toast.makeText(this,"Task forwaded to redo",Toast.LENGTH_SHORT).show();
        addCompletedTask();
    }
     public void alarmSet(int id,long longTime,String title,String time)
    {
        AlarmManager am=(AlarmManager)this.getSystemService(this.ALARM_SERVICE);
        Intent i=new Intent(this,AlarmReceiver.class);
        i.putExtra("title",title);
        i.putExtra("time",time);
        i.putExtra("id",id);
        PendingIntent pendingIntent=PendingIntent.getBroadcast(this,id,i,0);
        am.set(AlarmManager.RTC_WAKEUP,longTime,pendingIntent);
    }
}
