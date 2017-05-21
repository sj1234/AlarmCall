package com.example.sjeong.alarmcall;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceActivity;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceManager;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;


public class SettingFragment extends PreferenceFragment {
    // TODO: Rename parameter arguments, choose names that match

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_settings);
        Preference pAppName = (Preference) findPreference("setting_activity_id");
       // Preference pAppHow = (Preference) findPreference("setting_activity_how");
       // CheckBoxPreference cbpAlarmReceive = (CheckBoxPreference) findPreference("setting_activity_alarm_reiceive");
        Preference pEmail = (Preference) findPreference("sending_email");
        Preference pHow = (Preference) findPreference("setting_activity_how");
        Preference pSetting = (Preference) findPreference("push_setting");
        final SwitchPreference pPushlater =  (SwitchPreference) findPreference("push_later");


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

        PreferenceManager preferenceManager0 = getPreferenceManager();
        preferenceManager0.setSharedPreferencesName("time");
        preferenceManager0.setSharedPreferencesMode(Activity.MODE_PRIVATE);


        pSetting.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                PreferenceManager preferenceManager0 = getPreferenceManager();
                preferenceManager0.setSharedPreferencesName("time");
                preferenceManager0.setSharedPreferencesMode(Activity.MODE_PRIVATE);
                SharedPreferences preferences0 = getPreferenceManager().getSharedPreferences();
                final SharedPreferences.Editor editor0 = preferences0.edit();

                final CharSequence[] items0 = {"5", "10", "15", "30"};

                AlertDialog.Builder alertDialogBuilder0 = new AlertDialog.Builder(getActivity());

                // 제목셋팅
                alertDialogBuilder0.setTitle("시간 선택");
                alertDialogBuilder0.setSingleChoiceItems(items0, -1,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                if (items0[id].equals("5")){editor0.putInt("time",5);}

                                else if (items0[id].equals("10")){editor0.putInt("time",10);}

                                else if (items0[id].equals("15")){editor0.putInt("time",15);}

                                else if (items0[id].equals("30")){editor0.putInt("time",30);}


                                editor0.commit();

                                dialog.dismiss();
                            }
                        });
                AlertDialog alertDialog0 = alertDialogBuilder0.create();
                // 다이얼로그 보여주기
                alertDialog0.show();

                return false;
            }
        });





        PreferenceManager preferenceManager = getPreferenceManager();
        preferenceManager.setSharedPreferencesName("Later");
        preferenceManager.setSharedPreferencesMode(Activity.MODE_PRIVATE);

        pPushlater.setChecked(preferenceManager.getSharedPreferences().getString("onoff", "off").equals("on"));


        pPushlater.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {


                PreferenceManager preferenceManager = getPreferenceManager();
                preferenceManager.setSharedPreferencesName("Later");
                preferenceManager.setSharedPreferencesMode(Activity.MODE_PRIVATE);
                SharedPreferences preferences = getPreferenceManager().getSharedPreferences();
                SharedPreferences.Editor editor = preferences.edit();
                if (pPushlater.isChecked()==true){
                    editor.putString("onoff", "on");
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
