package com.example.sjeong.alarmcall;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private FragmentTransaction transaction;
    private Fragment fragment;
    private BottomNavigationView navigation;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            transaction = getFragmentManager().beginTransaction();

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragment=new HomeFragment();
                    transaction.replace(R.id.content,fragment);
                    transaction.commit();
                    return true;
                case R.id.navigation_mode:
                    fragment=new ModeFragment();
                    transaction.replace(R.id.content,fragment);
                    transaction.commit();
                    return true;
                case R.id.navigation_schedule:
                    fragment=new ScheduleFragment();
                    transaction.replace(R.id.content,fragment);
                    transaction.commit();
                    return true;
                case R.id.navigation_setting:
                    fragment=new SettingFragment();
                    transaction.replace(R.id.content,fragment);
                    transaction.commit();
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        transaction = getFragmentManager().beginTransaction();
        fragment=new HomeFragment();
        transaction.replace(R.id.content,fragment);
        transaction.commit();
    }

}
