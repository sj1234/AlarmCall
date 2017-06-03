package com.example.sjeong.alarmcall;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

public class ModeSetActivity extends AppCompatActivity implements View.OnClickListener{

    private Context context;
    private String Tag="test ModeSetActive";
    private DBManager dbManager;
    private Mode mode;
    private String name, sms_string, picture_string;
    private TextView startxt,contacttxt,unknowntxt;
    private EditText modename, sms_detail;
    private View iconview, smsview;
    private ImageButton icon;


    protected boolean shouldAskPermissions() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    @TargetApi(23)
    protected void askPermissions() {
        String[] permissions = {
                "android.permission.READ_EXTERNAL_STORAGE",
                "android.permission.WRITE_EXTERNAL_STORAGE"
        };
        int requestCode = 200;
        requestPermissions(permissions, requestCode);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (shouldAskPermissions()) {
            askPermissions();
        }

        setContentView(R.layout.activity_mode_set);
        context = this;

        // DB생성
        if(dbManager==null) {
            dbManager = new DBManager(ModeSetActivity.this, "AlarmCall", null, 1);
            dbManager.ReadDB();
        }

        Intent intent = getIntent();
        name = intent.getStringExtra("Name");

        icon = (ImageButton)findViewById(R.id.mode_icon);
        icon.setOnClickListener((View.OnClickListener)this);

        ImageButton star = (ImageButton) this.findViewById(R.id.star);
        star.setOnClickListener((View.OnClickListener) this);
        ImageButton contact = (ImageButton) this.findViewById(R.id.contact);
        contact.setOnClickListener((View.OnClickListener) this);
        ImageButton unknown = (ImageButton) this.findViewById(R.id.unknown);
        unknown.setOnClickListener((View.OnClickListener) this);
        ImageButton sms = (ImageButton) this.findViewById(R.id.sms);
        sms.setOnClickListener((View.OnClickListener) this);
        Button set = (Button) this.findViewById(R.id.set);
        set.setOnClickListener((View.OnClickListener) this);
        Button delete = (Button) this.findViewById(R.id.delete);
        delete.setOnClickListener((View.OnClickListener) this);

        modename = (EditText) findViewById(R.id.modename);
        startxt = (TextView) findViewById(R.id.startxt);
        startxt.setOnClickListener(this);
        contacttxt = (TextView) findViewById(R.id.contacttxt);
        contacttxt.setOnClickListener(this);
        unknowntxt = (TextView) findViewById(R.id.unknowntxt);
        unknowntxt.setOnClickListener(this);

        mode = new Mode();

        if(name != null) {
            mode=dbManager.getMode(name);
            modename.setText(name);
            startxt.setText( RingInformation(mode.getStar()));
            contacttxt.setText(RingInformation(mode.getContact()));
            unknowntxt.setText(RingInformation(mode.getUnknown()));
            if(mode.getDraw()==0)
            {
                File imgFile = new  File(mode.getPicture());
                if(imgFile.exists()){
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    RoundedAvatarDrawable tmpRoundedAvatarDrawable = new RoundedAvatarDrawable(myBitmap);
                    icon.setImageDrawable(tmpRoundedAvatarDrawable);
                }
            }
            else
                icon.setImageResource(mode.getDraw());
            sms_string = mode.getSms();
            picture_string = mode.getPicture();
        }
        else {
            mode = new Mode();
            modename.setHint("모드 이름");
            startxt.setText("수신음 선택");
            contacttxt.setText("수신음 선택");
            unknowntxt.setText("수신음 선택");
            icon.setImageResource(R.drawable.icon_empty);
            sms_string = "지금은 전화를 받을 수 없습니다.";
            mode.setSms(sms_string);
            delete.setText("취소");
        }

       Spinner s1 = (Spinner)findViewById(R.id.timespinner);

        s1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                mode.setTime(Integer.parseInt(""+parent.getItemAtPosition(position)));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        s1.setSelection(mode.getTime()-1);

        Spinner s2 = (Spinner)findViewById(R.id.countspinner);

        s2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                mode.setCount(Integer.parseInt(""+parent.getItemAtPosition(position)));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        s2.setSelection(mode.getCount()-1);
    }

