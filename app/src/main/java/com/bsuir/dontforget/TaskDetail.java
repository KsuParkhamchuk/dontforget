
//add data

package com.bsuir.dontforget;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TaskDetail  implements Serializable {
    private int ActualID=-1;
    private String name;
    private String description;
    private Date date;
    private boolean done=false;
    private boolean stored=false;
    public TaskDetail(String name/*,String description*/,Date date,boolean isDone)
    {
        this.name = name;
        this.ActualID = Main.SQL.tasksCount();
        this.date = date;
        //this.description = description;
        this.done = isDone;
    }

    public GregorianCalendar getGregorian(){
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(this.date);
        return cal;
    }
    public int getActualID(){return this.ActualID;}
    public String getDescription() {return this.description;}
    public Date getDate(){return this.date;}
    public String getName(){return this.name;}
    public boolean isDone() {return done;}
    public String getDateSql(){return new SimpleDateFormat("EEE, d MMM yyyy HH:mm").format(getDate());}
    public boolean isStored(){return this.stored;}

    public void setStored(boolean stored){this.stored=stored;}
    public void setDate(Date date){this.date = date;}
    public void setName(String name){this.name = name;}
    public void setActualID(int id){this.ActualID=id;}
}
