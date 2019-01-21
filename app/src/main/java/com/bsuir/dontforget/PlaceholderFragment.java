
// menu
package com.bsuir.dontforget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import java.util.Calendar;
import java.util.Date;

public class PlaceholderFragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
    public static TaskAdapter taskAdapter=null;
    public static TaskAdapter taskAdapterAllDays=null;
    public static Date LastSelectedDay = null;

    public static PlaceholderFragment newInstance(int sectionNumber) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public static Date getDate(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    private void callEditor(TaskDetail task)
    {
        Intent intent = new Intent(getContext(), TaskEditor.class);
        intent.putExtra("Task", task);
        startActivity(intent);
    }

    public static void scheduleAlarm(TaskDetail task,Context context)
    {
        Long time = task.getDate().getTime()-1000*60*30;//(s)*(m) 30min
        Intent intentAlarm = new Intent(context, AlarmReciever.class);
        intentAlarm.putExtra("name",task.getName());
        intentAlarm.putExtra("id",task.getActualID());
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP,time, PendingIntent.getBroadcast(context,task.getActualID(),  intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View rootView = null;
        TextView textView = null;
        ListView listView = null;
        switch (getArguments().getInt(ARG_SECTION_NUMBER))
        {
            case 1:
                rootView = inflater.inflate(R.layout.calendar, container, false);
                listView=rootView.findViewById(R.id.list);
                if(listView.getAdapter()==null)
                listView.setAdapter(taskAdapter = new TaskAdapter(Main.SQL.getTasksForTheDay(new Date()),getContext()));
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        TaskDetail item = (TaskDetail) parent.getItemAtPosition(position);
                        callEditor(item);
                    }
                });
                CalendarView calendarView=rootView.findViewById(R.id.calendarView2);
                calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener(){
                    public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                        Long date=0L;
                        if(view.getDate() != date){
                            date = view.getDate();//Check date
                            taskAdapter.updateList(Main.SQL.getTasksForTheDay(LastSelectedDay = getDate(year,month,dayOfMonth)));
                            taskAdapter.notifyDataSetChanged();
                        }
                    }
                });

                break;
            case 2:
                rootView = inflater.inflate(R.layout.list_tasks, container, false);
                listView=rootView.findViewById(R.id.list);
                if(listView.getAdapter()==null)
                listView.setAdapter(taskAdapterAllDays = new TaskAdapter(Main.SQL.getAllTasks(),rootView.getContext()));
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        TaskDetail item = (TaskDetail) parent.getItemAtPosition(position);
                        callEditor(item);
                    }
                });
                break;
            case 3:
                rootView = inflater.inflate(R.layout.settings, container, false);
                Spinner spinner = rootView.findViewById(R.id.spinner);
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),R.array.Themes, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        switch (position)
                        {
                            case 1:
                                getActivity().setTheme(R.style.AppTheme);
                                break;
                            case 2:
                                getActivity().setTheme(R.style.Dark);
                                break;
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
        }
        return rootView;
    }
}