    @Override
    public void onClick(View v) {
        SharedPreferences preferences =context.getSharedPreferences("Mode", Activity.MODE_PRIVATE);

        switch (v.getId()) {
            case R.id.startxt:
            case R.id.star:
                final CharSequence[] items1 = {"벨소리", "진동", "무음", "차단"};
                AlertDialog.Builder alertDialogBuilder1 = new AlertDialog.Builder(context);

                // 제목셋팅
                alertDialogBuilder1.setTitle("모드 선택 목록");
                alertDialogBuilder1.setSingleChoiceItems(items1, -1,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                startxt.setText(items1[id]);
                                if (items1[id].equals("벨소리"))
                                    mode.setStar(1);
                                else if (items1[id].equals("진동"))
                                    mode.setStar(2);
                                else if (items1[id].equals("무음"))
                                    mode.setStar(3);
                                else if (items1[id].equals("차단"))
                                    mode.setStar(4);

                                // 프로그램을 종료한다
                                Toast.makeText(getApplicationContext(), items1[id] + " 선택했습니다.", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        });
                // 다이얼로그 생성
                AlertDialog alertDialog1 = alertDialogBuilder1.create();
                // 다이얼로그 보여주기
                alertDialog1.show();
                break;
            case R.id.contacttxt:
            case R.id.contact:
                final CharSequence[] items2 = {"벨소리", "진동", "무음", "차단"};
                AlertDialog.Builder alertDialogBuilder2 = new AlertDialog.Builder(context);

                // 제목셋팅
                alertDialogBuilder2.setTitle("모드 선택 목록");
                alertDialogBuilder2.setSingleChoiceItems(items2, -1,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                contacttxt.setText(items2[id]);
                                if (items2[id].equals("벨소리"))
                                    mode.setContact(1);
                                else if (items2[id].equals("진동"))
                                    mode.setContact(2);
                                else if (items2[id].equals("무음"))
                                    mode.setContact(3);
                                else if (items2[id].equals("차단"))
                                    mode.setContact(4);

                                // 프로그램을 종료한다
                                Toast.makeText(getApplicationContext(), items2[id] + " 선택했습니다.", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        });
                // 다이얼로그 생성
                AlertDialog alertDialog2 = alertDialogBuilder2.create();
                // 다이얼로그 보여주기
                alertDialog2.show();
                break;
            case R.id.unknowntxt :
            case R.id.unknown:
                final CharSequence[] items3 = {"벨소리", "진동", "무음", "차단"};
                AlertDialog.Builder alertDialogBuilder3 = new AlertDialog.Builder(context);

                // 제목셋팅
                alertDialogBuilder3.setTitle("모드 선택 목록");
                alertDialogBuilder3.setSingleChoiceItems(items3, -1,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                unknowntxt.setText(items3[id]);
                                if (items3[id].equals("벨소리"))
                                    mode.setUnknown(1);
                                else if (items3[id].equals("진동"))
                                    mode.setUnknown(2);
                                else if (items3[id].equals("무음"))
                                    mode.setUnknown(3);
                                else if (items3[id].equals("차단"))
                                    mode.setUnknown(4);

                                // 프로그램을 종료한다
                                Toast.makeText(getApplicationContext(), items3[id] + " 선택했습니다.", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        });
                // 다이얼로그 생성
                AlertDialog alertDialog3 = alertDialogBuilder3.create();
                // 다이얼로그 보여주기
                alertDialog3.show();
                break;
            case R.id.set:
                mode.setName(modename.getText().toString());

                if (!mode.getName().isEmpty() && mode.getStar() > 0 && mode.getContact() > 0 && mode.getUnknown() > 0 && mode.getDraw() >= 0 && !mode.getSms().isEmpty()) {

                    if (name == null)
                        dbManager.insertMode(mode);
                     else if (name.equals(preferences.getString("name", "null"))) {
                        dbManager.updateMode(name, mode);

                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("set", "on");
                        editor.putString("name", mode.getName());
                        editor.putInt("star", mode.getStar());
                        editor.putInt("contact", mode.getContact());
                        editor.putInt("unknown", mode.getUnknown());
                        editor.putInt("time", mode.getTime());
                        editor.putInt("count", mode.getCount());
                        editor.putString("sms", mode.getSms());

                        editor.commit();

                        Log.i(Tag, "Update Now Mode");
                    } else
                        dbManager.updateMode(name, mode);
                    ModeSetActivity.this.finish();
                }
                else
                    Toast.makeText(ModeSetActivity.this, "모든 내용을 입력하십시오", Toast.LENGTH_SHORT).show();
                break;
            case R.id.delete:
                if (name == null) {
                    finish();
                }
                else {
                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(ModeSetActivity.this);
                    builder.setMessage("모드를 삭제하시겠습니까?");
                    builder.setNegativeButton("예",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    SharedPreferences preferences =context.getSharedPreferences("Mode", Activity.MODE_PRIVATE);

                                    if (preferences.getString("name", "null").equals(name)) {
                                        if (preferences.getString("set", "off").equals("off")) {
                                            deletemode_scheduleoff(name);
                                            dbManager.deleteMode(name);
                                            SharedPreferences.Editor editor = preferences.edit();
                                            editor.putString("name", "null");
                                            editor.commit();

                                            Toast.makeText(context, "모드를 삭제하였습니다.", Toast.LENGTH_SHORT).show();
                                            finish();
                                        } else
                                            Toast.makeText(context, "현재 모드라 삭제 불가능 합니다.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        deletemode_scheduleoff(name);
                                        dbManager.deleteMode(name);
                                        Toast.makeText(context, "모드를 삭제하였습니다.", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                }
                            });
                    builder.setPositiveButton("아니오", null);
                    builder.show();
                }
                break;
            case R.id.mode_icon:
                AlertDialog.Builder builder = new AlertDialog.Builder(ModeSetActivity.this);
                        LayoutInflater inflater = getLayoutInflater();
                        iconview = inflater.from(this).inflate(R.layout.mode_icon_select,null);
                        builder.setNegativeButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                RadioGroup radioGroup= (RadioGroup)iconview.findViewById(R.id.icon_group);
                                RadioButton radioButton = (RadioButton)radioGroup.findViewById(radioGroup.getCheckedRadioButtonId());

                                if(Integer.parseInt(radioButton.getTag().toString())==6)
                                {
                                    mode.setDraw(0); // 0은 아무 의미 없음 단지 0으로 tag가 6일때를 고름
                                    File imgFile = new  File(mode.getPicture());
                                    if(imgFile.exists()){
                                        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                                        RoundedAvatarDrawable tmpRoundedAvatarDrawable = new RoundedAvatarDrawable(myBitmap);
                                        icon.setImageDrawable(tmpRoundedAvatarDrawable);
                                    }
                                }
                                else
                                {
                                    mode.setDraw(IconImage(Integer.parseInt(radioButton.getTag().toString())));
                                    icon.setImageResource(mode.getDraw());
                                }
                                dialog.dismiss();
                            }
                        }).setPositiveButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                    }
                }).setNeutralButton("사진첩에서 선택", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        Intent intent = new Intent();
                        // Gallery 호출
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        // 잘라내기 셋팅
                        intent.putExtra("crop", "true");
                        intent.putExtra("aspectX", 0);
                        intent.putExtra("aspectY", 0);
                        intent.putExtra("outputX", 200);
                        intent.putExtra("outputY", 150);
                        try {
                            intent.putExtra("return-data", true);
                            startActivityForResult(Intent.createChooser(intent,
                                    "사진 어플"), 2);
                        } catch (ActivityNotFoundException e) {
                            // Do nothing for now
                        }
                    }
                });
                builder.setView(iconview);
                builder.show();
                break;
            case R.id.sms:
                AlertDialog.Builder smsbuilder = new AlertDialog.Builder(ModeSetActivity.this);
                LayoutInflater smsinflater = getLayoutInflater();
                smsview = smsinflater.from(this).inflate(R.layout.mode_sms,null);

                sms_detail = (EditText)smsview.findViewById(R.id.sms_detail);
                sms_detail.setText(mode.getSms());

                smsbuilder.setNegativeButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sms_string =  sms_detail.getText().toString();
                        mode.setSms(sms_string);
                        dialog.dismiss();
                    }
                }).setPositiveButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                smsbuilder.setView(smsview);
                smsbuilder.show();
                break;
            default:
                break;

        }
    }

    private void deletemode_scheduleoff(String modename){
        ArrayList<Schedule> arraySchedule = dbManager.deleteMode_scheduleOff(modename);

        for(Schedule schedule:arraySchedule){
            Log.i(Tag,schedule.getId()+"/ "+schedule.getOnoff());
            if(schedule.getOnoff()==1){
                int id = schedule.getId();
                Intent intent = new Intent(context,AlarmReceiver.class);
                AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

                PendingIntent amIntent = PendingIntent.getBroadcast(context, id*2, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                PendingIntent amIntent2 = PendingIntent.getBroadcast(context, (id*2)+1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                am.cancel(amIntent);
                am.cancel(amIntent2);

                SharedPreferences preferences= context.getSharedPreferences("Schedule", Activity.MODE_PRIVATE);
                if(preferences.getInt("id", -1)==id){
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putInt("id", -1);
                    editor.commit();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data) {


        if (requestCode == 2) {
            Bundle extras2 = data.getExtras();


            String filePath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/AlarmCall/"+System.currentTimeMillis()+".jpg";


            if (extras2 != null) {
                Bitmap photo = extras2.getParcelable("data");
                storeCropImage(photo, filePath); // CROP된 이미지를 외부저장소, 앨범에 저장한다.
                RoundedAvatarDrawable tmpRoundedAvatarDrawable = new RoundedAvatarDrawable(photo);
                icon.setImageDrawable(tmpRoundedAvatarDrawable);
            }

        }
    }

    private void storeCropImage(Bitmap bitmap, String filePath) {
        String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/AlarmCall/";
        File directory = new File(dirPath);
        if(!directory.exists()) // 폴더가 없다면 (새로 이미지를 저장할 경우에 속한다.)
            directory.mkdir();
        File copyFile = new File(filePath);
        BufferedOutputStream out = null;

        try {
            copyFile.createNewFile();
            out = new BufferedOutputStream(new FileOutputStream(copyFile));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            mode.setPicture(filePath);
            mode.setDraw(0);
            // 임시 토스트
            Toast.makeText(getApplicationContext(), " 저장했습니다.", Toast.LENGTH_SHORT).show();
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                    Uri.fromFile(copyFile)));
            out.flush();
            out.close();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), " 저장안됬습니다.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }




    private int IconImage(int id){
        switch (id){
            case 1:
                return R.drawable.icon_book;
            case 2:
                return R.drawable.icon_movie;
            case 3:
                return R.drawable.icon_office;
            case 4:
                return R.drawable.icon_sleep;
            case 5:
                return R.drawable.icon_tea;
            case 6:
                return 0;
            default:
                return R.drawable.icon_sleep;
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

 class RoundedAvatarDrawable extends Drawable {
    private final Bitmap mBitmap;
    private final Paint mPaint;
    private final RectF mRectF;
    private final int mBitmapWidth;
    private final int mBitmapHeight;

    public RoundedAvatarDrawable(Bitmap bitmap) {
        mBitmap = bitmap;
        mRectF = new RectF();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        final BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        mPaint.setShader(shader);

        mBitmapWidth = mBitmap.getWidth();
        mBitmapHeight = mBitmap.getHeight();
    }

    @Override
    public void draw(Canvas canvas) {
     //   canvas.drawOval(mRectF, mPaint);
        canvas.drawCircle(mBitmapWidth/2,mBitmapHeight/2,mBitmapHeight/2,mPaint);
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);

        mRectF.set(bounds);
    }

    @Override
    public void setAlpha(int alpha) {
        if (mPaint.getAlpha() != alpha) {
            mPaint.setAlpha(alpha);
            invalidateSelf();
        }
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        mPaint.setColorFilter(cf);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    public int getIntrinsicWidth() {
        return mBitmapWidth;
    }

    @Override
    public int getIntrinsicHeight() {
        return mBitmapHeight;
    }

    public void setAntiAlias(boolean aa) {
        mPaint.setAntiAlias(aa);
        invalidateSelf();
    }

    @Override
    public void setFilterBitmap(boolean filter) {
        mPaint.setFilterBitmap(filter);
        invalidateSelf();
    }

    @Override
    public void setDither(boolean dither) {
        mPaint.setDither(dither);
        invalidateSelf();
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }


}