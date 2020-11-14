package tw.tcnr109a05.grocery;

import android.app.ProgressDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.SimpleAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.google.android.material.bottomnavigation.LabelVisibilityMode.LABEL_VISIBILITY_LABELED;

public class F10100 extends F000 implements View.OnClickListener
        , AdapterView.OnItemClickListener, SearchView.OnQueryTextListener {

    private static final String DB_FILE = "news.db";
    private static final int DBversion = 1;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    private ArrayList<Map<String, Object>> mList;  //農業新聞
    private ArrayList<Map<String, Object>> mList02;  //公告
    private Intent intent = new Intent();
    //-------------------ListView農業新聞監聽--------------------------
    private final ListView.OnItemClickListener listviewON = new ListView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


            intent.setClass(F10100.this, F10101_news.class)
                    .putExtra("KEY_NEWS", position);

            startActivity(intent);
        }
    };
    //-------------------ListView公告監聽--------------------------
    private final ListView.OnItemClickListener list002ON = new ListView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            intent.setClass(F10100.this, F10102_announcement.class)
                    .putExtra("KEY_Anno", position);

            startActivity(intent);

        }
    };
    private ListView list002, list001;  //一新聞、二公告
    private Handler handler = new Handler();
    private SearchView sv, sv02;
    private F10100_FriendDbHelper dbHper;
    private ArrayList<String> recSet;
    private Handler mHandler = new Handler();
    private HashMap<String, Object> item;
    private HashMap<String, Object> item02;  //公告用
    private String title, pDate, link, description, id;    //OPENDATA STRING
    private String news_title, news_pDate, news_link, news_description;
    private ProgressBar probar00;
    private ProgressDialog dialog;
    private int update_time = 0;
    private long startTime;
    private Handler handler01 = new Handler();
    private int autotime = 43200;
    private String TAG = "TCNR077";
    private String sqlctl;
    private String anno_num, anno_date, anno_title, anno_content;
    private String a_num, a_title, a_date, a_content;
    private ArrayList<String> recSet_anno;
    private Runnable updateTimer = new Runnable() {
        @Override
        public void run() {
            try {
                setJason();
                setUpViewComponent();
            } catch (Exception e) {
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f10100);
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
        //-----------------------------------------------------------------------
        setUpViewComponent();
        // Bottom Navigation JAVA 開始
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.News);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem mrnuitem) {
                switch (mrnuitem.getItemId()) {
                    case R.id.News:

                        return true;
                    case R.id.Price:
                        startActivity(new Intent(getApplicationContext(), F10200.class));
                        overridePendingTransition(0, 0);
                        F10100.this.finish();
                        return true;
                    case R.id.Food:
                        startActivity(new Intent(getApplicationContext(), F10300.class));
                        overridePendingTransition(0, 0);
                        F10100.this.finish();
                        return true;
                    case R.id.Market:
                        startActivity(new Intent(getApplicationContext(), F10400.class));
                        overridePendingTransition(0, 0);
                        F10100.this.finish();

                        return true;

                    case R.id.Book:
                        startActivity(new Intent(getApplicationContext(), F10500.class));
                        overridePendingTransition(0, 0);
                        F10100.this.finish();
                        return true;
                }
                return false;
            }
        });
        BottomNavigationHelper.removeShiftMode(bottomNavigationView);
        bottomNavigationView.setLabelVisibilityMode(LABEL_VISIBILITY_LABELED);
        // Bottom Navigation JAVA 結束

        closeAndroidPDialog(); //解決警告標語
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    //==<搜尋功能>==
    public boolean onQueryTextChange(String newText) {
        if (TextUtils.isEmpty(newText)) {
            list001.clearTextFilter();
            list002.clearTextFilter();
        } else {
            list001.setFilterText(newText);
        }
        return true;
    }

    private void setUpViewComponent() {
//----------------------set Up listView Display---------------------------------------
        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int newscrollheight = displayMetrics.heightPixels * 70 / 100; // 設定ScrollView使用尺寸的0.7
        list001 = (ListView) findViewById(R.id.f10100_listView);
        list002 = (ListView) findViewById(R.id.f10100_listView);
        list001.getLayoutParams().height = newscrollheight;
        list001.setLayoutParams(list001.getLayoutParams()); // 重定ScrollView大小
        list002.getLayoutParams().height = newscrollheight;
        list002.setLayoutParams(list001.getLayoutParams()); // 重定ScrollView大小
//=======
        BottomNavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setSelectedItemId(R.id.navigation_AGRInews);


        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.navigation_AGRInews:
                        showdata();
                        break;

                    case R.id.navigation_Anno:
                        if (recSet_anno.size() != 0) {
                            showdata_anno();
                        } else {
                            sqlctl = "SELECT * FROM food_anno ";
                            Mysqlsel(sqlctl);
                        }
                        break;

                }
                return false;
            }
        });
