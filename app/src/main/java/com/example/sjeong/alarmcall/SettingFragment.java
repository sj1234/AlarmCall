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

public class SettingFragment extends PreferenceFragment {
    // TODO: Rename parameter arguments, choose names that match

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_settings);
        Preference pAppName = (Preference) findPreference("setting_activity_id");
        Preference pAppVersion = (Preference) findPreference("setting_activity_app_version");
        CheckBoxPreference cbpAlarmReceive = (CheckBoxPreference) findPreference("setting_activity_alarm_reiceive");
        Preference pEmail = (Preference) findPreference("sending_email");
        final SwitchPreference pPushlater =  (SwitchPreference) findPreference("push_later");
        Preference pBack = (Preference) findPreference("back");
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
