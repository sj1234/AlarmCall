package com.example.sjeong.alarmcall;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

public class ScheduleFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match

    private DBManager dbManager;
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
        View view = inflater.inflate(R.layout.fragment_schedule,null);

        listView = (ListView)view.findViewById(R.id.schedulelistView);
        FloatingActionButton fab = (FloatingActionButton)view.findViewById(R.id.fab_schedule);
        // 이벤트 적용
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ScheduleSetActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        ArrayList<Schedule> arrayList = dbManager.getSchedules();
        ScheduleListAdapter listAdapter = new ScheduleListAdapter(getActivity(), R.layout.schedulelistview, arrayList, onClickListener);
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
            String position = v.getTag().toString();
            Intent intent;

            switch (v.getId()) {
                case R.id.scheduletime:
                    intent = new Intent(getActivity(), ScheduleSetActivity.class);
                    intent.putExtra("Position", position);
                    startActivity(intent);
                    break;
                case R.id.modename:
                    intent = new Intent(getActivity(), ScheduleSetActivity.class);
                    intent.putExtra("Position", position);
                    startActivity(intent);
                    break;
                case R.id.scheduleon:
                    break;
                case R.id.scheduleoff:
                    break;
                default:
                    break;

            }
        }
    };
}
