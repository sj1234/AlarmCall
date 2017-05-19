package com.example.sjeong.alarmcall;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by SJeong on 2017-05-08.
 */

public class ListAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<Mode> arraylist;
    private View.OnClickListener onClickListener;

    public ListAdapter(Context context, int layout, ArrayList<Mode> arraylist, View.OnClickListener onClickListener){
            this.context = context;
            this.layout = layout;
            this.arraylist = arraylist;
            this.onClickListener = onClickListener;
        }

        //리스트 객체 내의 item의 갯수를 반환해주는 함수. 리스트 객체의 size를 반환해주면된다
        @Override
        public int getCount() {
            return arraylist.size();
        }

        //전달받은 position의 위치에 해당하는 리스트 객체의 item를 객체 형태로 반환해주는 함수.
        @Override
        public Object getItem(int position) {
            return arraylist.get(position);
        }

        //전달받은 position의 위치에 해당하는 리스트 객체의 item의 row ID를 반환해주는 함수.
        @Override
        public long getItemId(int position) {
            return position;
        }

        // ListView의 항목들을 출력하는 함수 position : 해당되는 항목의 Adapter에서의 위치값  convertView : 재사용할 항목의 View   parent : 항목의 View들을 포함하고 있는 ListView
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.listview, parent, false);
            }

            TextView textView = (TextView) convertView.findViewById(R.id.itemmode);
            TextView textdetailView = (TextView) convertView.findViewById(R.id.itemdetail);
            ImageButton buttonselect = (ImageButton)convertView.findViewById(R.id.select);
            ImageView iconview = (ImageView)convertView.findViewById(R.id.itemicon);

            SharedPreferences preferences =  context.getSharedPreferences("Mode", Activity.MODE_PRIVATE);
            if(preferences.getString("set","off").equals("on") && preferences.getString("name"," ").equals(arraylist.get(position).getName().toString())) {
                convertView.setBackgroundResource(R.drawable.selectlistbackground);
                buttonselect.setImageResource(R.drawable.mode_start);
                iconview.setImageResource(arraylist.get(position).getDraw());
                textView.setText(arraylist.get(position).getName().toString());
                textdetailView.setText(RingInformation(arraylist.get(position).getStar())+" / "+RingInformation(arraylist.get(position).getContact()) +" / "+RingInformation(arraylist.get(position).getUnknown())+" / "+ arraylist.get(position).getTime() +"분 "+arraylist.get(position).getCount()+"번");

            }
            else {
                convertView.setBackgroundResource(R.drawable.listbackground);
                buttonselect.setImageResource(R.drawable.mode_end);
                iconview.setImageResource(arraylist.get(position).getDraw());
                textView.setText(arraylist.get(position).getName().toString());
                textdetailView.setText(RingInformation(arraylist.get(position).getStar())+" / "+RingInformation(arraylist.get(position).getContact()) +" / "+RingInformation(arraylist.get(position).getUnknown())+" / "+ arraylist.get(position).getTime() +"분 "+arraylist.get(position).getCount()+"번");

            }

            if(onClickListener != null) {
                textView.setTag(arraylist.get(position).getName().toString());
                textView.setOnClickListener(onClickListener);

                textdetailView.setTag(arraylist.get(position).getName().toString());
                textdetailView.setOnClickListener(onClickListener);

                buttonselect.setTag(arraylist.get(position).getName().toString());
                buttonselect.setOnClickListener(onClickListener);
            }
            return convertView;
    }

    public String RingInformation(int i){
        switch(i){
            case 1:
                return "벨소리";
            case 2:
                return "진동";
            case 3:
                return "무음";
            case 4:
                return "차단";
        }
        return null;
    }
}
