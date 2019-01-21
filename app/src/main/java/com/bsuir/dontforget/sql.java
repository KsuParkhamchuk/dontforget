package com.bsuir.dontforget;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class sql extends SQLiteOpenHelper {
    public sql(Context context){
        super(context, "DFORGET", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table tasks ("
                + "id integer primary key autoincrement,"
                + "title text,"
                + "details text,"
                + "time text,"
                + "status integer default 0"
                + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }


    public static boolean compareToDay(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            return false;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        return sdf.format(date1).compareTo(sdf.format(date2))==0;
    }

    //getting all users from database
    public ArrayList<TaskDetail> getTasksForTheDay(Date req_day)
    {
        ArrayList<TaskDetail> tasks=new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor=db.rawQuery("SELECT * FROM tasks",null);
        if(cursor.moveToFirst())
            try {
                do {
                    Date date;
                    SimpleDateFormat sp = new SimpleDateFormat("EEE, d MMM yyyy HH:mm");
                    date = sp.parse(cursor.getString(3));
                    if(compareToDay(date,req_day)) {
                        TaskDetail task = new TaskDetail(cursor.getString(1), date, cursor.getInt(4) != 0);
                        task.setStored(true);//data from sql
                        task.setActualID(cursor.getPosition());
                        tasks.add(task);
                    }
                } while (cursor.moveToNext());
            }catch (java.text.ParseException ex)
            {
                ex.printStackTrace();
            }
        if(cursor != null && !cursor.isClosed()){
            cursor.close();
        }
        db.close();
        return tasks;
    }

    public ArrayList<TaskDetail> getAllTasks()
    {
        ArrayList<TaskDetail> tasks=new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor=db.rawQuery("SELECT * FROM tasks",null);
        if(cursor.moveToFirst())
            try {
                do {
                    Date date;
                    SimpleDateFormat sp = new SimpleDateFormat("EEE, d MMM yyyy HH:mm");
                    date = sp.parse(cursor.getString(3));
                    TaskDetail task = new TaskDetail(cursor.getString(1),date , cursor.getInt(4) != 0);
                    task.setStored(true);//data from sql
                    task.setActualID(cursor.getPosition());
                    tasks.add(task);
                } while (cursor.moveToNext());
            }catch (java.text.ParseException ex)
            {
                ex.printStackTrace();
            }
        if(cursor != null && !cursor.isClosed()){
            cursor.close();
        }
        db.close();
        return tasks;
    }

    public void removeTask(TaskDetail task)
    {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("tasks","id = ?",new String[] { String.valueOf(task.getActualID()) });
        db.close();
    }


    public void updateTask(TaskDetail task)
    {
        ContentValues cv=new ContentValues();
        cv.put("title",task.getName());
        cv.put("details",task.getDescription());
        cv.put("time",task.getDateSql());
        SQLiteDatabase db = getWritableDatabase();
        db.update("tasks", cv, "id = ?", new String[] { String.valueOf(task.getActualID()) });
        db.close();
    }

    public void addTask(TaskDetail task)
    {
        ContentValues cv=new ContentValues();
        cv.put("title",task.getName());
        cv.put("details",task.getDescription());
        cv.put("time",task.getDateSql());
        SQLiteDatabase db = getWritableDatabase();
        db.insert("tasks", null, cv);
        db.close();
    }

    public int tasksCount(){
        int result;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor=db.rawQuery("SELECT * FROM tasks",null);
        result = cursor.getCount();
        cursor.close();
        db.close();
        return result;
    }
}
