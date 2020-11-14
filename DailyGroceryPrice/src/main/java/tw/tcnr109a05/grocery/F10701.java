package tw.tcnr109a05.grocery;

import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
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
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class F10701 extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {

    private static final String DB_File = "friends.db", DB_TABLE = "member";
    public static String myselection = "";
    public static String myargs[] = new String[]{};
    public static String myorder = "id ASC"; // 排序欄位
    public static int mem = 0;
    //--------------------------
    private static ContentResolver mContRes;
    int tcount;
    private Intent intent = new Intent();
    private EditText e001, e005, e006, e007, e008;
    private String s = "";
    private String d = "";
    private Button b001, b002;
    private SQLiteDatabase mFriendDb;
    private int DBversion = 1;
    private TextView e003, e004;
    private RadioGroup r001;
    private RadioButton r001b;
    private RadioButton r001a;
    private String tname, tbirthday, tage, taddress, temail, ttel, tsocial;
    private String tsex;
    private String t1email;
    private int rowsAffected;
    private String msg;
    private ArrayList<Map<String, Object>> mList;
    private ArrayList<String> recSet;
    private ListView list001;
    private ScrollView sc01;
    private Button b003;
    private TextView listt001;
    private String[] MYCOLUMN = new String[]{"id", "MemberID", "username", "password", "email", "name"
            , "sex", "birthday", "age", "address", "tel", "social", "level"};
    // ------------------
    private Handler handler = new Handler();
    private long startTime;
    private int autotime = 1000;//autotime設定幾秒跑一次匯入MySQL
    private String TAG = "TCNR=>";
    private Object[] aa;
    private Menu menu;
    private MenuItem memchange;
    private TextView t001;
    private TextView t002;
    private RelativeLayout mRelat;
    private ListView.OnItemLongClickListener listviewOnItemClkLis = new ListView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

            t001 = (TextView) view.findViewById(R.id.F10701list_t003);
            t002 = (TextView) view.findViewById(R.id.F10701list_t002);
            String t02 = t002.getText().toString();
//            if(t02.equals("姓名:")){
//                b003.performClick();
//                return true;
//            }
//            else {
//                return false;
//            }
            switch (position) {
                case 0:
                    b003.performClick();
                    return true;
                case 1:
                    b003.performClick();
                    return true;
                case 2:
                    b003.performClick();
                    return true;
                case 3:
                    b003.performClick();
                    return true;
                case 4:
                    b003.performClick();
                    return true;
                case 5:
                    b003.performClick();
                    return true;
                case 6:
                    b003.performClick();
                    return true;
                case 7:
                    b003.performClick();
                    return true;
            }
            return false;
        }
    };
    private DatePickerDialog.OnDateSetListener datePicDigOnDateSelLis = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//            d = (year + getString(R.string.n_yy) +