//------------------------------------------------------------------------------------
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false); //default false ==> close
        probar00 = (ProgressBar) findViewById(R.id.proBar);

//---------SetJson-------------------------
        initDB();
        if (dbHper.RecCount() != 0) {//資料庫有資料的話
            showdata();//抓取資料庫顯示資料
            probar00.setVisibility(View.INVISIBLE);
        } else {//資料庫沒資料就抓取opendata存入資料庫
//            dialog = ProgressDialog.show(this, "請稍候", "讀取中");
            handler.postDelayed(updateTimer, 1000);
//            act_contentdes.setText(getString(R.string.act_progress));
            F10100_UpdateDataWork work = new F10100_UpdateDataWork(this);
            work.setHandler(mHandler);//先指定工人
            work.setProgressBar(probar00);
            work.start();


            //progress Dialog
            final ProgressDialog progressDialog = new ProgressDialog(F10100.this);
            progressDialog.setTitle("請稍等");
            progressDialog.setMessage("資料下載中，\n約需要10-15秒鐘。");
            progressDialog.setIcon(android.R.drawable.ic_dialog_info);
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMax(100);
            progressDialog.show();


            new Thread(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void run() {
                    Calendar begin = Calendar.getInstance();
                    do {
                        Calendar now = Calendar.getInstance();
                        final int iDiffSec = 60 * (now.get(Calendar.MINUTE) - begin.get(Calendar.MINUTE)) +
                                now.get(Calendar.SECOND) - begin.get(Calendar.SECOND);

                        if (iDiffSec * 40 > 100) {
                            mHandler.post(new Runnable() {
                                public void run() {
                                    progressDialog.setProgress(100);
                                }
                            });
                            break;
                        }

                        mHandler.post(new Runnable() {
                            public void run() {
                                progressDialog.setProgress(iDiffSec * 40);
                            }
                        });

                        if (iDiffSec * 4 < 100)
                            mHandler.post(new Runnable() {
                                public void run() {
                                    progressDialog.setSecondaryProgress(iDiffSec * 4);
                                }
                            });
                        else
                            mHandler.post(new Runnable() {
                                public void run() {
                                    progressDialog.setSecondaryProgress(100);
                                }
                            });
                    } while (true);

                    progressDialog.cancel();

                }
            }).start();

        }
        list001.setTextFilterEnabled(true);
        sv = (SearchView) findViewById(R.id.f10101_searchView);
        sv.setIconifiedByDefault(false);
        sv.setOnQueryTextListener(this);
        sv.setSubmitButtonEnabled(true);
    }

    private void showdata() {
        mList = new ArrayList<>();
        for (int i = 0; i < recSet.size(); i++) {
            item = new HashMap<String, Object>();// TODO 要注意物件new在for迴圈內, 不然會出錯變重複
            String[] fld = recSet.get(i).split("#");
            item.put("title", fld[1]);
            item.put("cDate", fld[4]);
            mList.add(item);
        }

        //==========設定listView============
        SimpleAdapter adapter = new SimpleAdapter(
                getApplicationContext(),
                mList,
                R.layout.f10101_news,
                new String[]{"title", "cDate"},
                new int[]{R.id.f10101_title, R.id.f10101_pub}
        );
        list001.setAdapter(adapter);
        //ListView 設監聽
        list001.setOnItemClickListener(listviewON);
    }

    private void showdata_anno() {
        recSet_anno = dbHper.getRecSet_anno();
        mList02 = new ArrayList<>();
        for (int i = 0; i < recSet_anno.size(); i++) {
            item = new HashMap<String, Object>();// TODO 要注意物件new在for迴圈內, 不然會出錯變重複
            //ID, title, cDate, description
            String[] fld = recSet_anno.get(i).split("#");
            item.put("anno_title", fld[1]);
            item.put("anno_content", fld[2]);
            mList02.add(item);
        }
        //==========設定listView============
        SimpleAdapter adapter = new SimpleAdapter(
                getApplicationContext(),
                mList02,
                R.layout.f10101_anno,
                new String[]{"anno_title", "anno_content"},
                new int[]{R.id.f10101_title, R.id.f10101_upd}
        );
        list002.setAdapter(adapter);
        list002.setOnItemClickListener(list002ON);
    }

    private void initDB() {//連接SQLite
        if (dbHper == null)//沒有連線的話，就開啟連線
            dbHper = new F10100_FriendDbHelper(this, DB_FILE, null, DBversion);
        recSet = dbHper.getRecSet_news();
        recSet_anno = dbHper.getRecSet_anno();
    }

    private void Mysqlsel(String sqlctl) {
        try {

            List<Map<String, Object>> mList = new ArrayList<Map<String, Object>>();
            String result = DBConnector_F10100.executeQuery(sqlctl);
            /**************************************************************************
             * SQL 結果有多筆資料時使用JSONArray
             * 只有一筆資料時直接建立JSONObject物件 JSONObject
             * jsonData = new JSONObject(result);
             **************************************************************************/
            JSONArray jsonArray = new JSONArray(result);
            int aa = jsonArray.length();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonData = jsonArray.getJSONObject(i);
                Map<String, Object> item = new HashMap<String, Object>();
//                id =jsonData.getString("ID");
                anno_num = jsonData.getString("num");
                anno_title = jsonData.getString("anno_title");
                anno_date = jsonData.getString("anno_date");
                anno_content = jsonData.getString("anno_content");
                item.put("anno_num", anno_num);
                item.put("anno_title", anno_title);
                item.put("anno_date", anno_date);
                item.put("anno_content", anno_content);
                mList.add(item);
                //------Data insert SQLite----------------
                a_num = anno_num;
                a_title = anno_title;
                a_date = anno_date;
                a_content = anno_content;
                //----------------------------------------

                long rowID = dbHper.insertRec_F10101_anno(a_num, a_title, a_date, a_content);
            }
        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }
        showdata_anno();
    }

    private void setJason() {
        try {
            String Task_opendata
                    = new TransTask().execute("https://data.coa.gov.tw/Service/OpenData/Agriculturalnews_agriRss.aspx").get();

            List<Map<String, Object>> mList;
            mList = new ArrayList<>();
            //-----解析json-----
            JSONArray jsonArray = new JSONArray(Task_opendata);
            //-----------------
            int xx = jsonArray.length();  //Debug==> set if() to check, if xx.length=0, then it must rerun

            //------------json排序---------------------
            jsonArray = sortJsonArray(jsonArray);
            //----------------------------------------

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonData = jsonArray.getJSONObject(i);
                title = jsonData.getString("title");
                pDate = jsonData.getString("pubDate");
                link = jsonData.getString("link");
                description = jsonData.getString("description");
                //------Data insert SQLite----------------
                news_title = title;
                news_pDate = pDate;
                news_link = link;
                news_description = description;

                long rowID = dbHper.insertRec_F10101_news(news_title, news_pDate, news_link, news_description);

            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    //===============JSON OPEN DATA=======================================

    private JSONArray sortJsonArray(JSONArray jsonArray) {
        //county 自定義的排序method
        final ArrayList<JSONObject> json = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                json.add(jsonArray.getJSONObject(i));
            } catch (JSONException jsone) {
                jsone.printStackTrace();
            }
        }
//--------------------------------------------------------------------------------------------------
        Collections.sort(
                json, new Comparator<JSONObject>() {
                    @Override
                    public int compare(JSONObject jsonOb1, JSONObject jsonOb2) {
                        // 用多重key 排序
                        String lidDate = "", ridDate = "";

                        return lidDate.compareTo(ridDate);
                    }
                }
        );
        return new JSONArray(json);
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    private void closeAndroidPDialog() {
        try {
            Class aClass = Class.forName("android.content.pm.PackageParser$Package");
            Constructor declaredConstructor = aClass.getDeclaredConstructor(String.class);
            declaredConstructor.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Class cls = Class.forName("android.app.ActivityThread");
            Method declaredMethod = cls.getDeclaredMethod("currentActivityThread");
            declaredMethod.setAccessible(true);
            Object activityThread = declaredMethod.invoke(null);
            Field mHiddenApiWarningShown = cls.getDeclaredField("mHiddenApiWarningShown");
            mHiddenApiWarningShown.setAccessible(true);
            mHiddenApiWarningShown.setBoolean(activityThread, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //===============JSON OPEN DATA=======================================
    private class TransTask extends AsyncTask<String, Void, String> {
        String ans;

        @Override
        protected String doInBackground(String... params) {
            StringBuilder sb = new StringBuilder();
            try {
                URL url = new URL(params[0]);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(url.openStream()));
                String line = in.readLine();
                while (line != null) {
                    Log.d("HTTP", line);
                    sb.append(line);
                    line = in.readLine();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            ans = sb.toString();
            //------------
            return ans;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("s", "s:" + s);
            parseJson(s);
        }

        private void parseJson(String s) {

        }

    }
}
