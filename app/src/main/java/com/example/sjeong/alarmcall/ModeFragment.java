package com.example.sjeong.alarmcall;

import android.app.Activity;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

public class ModeFragment extends Fragment{
    // TODO: Rename parameter arguments, choose names that match

    private DBManager dbManager;
    private View view;
    private ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // DB생성
        if (dbManager == null) {
            dbManager = new DBManager(getActivity(), "AlarmCall", null, 1);
            dbManager.ReadDB();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_mode,null);

        listView = (ListView)view.findViewById(R.id.listView);

        FloatingActionButton fab = (FloatingActionButton)view.findViewById(R.id.fab_mode);
        // 이벤트 적용
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ModeSetActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        ArrayList<Mode> modes = dbManager.getModes();
        ListAdapter listAdapter = new ListAdapter(getActivity(), R.layout.listview, modes, onClickListener);
        listView.setAdapter(listAdapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String modename = v.getTag().toString();

            switch (v.getId()) {
                case R.id.itemdetail:
                case R.id.itemmode:
                    Log.i("test tag","수정");
                    Intent intent = new Intent(getActivity(), ModeSetActivity.class);
                    intent.putExtra("Name",modename);
                    startActivity(intent);
                    break;
                case R.id.select:
                    Log.i("test tag", "현재");

                    SharedPreferences preferences = getActivity().getSharedPreferences("Mode", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();

                    if(preferences.getString("name", "").equals(modename)) {
                        if(preferences.getString("set", "off").equals("off"))
                            editor.putString("set", "on");
                        else
                            editor.putString("set", "off");
                    }
                    else {
                        Mode mode = dbManager.getMode(modename);

                        editor.putString("set", "on");
                        editor.putString("name", mode.getName());
                        editor.putInt("star", mode.getStar());
                        editor.putInt("contact", mode.getContact());
                        editor.putInt("unknown", mode.getUnknown());
                        editor.putInt("time", mode.getTime());
                        editor.putInt("count", mode.getCount());
                        editor.putInt("draw", mode.getDraw());
                    }

                    // 위젯 변경
                    Intent wintent = new Intent(getActivity(), AppWidget.class);
                    wintent.setAction("com.example.sjeong.AlarmCall.CHANGE");
                    PendingIntent pendindintent = PendingIntent.getBroadcast(getActivity(), 0, wintent, 0);
                    try {
                        pendindintent.send();
                    } catch (PendingIntent.CanceledException e) {
                        e.printStackTrace();
                    }

                    editor.commit();
                    onResume();
                    break;
            }
        }
    };
}
