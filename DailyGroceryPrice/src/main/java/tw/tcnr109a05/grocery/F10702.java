package tw.tcnr109a05.grocery;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class F10702 extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {

    private static final String DB_File = "friends.db", DB_TABLE = "member";
    public static String myselection = "";
    public static String myargs[] = new String[]{};
    public static String myorder = "id ASC"; // 排序欄位
    public static int mem = 0;
    //--------------------------
    private static ContentResolver mContRes;
    int tcount;
    private SQLiteDatabase mFriendDb;
    private int DBversion = 1;
    private EditText e001, e002;
    private String s = "";
    private String d = "";
    private Button b001;
    private TextView t001, t002;
    private MotionEvent motionEvent;
    private int rowsAffected;
    private String check_tpassword, tpassword;
    private String msg;
    private String t1email;
    private int loginon;
    private TextView t003;
    private String[] MYCOLUMN = new String[]{"id", "MemberID", "username", "password", "email", "name"
            , "sex", "birthday", "age", "address", "tel", "social", "level"};
    // ------------------
    private Handler handler = new Handler();
    private long startTime;
    private int autotime = 1000;//autotime設定幾秒跑一次匯入MySQL
    private String TAG = "TCNR=>";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f10702);
        u_loademail();

        //-------------抓取遠端資料庫設定執行續------------------------------
        StrictMode.setThreadPolicy(new
                StrictMode.
                        ThreadPolicy.Builder().
                detectDiskReads().
                detectDiskWrites().
                detectNetwork().
                penaltyLog().
                build());
        StrictMode.setVmPolicy(
                new
                        StrictMode.
                                VmPolicy.
                                Builder().
                        detectLeakedSqlLiteObjects().
                        penaltyLog().
                        penaltyDeath().
                        build());
        mContRes = getContentResolver();
//---------------------------------------------------------------------
        //--------取得目前時間 初始時間放在setupViewcomponent會因為重刷而被更新，
        //--------經過時間會被洗掉重算
//        startTime = System.currentTimeMillis();

        mem = 1;
        setupViewComponent();
    }

    private void setupViewComponent() {
        Intent intent = this.getIntent();
        String xx = intent.getStringExtra("class_title");
        this.setTitle(xx);
        e001 = (EditText) findViewById(R.id.f10702_e001);
        e002 = (EditText) findViewById(R.id.f10702_e002);
        t001 = (TextView) findViewById(R.id.f10702_t001);
        t002 = (TextView) findViewById(R.id.f10702_t002);
        t003 = (TextView) findViewById(R.id.f10702_t003);
        b001 = (Button) findViewById(R.id.f10702_btnOK);
        b001.setOnClickListener(this);
        e002.setOnClickListener(this);
        e001.setOnClickListener(this);
        e002.setOnTouchListener(this);
        e001.setOnTouchListener(this);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.f10702_btnOK:
                ctlUpdate();

                break;
            case R.id.f10702_e001:
                break;
            case R.id.f10702_e002:

                break;
        }
    }

    public void u_loademail() {
        SharedPreferences login =
                getSharedPreferences("GAME_RESULT", 0);
        String temail = login.getString("email", "請重新登入");
        t1email = temail;
        int a = 0;
    }

    private void ctlUpdate() {
        //資料更新
        tpassword = e001.getText().toString().trim();
        check_tpassword = e002.getText().toString().trim();
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.updatepassword1))
                .setMessage(getString(R.string.updatepassword))
                .setCancelable(true)
                .setIcon(R.drawable.logo)
                .setPositiveButton(getString(R.string.yes),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                String u_id = Findid(t1email);
                                if (tpassword.equals(check_tpassword) && !tpassword.equals("")) {
                                    String result = mysql_updatepassword();//傳回修改筆數
                                    if (result == null) {
                                        msg = "請重新登入 !";
                                    } else {

                                        msg = "密碼修改成功!!";
//                                    loginon = 0;//登出

//                                    SharedPreferences login =
//                                            getSharedPreferences("GAME_RESULT", 0);
//                                    login
//                                            .edit()
//                                            .putInt("flag", loginon)
//                                            .commit();
//                                    //---------------------------------------------------------------------------------------------------
//                                    Intent it = new Intent();
//                                    Bundle bundle = new Bundle();
//                                    bundle.putInt("flag",loginon);
//                                    it.putExtras(bundle);
//                                    setResult(RESULT_OK, it);
                                        F10702.this.finish();
                                    }
                                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                                } else {
                                    t003.setVisibility(View.VISIBLE);
                                }
                            }
                        })
                .setNeutralButton(getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                .show();

    }

    private String Findid(String tusername) {
        mContRes = getContentResolver();
//        Cursor cur = mContRes.query(FriendsContentProvider.CONTENT_URI_f00000, MYCOLUMN, null, null, null);
//        cur.moveToFirst(); // 一定要寫，不然會出錯

        String fldSet = null;
        try {
            String sql = "SELECT * FROM members WHERE username LIKE " + "'" + tusername + "'" + " ORDER BY id ASC";

            String result = DBConnector.executeQuery(sql);
            JSONArray jsonArray = new JSONArray(result);

            if (result != null) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonData = jsonArray.getJSONObject(i);
                    fldSet = jsonData.get("id").toString();
                }
            }
        } catch (Exception e) {
            Log.e("log_tag", e.toString());
        }
        return fldSet;
    }

    private String mysql_updatepassword() {
        tpassword = e001.getText().toString().trim();
        String b_id = Findid(t1email);
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("id", b_id));
        nameValuePairs.add(new BasicNameValuePair("password", tpassword));
        String result = DBConnector.executeUpdate_f10702("SELECT * FROM members", nameValuePairs);
        Log.d(TAG, "Updateresult:" + result);
        return result;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_UP:

                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_DOWN:
                switch (view.getId()) {
                    case R.id.f10702_e001:
                        e001.setHint("");
                        t001.setVisibility(View.VISIBLE);
                        break;
                    case R.id.f10702_e002:
                        e002.setHint("");
                        t002.setVisibility(View.VISIBLE);
//                        if(!tpassword.equals(check_tpassword)){
//                            t003.setVisibility(View.VISIBLE);
//                        }else {
//                            t003.setVisibility(View.INVISIBLE);
//
//                        }
                        break;
                }
                break;
        }
        return false;
    }

    // Activity 的事件分發
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_DOWN:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    // Activity 的事件消費
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_DOWN:
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();


    }


    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onStop() {
        super.onStop();


    }

    @Override
    protected void onRestart() {
        super.onRestart();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
        }
        return true;
    }


}
