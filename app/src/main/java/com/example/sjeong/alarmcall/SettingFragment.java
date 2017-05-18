package com.example.sjeong.alarmcall;

import android.app.Fragment;
import android.content.Context;
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
