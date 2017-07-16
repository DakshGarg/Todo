package com.example.daksh.todolist;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ReceiverCalledAfterBoot extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        TodoOpenHelper todoOpenHelper = new TodoOpenHelper(context);
        SQLiteDatabase database = todoOpenHelper.getReadableDatabase();
        Cursor cursor = database.query(TodoOpenHelper.TODO_TABLE_NAME, null, TodoOpenHelper.TODO_ISCHECKED + "='false'", null, null, null, null);
        while (cursor.moveToNext()) {
            long date_time = cursor.getLong(cursor.getColumnIndex(TodoOpenHelper.TODO_DATE_TIME));
            if (date_time > System.currentTimeMillis()) {
                String title = cursor.getString(cursor.getColumnIndex(TodoOpenHelper.TODO_TITLE));
                String time = new java.text.SimpleDateFormat("HH:mm").format(new java.util.Date (date_time));
                int id = cursor.getInt(cursor.getColumnIndex(TodoOpenHelper.TODO_ID));
                AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                Intent i = new Intent(context, AlarmReceiver.class);
                i.putExtra("title", title);
                i.putExtra("time", time);
                i.putExtra("id", id);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, i, 0);
                am.set(AlarmManager.RTC_WAKEUP, date_time, pendingIntent);
            }
        }


    }
}

