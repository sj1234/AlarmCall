package com.example.sjeong.alarmcall;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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

public class ModeSetActivity extends AppCompatActivity implements View.OnClickListener{


    private Context context;
    private String Tag="test ModeSetActive";
    private DBManager dbManager;
    private Mode mode;
    private String name;

    private TextView startxt,contacttxt,unknowntxt;
    private EditText modename;
    private View iconview;
    private ImageButton icon;
    private Uri mImageCaptureUri;
    private ImageView imageView;
    private String absoultePath;
    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;
    private static final int CROP_FROM_iMAGE = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        TextView timetxt = (TextView) findViewById(R.id.timetxt);
        TextView counttxt = (TextView) findViewById(R.id.counttxt);

        mode = new Mode();

        if(name != null) {
            mode=dbManager.getMode(name);
            modename.setText(name);
            startxt.setText( RingInformation(mode.getStar()));
            contacttxt.setText(RingInformation(mode.getContact()));
            unknowntxt.setText(RingInformation(mode.getUnknown()));
            icon.setImageResource(mode.getDraw());
        }
        else {
            mode = new Mode();
            modename.setHint("모드 이름");
            startxt.setText("수신음 선택");
            contacttxt.setText("수신음 선택");
            unknowntxt.setText("수신음 선택");
            icon.setImageResource(R.drawable.icon_empty);
            delete.setText("취소");
        }


       final Spinner s1 = (Spinner)findViewById(R.id.timespinner);

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
                if (mode.getName() != null && mode.getStar() > 0 && mode.getContact() > 0 && mode.getUnknown() > 0 && mode.getDraw() > 0) {
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
                                            dbManager.deleteMode(name);
                                            SharedPreferences.Editor editor = preferences.edit();
                                            editor.putString("name", "null");
                                            editor.commit();

                                            Toast.makeText(context, "모드를 삭제하였습니다.", Toast.LENGTH_SHORT).show();
                                            finish();
                                        } else
                                            Toast.makeText(context, "현재 모드라 삭제 불가능 합니다.", Toast.LENGTH_SHORT).show();
                                    } else {
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
                        mode.setDraw(IconImage(Integer.parseInt(radioButton.getTag().toString())));
                        icon.setImageResource(mode.getDraw());
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
                        Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                        startActivityForResult(intent, PICK_FROM_ALBUM);
                    }
                });

                builder.setView(iconview);
                builder.show();
                break;
            default:
                break;

        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode != RESULT_OK)
            return;

        switch(requestCode)
        {
            case PICK_FROM_ALBUM:
            {
                // 이후의 처리가 카메라와 같으므로 일단  break없이 진행합니다.
                // 실제 코드에서는 좀더 합리적인 방법을 선택하시기 바랍니다.
                mImageCaptureUri = data.getData();
                Log.d("SmartWheel",mImageCaptureUri.getPath().toString());
            }

            case PICK_FROM_CAMERA:
            {
                // 이미지를 가져온 이후의 리사이즈할 이미지 크기를 결정합니다.
                // 이후에 이미지 크롭 어플리케이션을 호출하게 됩니다.
                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(mImageCaptureUri, "image/*");

                // CROP할 이미지를 200*200 크기로 저장
                intent.putExtra("outputX", 200); // CROP한 이미지의 x축 크기
                intent.putExtra("outputY", 200); // CROP한 이미지의 y축 크기
                intent.putExtra("aspectX", 1); // CROP 박스의 X축 비율
                intent.putExtra("aspectY", 1); // CROP 박스의 Y축 비율
                intent.putExtra("scale", true);
                intent.putExtra("return-data", true);
                startActivityForResult(intent, CROP_FROM_iMAGE); // CROP_FROM_CAMERA case문 이동
                break;
            }
            case CROP_FROM_iMAGE:
            {
                // 크롭이 된 이후의 이미지를 넘겨 받습니다.
                // 이미지뷰에 이미지를 보여준다거나 부가적인 작업 이후에
                // 임시 파일을 삭제합니다.
                if(resultCode != RESULT_OK) {
                    return;
                }
                final Bundle extras = data.getExtras();
                // CROP된 이미지를 저장하기 위한 FILE 경로
                String filePath = Environment.getExternalStorageDirectory().getAbsolutePath()+
                        "/SmartWheel/"+System.currentTimeMillis()+".jpg";
                if(extras != null)
                {
                    Bitmap photo = extras.getParcelable("data"); // CROP된 BITMAP
                    imageView.setImageBitmap(photo); // 레이아웃의 이미지칸에 CROP된 BITMAP을 보여줌
                    storeCropImage(photo, filePath); // CROP된 이미지를 외부저장소, 앨범에 저장한다.
                    absoultePath = filePath;
                    break;
                }
                // 임시 파일 삭제
                File f = new File(mImageCaptureUri.getPath());
                if(f.exists())
                {
                    f.delete();
                }
            }
        }

    }

    /*
    * Bitmap을 저장하는 부분
    */
    private void storeCropImage(Bitmap bitmap, String filePath) {
        // SmartWheel 폴더를 생성하여 이미지를 저장하는 방식이다.
        String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/SmartWheel";
        File directory_SmartWheel = new File(dirPath);

        if(!directory_SmartWheel.exists()) // SmartWheel 디렉터리에 폴더가 없다면 (새로 이미지를 저장할 경우에 속한다.)
            directory_SmartWheel.mkdir();
        File copyFile = new File(filePath);
        BufferedOutputStream out = null;
        try {

            copyFile.createNewFile();
            out = new BufferedOutputStream(new FileOutputStream(copyFile));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            // sendBroadcast를 통해 Crop된 사진을 앨범에 보이도록 갱신한다.
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                    Uri.fromFile(copyFile)));

            out.flush();
            out.close();
        } catch (Exception e) {
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
