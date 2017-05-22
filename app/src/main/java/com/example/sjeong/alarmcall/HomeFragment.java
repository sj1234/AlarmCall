package com.example.sjeong.alarmcall;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match

    private View view;
    private TextView nowname;
    private TextView nowstar;
    private TextView nowcontact;
    private TextView nowunknown;
    private TextView nowtimecount;
    private ImageView nowicon;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home,null);
        nowname = (TextView)view.findViewById(R.id.nowname);
        nowstar = (TextView)view.findViewById(R.id.nowstar);
        nowcontact = (TextView)view.findViewById(R.id.nowcontact);
        nowunknown = (TextView)view.findViewById(R.id.nowunknown);
        nowtimecount = (TextView)view.findViewById(R.id.nowtimecount);
        nowicon = (ImageView)view.findViewById(R.id.nowicon);

        // return inflater.inflate(R.layout.fragment_home, container, false);
        //return super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        NowMode();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void NowMode(){

        String name;
        int star, contact, unknown, time, count;

        // Preferences 생성
        SharedPreferences preferences = getActivity().getSharedPreferences("Mode", Activity.MODE_PRIVATE);

        if(preferences.getString("set", "off").equals("off")) {
            nowname.setText("현재 모드 : off");
            nowstar.setText("");
            nowcontact.setText("");
            nowunknown.setText("");
            nowtimecount.setText("");
            return ;
        }
        else{
            // 현재 상태 띄우기
            name = preferences.getString("name", "null");
            star = preferences.getInt("star", 4);
            contact = preferences.getInt("contact", 4);
            unknown = preferences.getInt("unknown", 4);
            time = preferences.getInt("time", 0);
            count = preferences.getInt("count", 0);

            nowname.setText(name);
            nowstar.setText("  "+RingInformation(star));
            nowcontact.setText("  "+RingInformation(contact));
            nowunknown.setText("  "+RingInformation(unknown));
            nowtimecount.setText("  "+time+"분안에 "+count+"회 이상");
            nowicon.setImageResource(preferences.getInt("draw",R.drawable.icon_empty));

        }
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
