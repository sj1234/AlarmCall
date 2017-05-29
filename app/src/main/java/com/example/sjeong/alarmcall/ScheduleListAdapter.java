
package com.example.sjeong.alarmcall;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
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
            TextView repeat = (TextView) convertView.findViewById(R.id.repeat);
            ImageButton buttonon = (ImageButton)convertView.findViewById(R.id.scheduleon);

            if(arraylist.get(position).getOnoff()==1){
                convertView.setBackgroundResource(R.drawable.selectlistbackground);
                buttonon.setImageResource(R.drawable.schedule_start);
            }
            else{
                convertView.setBackgroundResource(R.drawable.listbackground);
                buttonon.setImageResource(R.drawable.schedule_end);
            }

            time.setText(arraylist.get(position).getStart().toString()+" ~ "+arraylist.get(position).getEnd().toString());
            mode.setText("모드  "+arraylist.get(position).getModename().toString());
            repeat.setText("반복 "+RepeatWeek(arraylist.get(position)));

            if(onClickListener != null) {
                String tag = position+"/"+arraylist.get(position).getOnoff();

                time.setTag(tag);
                time.setOnClickListener(onClickListener);
                mode.setTag(tag);
                mode.setOnClickListener(onClickListener);
                repeat.setTag(tag);
                repeat.setOnClickListener(onClickListener);
                buttonon.setTag(tag);
                buttonon.setOnClickListener(onClickListener);
            }


        return convertView;

    }

    public String RepeatWeek(Schedule schedule){
        String string = "";

        if(schedule.getSun()==1)
            string = string+"일 ";
        if(schedule.getMon()==1)
            string = string+"월 ";
        if(schedule.getTue()==1)
            string = string+"화 ";
        if(schedule.getWed()==1)
            string = string+"수 ";
        if(schedule.getThu()==1)
            string = string+"목 ";
        if(schedule.getFri()==1)
            string = string+"금 ";
        if(schedule.getSat()==1)
            string = string+"토";

        if(string.equals("일 월 화 수 목 금 토"))
            string = "매일";
        if(string.equals("월 화 수 목 금 "))
            string = "주중";
        if((string.equals("일 토")))
            string="주말";

        return string;
    }
}

