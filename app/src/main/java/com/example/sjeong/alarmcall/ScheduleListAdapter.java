
package com.example.sjeong.alarmcall;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by SJeong on 2017-05-09.
 */

public class ScheduleListAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<Schedule> arraylist;
    private View.OnClickListener onClickListener;
    private DBManager dbManager;

    public ScheduleListAdapter(Context context, int layout, ArrayList<Schedule> arraylist, View.OnClickListener onClickListener){
        this.context = context;
        this.layout = layout;
        this.arraylist = arraylist;
        this.onClickListener = onClickListener;
    }

    @Override
    public int getCount() {
        return arraylist.size();
    }

    @Override
    public Object getItem(int position) {
        return arraylist.get(position);
    }

    @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.schedulelistview, parent, false);
            }

            TextView time = (TextView)convertView.findViewById(R.id.scheduletime);
            TextView mode = (TextView)convertView.findViewById(R.id.modename);

            time.setText(arraylist.get(position).getStart().toString()+"~"+arraylist.get(position).getEnd().toString());
            mode.setText(arraylist.get(position).getModename().toString());

            Button buttonon = (Button)convertView.findViewById(R.id.scheduleon);
            Button buttonoff = (Button)convertView.findViewById(R.id.scheduleoff);

            if(onClickListener != null) {
                time.setTag(position);
                time.setOnClickListener(onClickListener);

                mode.setTag(position);
                mode.setOnClickListener(onClickListener);

                buttonon.setTag(position);
                buttonon.setOnClickListener(onClickListener);

                buttonoff.setTag(position);
                buttonoff.setOnClickListener(onClickListener);
            }


        return convertView;

    }
}

