package com.bsuir.dontforget;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import com.kunzisoft.switchdatetime.SwitchDateTimeDialogFragment;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

public class TaskEditor extends AppCompatActivity {
    private SwitchDateTimeDialogFragment dateTimeFragment;
    private TaskDetail task = null;
    private TextView dateView =null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_editor);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        task = (TaskDetail) getIntent().getSerializableExtra("Task");
        TextView tv = findViewById(R.id.task_title);
        tv.setText(task.getName());
        FloatingActionButton fab = findViewById(R.id.fab);
        if(!task.isStored())
            findViewById(R.id.deltask).setVisibility(View.GONE);//Hide delete button if data not from sql db
        View del = findViewById(R.id.deltask);
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Main.SQL.removeTask(task);
                //Update activity
                PlaceholderFragment.taskAdapter.updateList(Main.SQL.getTasksForTheDay(PlaceholderFragment.LastSelectedDay));
                PlaceholderFragment.taskAdapter.notifyDataSetChanged();
                PlaceholderFragment.taskAdapterAllDays.updateList(Main.SQL.getAllTasks());
                PlaceholderFragment.taskAdapterAllDays.notifyDataSetChanged();
                TaskEditor.super.finish();
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                task.setName(((TextView)findViewById(R.id.task_title)).getText().toString());
                if(task.isStored())
                    Main.SQL.updateTask(task);
                else
                    Main.SQL.addTask(task);
                PlaceholderFragment.scheduleAlarm(task,getBaseContext());
                PlaceholderFragment.taskAdapter.updateList(Main.SQL.getTasksForTheDay(PlaceholderFragment.LastSelectedDay));
                PlaceholderFragment.taskAdapter.notifyDataSetChanged();
                PlaceholderFragment.taskAdapterAllDays.updateList(Main.SQL.getAllTasks());
                PlaceholderFragment.taskAdapterAllDays.notifyDataSetChanged();
                TaskEditor.super.finish();
            }
        });
        dateTimeFragment = (SwitchDateTimeDialogFragment) getSupportFragmentManager().findFragmentByTag("TAG_DATETIME_FRAGMENT");
        if(dateTimeFragment == null) {
            dateTimeFragment = SwitchDateTimeDialogFragment.newInstance(
                    getString(R.string.label_datetime_dialog),
                    getString(android.R.string.ok),
                    getString(android.R.string.cancel)
            );
        }

        dateTimeFragment.setOnButtonClickListener(new SwitchDateTimeDialogFragment.OnButtonWithNeutralClickListener() {
            @Override
            public void onNeutralButtonClick(Date date) {
            }
            @Override
            public void onPositiveButtonClick(Date date) {
                task.setDate(date);
                TextView txtView = findViewById(R.id.datetime);
                txtView.setText(new SimpleDateFormat("EEE, d MMM yyyy HH:mm").format(date));
            }
            @Override
            public void onNegativeButtonClick(Date date) {
            }
        });
        dateView = findViewById(R.id.datetime);
        dateView.setText(new SimpleDateFormat("EEE, d MMM yyyy HH:mm").format(task.getDate()));
        dateView = findViewById(R.id.changetime);
        dateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateTimeFragment.startAtCalendarView();
                dateTimeFragment.setDefaultDateTime(new GregorianCalendar().getTime());
                dateTimeFragment.show(getSupportFragmentManager(), "TAG_DATETIME_FRAGMENT");
            }
        });
    }
}