//                    (month + 1) + getString(R.string.n_mm) +
//                    dayOfMonth + getString(R.string.n_dd)
//            );
            d = (year + "-" + (month / 10) +
                    (month % 10 + 1) + "-" + (dayOfMonth / 10) +
                    dayOfMonth % 10);
            e003.setText(d + "\n" + s);
            Calendar now = Calendar.getInstance();
            int age = now.get(Calendar.YEAR) - year;
            e004.setText(age + getString(R.string.age));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f10701);
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
        e001 = (EditText) findViewById(R.id.f10701_e001);//姓名
        r001 = (RadioGroup) findViewById(R.id.f10701_r001);//男女
        r001a = (RadioButton) findViewById(R.id.f10701_r001a);
        r001b = (RadioButton) findViewById(R.id.f10701_r001b);
        e003 = (TextView) findViewById(R.id.f10701_e003);//生日
        e004 = (TextView) findViewById(R.id.f10701_e004);//年齡
        e005 = (EditText) findViewById(R.id.f10701_e005);//地址
        e006 = (EditText) findViewById(R.id.f10701_e006);//信箱
        e007 = (EditText) findViewById(R.id.f10701_e007);//手機
        e008 = (EditText) findViewById(R.id.f10701_e008);//FB
        b001 = (Button) findViewById(R.id.f10701_btnOK);//確認修改
        b002 = (Button) findViewById(R.id.f10701_btnCancel);//取消修改
        b003 = (Button) findViewById(R.id.f10701list_b001);//修改會員資料
        listt001 = (TextView) findViewById(R.id.f10701list_t001);

        sc01 = (ScrollView) findViewById(R.id.sc01);

        //----------------------------------------------------------------------------------------
        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int newscrollheight = displayMetrics.heightPixels * 95 / 100; // 設定ScrollView使用尺寸的4/5

        list001 = (ListView) findViewById(R.id.F10701_list);
        list001.getLayoutParams().height = newscrollheight;
        list001.setLayoutParams(list001.getLayoutParams()); // 重定ScrollView大小

        mRelat = (RelativeLayout) findViewById(R.id.mRelative_dlg);

        //--------------------------------------------------------------------------------------------
        e003.setOnTouchListener(this);
        b001.setOnClickListener(this);
        b002.setOnClickListener(this);
        b003.setOnClickListener(this);
        //===========取SQLite 資料=============
        mList = new ArrayList<>();
        String[] title = {
                getString(R.string.f10701_t001),
                getString(R.string.f10701_t002),
                getString(R.string.f10701_t003),
                getString(R.string.f10701_t004),
                getString(R.string.f10701_t005),
                getString(R.string.f10701_t006),
                getString(R.string.f10701_t007),
                getString(R.string.f10701_t008)};
        ArrayList<String> memberinfomation = showmember(t1email);
        aa = memberinfomation.toArray();
        int bb = 0;


        for (int j = 0; j < 8; j++) {

            Map<String, Object> item = new HashMap<>();

            item.put("t002", title[j]);

            item.put("t003", aa[j]);
            mList.add(item);

        }

        //===========設定listview=============
        SimpleAdapter adapter = new SimpleAdapter(this,
                mList,
                R.layout.list_f10701,
                new String[]{"t002", "t003"},
                new int[]{R.id.F10701list_t002, R.id.F10701list_t003}
        );
        list001.setAdapter(adapter);
        list001.setOnItemLongClickListener(listviewOnItemClkLis);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        sc01.setVisibility(View.INVISIBLE);
        list001.setVisibility(View.VISIBLE);
//        b003.setVisibility(View.VISIBLE);
//        listt001.setVisibility(View.VISIBLE);

    }

    private void meminformation(String t02) {

    }

    private ArrayList<String> showmember(String tusername) {
        mContRes = getContentResolver();
//        Cursor cur = mContRes.query(FriendsContentProvider.CONTENT_URI_f00000, MYCOLUMN, null, null, null);
//        cur.moveToFirst(); // 一定要寫，不然會出錯

        String fldSet = null;
        ArrayList<String> recAry = new ArrayList<String>();
        try {
            String sql = "SELECT * FROM members WHERE username LIKE " + "'" + tusername + "'" + " ORDER BY id ASC";

            String result = DBConnector.executeQuery(sql);
            JSONArray jsonArray = new JSONArray(result);

            if (result != null) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonData = jsonArray.getJSONObject(i);
                    recAry.add(jsonData.get("name").toString());
                    recAry.add(jsonData.get("sex").toString());
                    recAry.add(jsonData.get("birthday").toString());
                    recAry.add(jsonData.get("age").toString());
                    recAry.add(jsonData.get("address").toString());
                    recAry.add(jsonData.get("email").toString());
                    recAry.add(jsonData.get("tel").toString());
                    recAry.add(jsonData.get("social").toString());
                }
            }
        } catch (Exception e) {
            Log.e("log_tag", e.toString());
        }
        return recAry;
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

    private String mysql_update() {

        String b_id = Findid(t1email);
        tname = e001.getText().toString().trim();
        tbirthday = e003.getText().toString().trim();
        tage = e004.getText().toString().trim();
        taddress = e005.getText().toString().trim();
        temail = e006.getText().toString().trim();
        ttel = e007.getText().toString().trim();
        tsocial = e008.getText().toString().trim();
        switch (r001.getCheckedRadioButtonId()) {
            case R.id.f10701_r001a:
                tsex = r001a.getText().toString().trim();
                break;
            case R.id.f10701_r001b:
                tsex = r001b.getText().toString().trim();
                break;
        }

        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("id", b_id));
        nameValuePairs.add(new BasicNameValuePair("name", tname));
        nameValuePairs.add(new BasicNameValuePair("sex", tsex));
        nameValuePairs.add(new BasicNameValuePair("birthday", tbirthday));
        nameValuePairs.add(new BasicNameValuePair("age", tage));
        nameValuePairs.add(new BasicNameValuePair("address", taddress));
        nameValuePairs.add(new BasicNameValuePair("email", temail));
        nameValuePairs.add(new BasicNameValuePair("tel", ttel));
        nameValuePairs.add(new BasicNameValuePair("social", tsocial));
        String result = DBConnector.executeUpdate_f10701("SELECT * FROM members", nameValuePairs);
        Log.d(TAG, "Updateresult:" + result);
        return result;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.f10701_btnOK:
                tname = e001.getText().toString().trim();
                tbirthday = e003.getText().toString().trim();
                tage = e004.getText().toString().trim();
                taddress = e005.getText().toString().trim();
                temail = e006.getText().toString().trim();
                ttel = e007.getText().toString().trim();
                tsocial = e008.getText().toString().trim();


                switch (r001.getCheckedRadioButtonId()) {
                    case R.id.f10701_r001a:
                        tsex = r001a.getText().toString().trim();
                        break;
                    case R.id.f10701_r001b:
                        tsex = r001b.getText().toString().trim();
                        break;
                }
