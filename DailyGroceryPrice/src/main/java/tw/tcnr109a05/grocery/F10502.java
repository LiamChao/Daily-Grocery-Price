package tw.tcnr109a05.grocery;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableRow;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

import tw.tcnr109a05.grocery.providers.FriendsContentProvider;


public class F10502 extends AppCompatActivity implements View.OnClickListener {

    private static final String DB_FILE = "friends.db";
    private static ContentResolver mContRes;
    int tcount;
    private Button b001;
    private Button b002;
    private ImageButton imgView001;
    private ImageButton imgView002;
    private TableRow Tab;
    private int i = 1;
    private int j = 5;
    private EditText e;
    private TableRow t;
    private int k = 15;
    private ImageButton imgView003;
    private ImageButton imgView004;
    private ImageButton imgView005;
    private ImageButton imgView006;
    private ImageButton imgView007;
    private String idName;
    private String idName1;
    private String idName2;
    private String idName3;
    private String idName4;
    private int resID;
    private int resID1;
    private int resID2;
    private int resID3;
    private int resID4;
    private ImageButton imgView008;
    private EditText t001;
    private EditText t002;
    private String s_t001;
    private String s_t002;
    private FriendDbHelper dbHper;
    private int DBver = 1;
    private EditText e1;
    private EditText e2;
    private String s_t003;
    private String s_t004;
    private int resID5;
    private EditText e3;
    private String s_t003_2;
    private String[] MYCOLUMN = new String[]{"id", "recipe", "introduction", "ingredients", "step", "r_id"};
    private int index;
    private String a;
    private String b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f10501);
        setupViewcompenent();
    }

    private void setupViewcompenent() {
        b001 = (Button) findViewById(R.id.f10501_b001);
        b002 = (Button) findViewById(R.id.f10501_b002);
        t001 = (EditText) findViewById(R.id.f10501_e001);
        t002 = (EditText) findViewById(R.id.f10501_e002);

        imgView001 = (ImageButton) findViewById(R.id.f10501_imgView001);
        imgView002 = (ImageButton) findViewById(R.id.f10501_imgView002);
        imgView003 = (ImageButton) findViewById(R.id.f10501_imgView003);
        imgView004 = (ImageButton) findViewById(R.id.f10501_imgView004);
        imgView005 = (ImageButton) findViewById(R.id.f10501_imgView005);
        imgView006 = (ImageButton) findViewById(R.id.f10501_imgView006);
        imgView007 = (ImageButton) findViewById(R.id.f10501_imgView007);
        imgView008 = (ImageButton) findViewById(R.id.f10501_imgView008);

        b001.setOnClickListener(this);
        b002.setOnClickListener(this);
        imgView001.setOnClickListener(this);
        imgView002.setOnClickListener(this);
        imgView003.setOnClickListener(this);
        imgView004.setOnClickListener(this);
        imgView005.setOnClickListener(this);
        imgView006.setOnClickListener(this);
        imgView007.setOnClickListener(this);
        imgView008.setOnClickListener(this);

        initDB();

        imgView008.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.f10501_b001:  //取消
                intent = new Intent();
                intent.setClass(F10502.this, F10500.class);
                startActivity(intent);
                this.finish();
                break;
            case R.id.f10501_b002:  //確定

                mContRes = getContentResolver();
                Cursor c_add = mContRes.query(FriendsContentProvider.CONTENT_URI, MYCOLUMN, null, null, null);
                s_t001 = t001.getText().toString().trim();
                s_t002 = t002.getText().toString().trim();
                String aa[] = {"", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};
                int OO = 0;
                int XX = 0;
                a = "";
                b = "";
                int kk = 0;
                for (int m = 1; m < 16; m++) {
                    idName3 = "f10501_e003_" + String.format("%02d", m);
                    resID3 = getResources().getIdentifier(idName3, "id", getPackageName());
                    e1 = ((EditText) findViewById(resID3));
                    s_t003 = e1.getText().toString().trim();
                    if (s_t003.equals("")) {
                    } else {
                        OO++;
                    }
                    aa[m - 1 + kk] = s_t003;
                    kk++;
                }
                kk = 0;
                for (int m = 1; m < 16; m++) {
                    idName3 = "f10501_e004_" + String.format("%02d", m);
                    resID5 = getResources().getIdentifier(idName3, "id", getPackageName());
                    e3 = ((EditText) findViewById(resID5));
                    s_t003_2 = e3.getText().toString().trim();
                    if (s_t003_2.equals("")) {
                    } else {
                        XX++;
                    }
                    aa[m + kk] = s_t003_2;
                    kk++;
                }

                for (int m = 5; m < 11; m++) {
                    idName4 = "f10501_e" + String.format("%03d", m);
                    resID4 = getResources().getIdentifier(idName4, "id", getPackageName());
                    e2 = ((EditText) findViewById(resID4));
                    s_t004 = e2.getText().toString().trim();
                    b += s_t004 + "#";
                }
                if (s_t001.equals("") || s_t002.equals("") || OO != XX) {
                    Toast.makeText(F10502.this, "資料空白無法新增 !", Toast.LENGTH_SHORT).show();
                    return;
                }
                String msg = null;
                for (int mm = 0; mm < 30; mm++) {
                    a += aa[mm] + "#";
                }
                //--------------------------直接新增到mysql--------------------
                mysql_insert();

//                (s_t001, s_t002,a,b,1)
                //-------------------------------------------------------------------
                ContentValues newRow = new ContentValues();
                newRow.put("recipe", s_t001);
                newRow.put("introduction", s_t002);
                newRow.put("ingredients", a);
                newRow.put("step", b);
                F00000.mem = 2;
                mContRes.insert(FriendsContentProvider.CONTENT_URI, newRow);
                // -------------------------
//                ContentValues newRow = new ContentValues();
//                newRow.put("name", tname);
//                newRow.put("grp", tgrp);
//                newRow.put("address", taddr);
//                mContRes.insert(FriendsContentProvider.CONTENT_URI, newRow);
                // -------------------------
                msg = "新增記錄  成功 ! \n" + "目前資料表共有 " + (c_add.getCount() + 1) + " 筆記錄 !";
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

                if (c_add == null) {
                    tcount = 0;
                    index = 0;
                    return;
                }
                ;
//                dbmysql();
                c_add.close();
//                setupViewComponent();


                //---------------------------------------------

//
//
//                s_t001=t001.getText().toString().trim();
//                s_t002=t002.getText().toString().trim();
//                String aa[]={"","","","","","","","","","","","","","","","","","","","","","","","","","","","","",""};
//                int OO=0;
//                int XX=0;
//                String a="";
//                String b="";
//                int kk=0;
//                for (int m=1;m<16;m++){
//                    idName3 = "f10501_e003_" + String.format("%02d", m);
//                    resID3 = getResources().getIdentifier(idName3, "id", getPackageName());
//                    e1 = ((EditText) findViewById(resID3));
//                    s_t003=e1.getText().toString().trim();
//                    if (s_t003.equals("")){
//                    }else {
//                        OO++;
//                    }
//                    aa[m-1+kk]=s_t003;
//                    kk++;
//                }
//                kk=0;
//                for (int m=1;m<16;m++){
//                    idName3 = "f10501_e004_" + String.format("%02d", m);
//                    resID5 = getResources().getIdentifier(idName3, "id", getPackageName());
//                    e3 = ((EditText) findViewById(resID5));
//                    s_t003_2=e3.getText().toString().trim();
//                    if (s_t003_2.equals("")){
//                    }else {
//                        XX++;
//                    }
//                    aa[m+kk]=s_t003_2;
//                    kk++;
//                }
//
//                for (int m=5;m<11;m++){
//                    idName4 = "f10501_e" + String.format("%03d", m);
//                    resID4 = getResources().getIdentifier(idName4, "id", getPackageName());
//                    e2 = ((EditText) findViewById(resID4));
//                    s_t004=e2.getText().toString().trim();
//                    b +=s_t004+"#";
//                }
//                if (s_t001.equals("") || s_t002.equals("")||OO!=XX) {
//                    Toast.makeText(F10501.this, "資料空白無法新增 !", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                String msg = null;
//                for (int mm=0;mm<30;mm++){
//                    a += aa[mm]+"#";
//                }
//
//                long rowID = dbHper.insert_f10501(s_t001, s_t002,a,b,1); //真正執行SQL
//                int c=000;
//                if (rowID != -1){
//                    msg = "新增成功 !";
//                }else {
//                    msg = "新增失敗";
//                }
//                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();


                intent = new Intent();
                intent.setClass(F10502.this, F10500.class);
                startActivity(intent);
                this.finish();

                break;
            case R.id.f10501_imgView001:
                if (i < 15) {
                    i++;
                    imgView008.setVisibility(View.VISIBLE);
                    idName = "f10501_tr" + String.format("%03d", i);
                    resID = getResources().getIdentifier(idName, "id", getPackageName());
                    Tab = ((TableRow) findViewById(resID));
                    Tab.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.f10501_imgView002:
                if (j < 10) {
                    j++;
                    idName1 = "f10501_e" + String.format("%03d", j);
                    resID1 = getResources().getIdentifier(idName1, "id", getPackageName());
                    e = ((EditText) findViewById(resID1));
                    e.setVisibility(View.VISIBLE);
                }
                if (k < 20) {
                    k++;
                    if (k == 17) {
                        imgView003.setVisibility(View.INVISIBLE);
                    } else if (k == 18) {
                        imgView004.setVisibility(View.INVISIBLE);
                    } else if (k == 19) {
                        imgView005.setVisibility(View.INVISIBLE);
                    } else if (k == 20) {
                        imgView006.setVisibility(View.INVISIBLE);
                    }
                    idName2 = "f10501_tr" + String.format("%03d", k);
                    resID2 = getResources().getIdentifier(idName2, "id", getPackageName());
                    t = ((TableRow) findViewById(resID2));
                    t.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.f10501_imgView003:
                imgView002.setVisibility(View.VISIBLE);
                e.setVisibility(View.GONE);
                t.setVisibility(View.GONE);
                j--;
                k--;
                id();
                break;
            case R.id.f10501_imgView004:
                imgView003.setVisibility(View.VISIBLE);
                e.setVisibility(View.GONE);
                t.setVisibility(View.GONE);
                j--;
                k--;
                id();
                break;
            case R.id.f10501_imgView005:
                imgView004.setVisibility(View.VISIBLE);
                e.setVisibility(View.GONE);
                t.setVisibility(View.GONE);
                j--;
                k--;
                id();
                break;
            case R.id.f10501_imgView006:
                imgView005.setVisibility(View.VISIBLE);
                e.setVisibility(View.GONE);
                t.setVisibility(View.GONE);
                j--;
                k--;
                id();
                break;
            case R.id.f10501_imgView007:
                imgView006.setVisibility(View.VISIBLE);
                e.setVisibility(View.GONE);
                t.setVisibility(View.GONE);
                j--;
                k--;
                id();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + v.getId());
            case R.id.f10501_imgView008:
                if (i > 1) {
                    idName = "f10501_tr" + String.format("%03d", i);
                    resID = getResources().getIdentifier(idName, "id", getPackageName());
                    Tab = ((TableRow) findViewById(resID));
                    Tab.setVisibility(View.GONE);
                    i--;
                    if (i == 1) {
                        imgView008.setVisibility(View.INVISIBLE);
                    }
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

    private void id() {
        idName1 = "f10501_e" + String.format("%03d", j);
        resID1 = getResources().getIdentifier(idName1, "id", getPackageName());
        e = ((EditText) findViewById(resID1));
        idName2 = "f10501_tr" + String.format("%03d", k);
        resID2 = getResources().getIdentifier(idName2, "id", getPackageName());
        t = ((TableRow) findViewById(resID2));
    }

    private void initDB() {
        if (dbHper == null) {
            dbHper = new FriendDbHelper(getApplicationContext(), DB_FILE, null, DBver);
//            recSet = dbHper.getID();
        }
    }

    private void mysql_insert() {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

        nameValuePairs.add(new BasicNameValuePair("recipe", s_t001));
        nameValuePairs.add(new BasicNameValuePair("introduction", s_t002));
        nameValuePairs.add(new BasicNameValuePair("ingredients", a));
        nameValuePairs.add(new BasicNameValuePair("step", b));
//        nameValuePairs.add(new BasicNameValuePair("r_id", 1));

        try {
            Thread.sleep(500); //  延遲Thread 睡眠0.5秒
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String result = DBConnector.executeInsert("SELECT * FROM recipe", nameValuePairs);
    }
}
