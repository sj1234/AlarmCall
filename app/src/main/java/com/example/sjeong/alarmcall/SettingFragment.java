package com.example.sjeong.alarmcall;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.widget.Toast;


public class SettingFragment extends PreferenceFragment {
    // TODO: Rename parameter arguments, choose names that match

    private SharedPreferences preferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_settings);
        Preference pAppName = (Preference) findPreference("setting_activity_id");
       // Preference pAppHow = (Preference) findPreference("setting_activity_how");
       // CheckBoxPreference cbpAlarmReceive = (CheckBoxPreference) findPreference("setting_activity_alarm_reiceive");
        Preference pEmail = (Preference) findPreference("sending_email");
        Preference pHow = (Preference) findPreference("setting_activity_how");
        final Preference pSetting = (Preference) findPreference("push_setting");
        final SwitchPreference pPushlater =  (SwitchPreference) findPreference("push_later");

        // 정보 받아오기 위해
        preferences= getActivity().getSharedPreferences("Later", Activity.MODE_PRIVATE);


        pAppName.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Toast.makeText(getActivity(), "NAME : Alarm Call \n VERSION : Beta ", Toast.LENGTH_SHORT).show();
                return false;
            }
        });




        pEmail.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Toast.makeText(getActivity(), "Sending Email", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        pHow.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(getActivity(), HowToUseActivity.class);
                startActivity(intent);
                return false;
            }
        });


        pSetting.setSummary("나중에 알림시간 "+preferences.getInt("time", 10)+"분");
        pSetting.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {


                final CharSequence[] items0 = {"5", "10", "15", "30"};

                AlertDialog.Builder alertDialogBuilder0 = new AlertDialog.Builder(getActivity());

                // 제목셋팅
                alertDialogBuilder0.setTitle("시간 선택");
                alertDialogBuilder0.setSingleChoiceItems(items0, -1,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                SharedPreferences.Editor editor = preferences.edit();

                                if (items0[id].equals("5")){
                                    editor.putInt("time",5);
                                    pSetting.setSummary("나중에 알림시간  5분");
                                }
                                else if (items0[id].equals("10")){
                                    editor.putInt("time",10);
                                    pSetting.setSummary("나중에 알림시간  10분");}
                                else if (items0[id].equals("15")){
                                    editor.putInt("time",15);
                                    pSetting.setSummary("나중에 알림시간  15분");}
                                else if (items0[id].equals("30")){
                                    editor.putInt("time",30);
                                    pSetting.setSummary("나중에 알림시간  30분");}

                                editor.commit();
                                dialog.dismiss();
                            }
                        });
                AlertDialog alertDialog0 = alertDialogBuilder0.create();
                // 다이얼로그 보여주기
                alertDialog0.show();
                return false;
            }
        });


        pPushlater.setChecked( preferences.getString("onoff", "off").equals("on"));
        pPushlater.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                SharedPreferences.Editor editor = preferences.edit();
                if (pPushlater.isChecked()){
                    SharedPreferences modepreferences = getActivity().getSharedPreferences("Mode", Activity.MODE_PRIVATE);
                    // 모드 켜져있을 때만 나중에 알림 가능
                    if(modepreferences.getString("set", "off").equals("on"))
                        editor.putString("onoff", "on");
                    else {
                        pPushlater.setChecked(Boolean.FALSE);
                        Toast.makeText(getActivity(), "모드 사용시에만 가능합니다.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    editor.putString("onoff", "off");
                }
                editor.commit();

                return false;
            }
        });


    }

    public boolean onPreferenceClick(Preference preference) {
        // 어플리케이션 이름
        if (preference.getKey().equals("setting_activity_id")) {
        }
        // 어플리케이션 버전
        else if (preference.getKey().equals("setting_activity_app_version")) {
        }
        // 알림 받기
        else if (preference.getKey().equals("setting_activity_alarm_reiceive")) {
        }
        else if (preference.getKey().equals("sending_email")) {
        }
        else if (preference.getKey().equals("push_later")) {
        }
        else if (preference.getKey().equals("setting_activity_how")) {
        }
        else if (preference.getKey().equals("push_setting")) {
        }
        return false;


    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