//                tsex = r001.getTag().toString().trim();

                new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.updatemember))
                        .setMessage(getString(R.string.updatemember1))
                        .setCancelable(true)
                        .setIcon(R.drawable.logo)
                        .setPositiveButton(getString(R.string.yes),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (tsocial.equals("")) {
                                            tsocial = " ";
                                        }
                                        String u_id = Findid(t1email);
                                        String result = mysql_update();//傳回修改筆數
                                        if (result == null) {
                                            msg = "請重新登入 !";
                                        } else {
                                            msg = "資料修改成功!!";
                                            setupViewComponent();
                                        }
                                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

                                        memchange.setVisible(true);
                                    }
                                })
                        .setNeutralButton(getString(R.string.cancel),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                })
                        .show();
                break;
            case R.id.f10701_btnCancel:
                e001.setText("");
                e003.setText("");
                e004.setText("");
                e005.setText("");
                e006.setText("");
                e007.setText("");
                e008.setText("");

                sc01.setVisibility(View.INVISIBLE);
                list001.setVisibility(View.VISIBLE);
                memchange.setVisible(true);
//                b003.setVisibility(View.VISIBLE);
//                listt001.setVisibility(View.VISIBLE);

                this.setTitle("會員資料");
                ActionBar actionBar = getSupportActionBar();
                actionBar.setDisplayHomeAsUpEnabled(true);
                break;
            case R.id.f10701list_b001:
                e001.setText(aa[0].toString());
                if (aa[1].equals("男")) {
                    r001.check(R.id.f10701_r001a);
                } else {
                    r001.check(R.id.f10701_r001b);
                }

                this.setTitle("會員資料修改");
                e003.setText(aa[2].toString());
                e004.setText(aa[3].toString());
                e005.setText(aa[4].toString());
                e006.setText(aa[5].toString());
                e007.setText(aa[6].toString());
                e008.setText(aa[7].toString());
                actionBar = getSupportActionBar();
                actionBar.setDisplayHomeAsUpEnabled(false);
                sc01.setVisibility(View.VISIBLE);
                list001.setVisibility(View.INVISIBLE);
                memchange.setVisible(false);
//                b003.setVisibility(View.INVISIBLE);
//                listt001.setVisibility(View.INVISIBLE);
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

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_UP:

                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_DOWN:
                switch (view.getId()) {
                    case R.id.f10701_e003:
                        e003.setText("");
                        Calendar now = Calendar.getInstance();
                        DatePickerDialog datePicDlg = new DatePickerDialog(
                                this,
                                datePicDigOnDateSelLis,
                                now.get(Calendar.YEAR),
                                now.get(Calendar.MONTH),
                                now.get(Calendar.DAY_OF_MONTH)
                        );
                        datePicDlg.setTitle(getString(R.string.F10701_datetitle));
//                datePicDlg.setMessage(getString(R.string.F10701_datemessage));
                        datePicDlg.setIcon(android.R.drawable.ic_dialog_info);
                        datePicDlg.setCancelable(false);
                        datePicDlg.show();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        this.menu = menu;
        memchange = menu.findItem(R.id.changemember);
        memchange.setVisible(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
            case R.id.changemember:
                b003.performClick();
                break;
        }
        return true;
    }


}
