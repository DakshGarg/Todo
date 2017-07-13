package com.example.daksh.todolist;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import static android.content.Context.NOTIFICATION_SERVICE;

public class AlarmReceiver extends BroadcastReceiver {
    static int i=0;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction()!=null && intent.getAction().equals("FINISH")){
            int id=intent.getIntExtra("id",-1);
            TodoOpenHelper todoOpenHelper=new TodoOpenHelper(context);
            SQLiteDatabase database=todoOpenHelper.getWritableDatabase();
            ContentValues cv=new ContentValues();
            cv.put(TodoOpenHelper.TODO_ISCHECKED,"true");
           database.update(TodoOpenHelper.TODO_TABLE_NAME,cv, TodoOpenHelper.TODO_ID + "=" + id, null);
            Toast.makeText(context,"Tasks Finished",Toast.LENGTH_SHORT).show();
            NotificationManager nManager =
                    (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            nManager.cancel(id);
            return;

        }
        // TODO: This method is called when the BroadcastReceiver is receiving
        String title=intent.getStringExtra("title");
        String time=intent.getStringExtra("time");
        int id=intent.getIntExtra("id",-1);
        Intent in = new Intent(context, AlarmReceiver.class);
        in.putExtra("id",id);
        in.setAction("FINISH");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,id, in, 0);
        NotificationCompat.Builder mBuilder=new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.icon_2)
                .setContentTitle("Task at "+time)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setTicker("Notification")
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentText(title)
                .addAction(0,"FINISH",pendingIntent);


        Intent resultIntent =new Intent(context,TodoDetails.class);
        resultIntent.putExtra(IntentConstant.TODO_POSITION,id);
        PendingIntent resultPendingIntent=PendingIntent.getActivity(context,2,resultIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager=(NotificationManager)context.getSystemService(NOTIFICATION_SERVICE);
        mNotificationManager.notify(id,mBuilder.build());
    }


}
