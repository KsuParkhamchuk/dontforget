package com.bsuir.dontforget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.GregorianCalendar;

public class TaskAdapter extends ArrayAdapter<TaskDetail> implements View.OnClickListener{

    private ArrayList<TaskDetail> dataSet;
    Context mContext;

    private static class ViewHolder {
        TextView Name;
        TextView txtType;
        TextView date;
    }

    public TaskAdapter(ArrayList<TaskDetail> data, Context context) {
        super(context, R.layout.list_item, data);
        this.dataSet = data;
        this.mContext=context;
    }

    public void updateList(ArrayList<TaskDetail> ndata) {
        this.dataSet.clear();
        this.dataSet.addAll(ndata);
    }

    public TaskDetail getItem(int index)
    {
        return this.dataSet.get(index);
    }

    @Override
    public void onClick(View v) {
        int position=(Integer) v.getTag();
        Object object= getItem(position);
        TaskDetail dataModel=(TaskDetail)object;
        switch (v.getId())
        {
            default://case R.id.l_item:
                Toast.makeText(getContext(),dataModel.getName(),Toast.LENGTH_LONG);
                break;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TaskDetail dataModel = getItem(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_item, parent, false);
            viewHolder.Name = convertView.findViewById(R.id.name);
            viewHolder.txtType = convertView.findViewById(R.id.type);
            viewHolder.date = convertView.findViewById(R.id.dateitem);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.Name.setText(dataModel.getName());
        viewHolder.date.setText(dataModel.getDateSql());
        return convertView;
    }
}
