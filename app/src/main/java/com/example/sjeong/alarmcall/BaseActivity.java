package com.example.sjeong.alarmcall;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tsengvn.typekit.TypekitContextWrapper;

/**
 * Created by kjh on 2017-05-26.
 */

public class BaseActivity extends AppCompatActivity {
    private static Typeface mTypeface = null;
    @Override
    protected void attachBaseContext(Context newBase) {

        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));

    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        if (mTypeface == null) {
            mTypeface = Typeface.createFromAsset(this.getAssets(), "fonts/aa.ttf"); // 외부폰트 사용
            mTypeface = Typeface.MONOSPACE; // 내장 폰트 사용
        }
        setGlobalFont(getWindow().getDecorView());
        // 또는
        // View view = findViewById(android.R.id.content);
        // setGlobalFont(view);
    }

    private void setGlobalFont(View view) {
        if (view != null) {
            if(view instanceof ViewGroup){
                ViewGroup vg = (ViewGroup)view;
                int vgCnt = vg.getChildCount();
                for(int i=0; i < vgCnt; i++){
                    View v = vg.getChildAt(i);
                    if(v instanceof TextView){
                        ((TextView) v).setTypeface(mTypeface);
                    }
                    setGlobalFont(v);
                }
            }
        }
    }


}
