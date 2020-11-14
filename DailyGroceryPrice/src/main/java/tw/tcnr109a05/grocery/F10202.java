package tw.tcnr109a05.grocery;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import tw.tcnr109a05.grocery.providers.FriendsContentProvider_F10202;

import static com.google.android.material.bottomnavigation.LabelVisibilityMode.LABEL_VISIBILITY_LABELED;

public class F10202 extends F000 implements SearchView.OnQueryTextListener {

    private static final String DB_FILE = "F10202.db";
    private static final String DB_TABLE = "fish";
    private static final int DBversion = 1;
    private Intent intent = new Intent();
    private ListView list001;
    private TableRow tab01;
    private String check_t = "";
    private SearchView sv;
    private F10202DbHelper dbHper;
    private ArrayList<String> recSet;
    private Handler handler = new Handler();
    private String f_date, num, fish, market, price;
    private String tNum, tFish, tMarket, tPrice, tDate;
    private ArrayList<Map<String, Object>> mList;
    private HashMap<String, Object> item;
    private Handler mHandler = new Handler();
    //    private ProgressDialog dialog;
    private ProgressBar f10202_progressBar;
    private TextView f10202_contentdes;
    private ContentResolver mContRes;
    private String[] MYCOLUMN = new String[]{"ID", "Fish", "Market", "Avgprice", "Tradedate"};
    private JSONArray jsonArray;
    private Runnable updateTimer = new Runnable() {
        @Override
        public void run() {
            mContRes = getContentResolver();
            Cursor cur = mContRes.query(FriendsContentProvider_F10202.CONTENT_URI,
                    MYCOLUMN, null, null, null);
            cur.moveToFirst(); // 一定要寫，不然會出錯


            try {
                String result = DBConnector_F10202.executeQuery("SELECT * FROM fish");

                //解析json
                jsonArray = new JSONArray(result);
//                int xx = jsonArray.length();
                //---------------------------------------
//                jsonArray = sortJsonArray(jsonArray);
                //---------------------------------------
                //json排序
                //+表頭

                if (jsonArray.length() > 0) {
                    Uri uri = FriendsContentProvider_F10202.CONTENT_URI;
                    mContRes.delete(uri, null, null); // 匯入前,刪除所有SQLite資料

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonData = jsonArray.getJSONObject(i);

//                    if (crop.equals(check_t)) {
//                        crop = "..";
//                    } else {
//                        check_t = crop;
//                    }

                        //-------傳入SQLite-----------------
                        ContentValues newRow = new ContentValues();
                        newRow.put("Fish", jsonData.getString("Fish").toString());
                        newRow.put("Market", jsonData.getString("Market").toString());
                        newRow.put("Avgprice", jsonData.getString("Avgprice").toString());
                        newRow.put("Tradedate", jsonData.getString("Tradedate").toString());
                        mContRes.insert(FriendsContentProvider_F10202.CONTENT_URI, newRow);
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "主機資料庫無資料", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            cur.close();
            //-------run完重新顯示畫面---------------
            initDB();
            f10202_progressBar.setVisibility(View.INVISIBLE);
            f10202_contentdes.setVisibility(View.INVISIBLE);
            showData();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f10202);
        setupViewComponent();

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
//---------------------------------------------------------------------

        BottomNavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setSelectedItemId(R.id.navigation_fish);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_poultry:
                        startActivity(new Intent(getApplicationContext(), F10201.class));
                        overridePendingTransition(0, 0);
                        F10202.this.finish();
                        return true;
                    case R.id.navigation_vegetable:
                        startActivity(new Intent(getApplicationContext(), F10200.class));
                        overridePendingTransition(0, 0);
                        F10202.this.finish();
                        return true;
                    case R.id.navigation_fish:
                        return true;
                }
                return false;
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.Price);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem mrnuitem) {
                switch (mrnuitem.getItemId()) {
//                    case R.id.Home:
//                        startActivity(new Intent(getApplicationContext(),F10000.class));
//                        overridePendingTransition(0, 0);
//                        return true;
                    case R.id.News:
                        startActivity(new Intent(getApplicationContext(), F10100.class));
                        overridePendingTransition(0, 0);
                        F10202.this.finish();
                        return true;
                    case R.id.Price:
//                        startActivity(new Intent(getApplicationContext(),F10200.class));
//                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.Food:
                        startActivity(new Intent(getApplicationContext(), F10300.class));
                        overridePendingTransition(0, 0);
                        F10202.this.finish();
                        return true;
                    case R.id.Market:
                        startActivity(new Intent(getApplicationContext(), F10400.class));
                        overridePendingTransition(0, 0);
                        F10202.this.finish();
                        return true;
                    case R.id.Book:
                        startActivity(new Intent(getApplicationContext(), F10500.class));
                        overridePendingTransition(0, 0);
                        F10202.this.finish();
                        return true;
                }
                return false;
            }
        });
        BottomNavigationHelper.removeShiftMode(bottomNavigationView);
        bottomNavigationView.setLabelVisibilityMode(LABEL_VISIBILITY_LABELED);
    }

    private void setupViewComponent() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int newscrollheight = displayMetrics.heightPixels * 95 / 100; // 設定ScrollView使用尺寸的4/5

        list001 = (ListView) findViewById(R.id.f10202_listView01);
        list001.getLayoutParams().height = newscrollheight;
        list001.setLayoutParams(list001.getLayoutParams()); // 重定ScrollView大小

        tab01 = (TableRow) findViewById(R.id.f10202_tab01);

        list001.setTextFilterEnabled(true);
        sv = (SearchView) findViewById(R.id.f10202_SearchView);
        sv.setIconifiedByDefault(false);
        sv.setOnQueryTextListener(this);
        sv.setSubmitButtonEnabled(true);
        sv.setQueryHint("查詢食材");

        f10202_progressBar = (ProgressBar) findViewById(R.id.f10202_progressBar);
        f10202_progressBar.setVisibility(View.INVISIBLE);
        f10202_contentdes = (TextView) findViewById(R.id.f10202_contentdes);
        f10202_contentdes.setVisibility(View.INVISIBLE);

        initDB();
        if (dbHper.RecCount() != 0) {
            showData();
        } else {
//            dialog = ProgressDialog.show(this, "讀取中", "請稍候");
            handler.postDelayed(updateTimer, 1000);
            f10202_contentdes.setText(getString(R.string.f10202_contentdes));
            f10202_progressBar.setVisibility(View.VISIBLE);
            f10202_contentdes.setVisibility(View.VISIBLE);
            F10202UpdateDataWork work = new F10202UpdateDataWork(this);
            work.setHandler(mHandler);//先指定個工人...注意上面的宣告要看一下
            work.setProgressBar(f10202_progressBar);//傳遞引數過去...
            work.start();//開始運行 TODO
        }
    }

    private void showData() {
        mList = new ArrayList<>();
        for (int i = 0; i < recSet.size(); i++) {
            item = new HashMap<>();
            String[] fld = recSet.get(i).split("#");
            item.put("Fish", fld[1]);
            item.put("Market", fld[2]);
            item.put("Avgprice", fld[3]);
            item.put("Tradedate", fld[4]);
            mList.add(item);
        }
        //----------------設定listView----------------
        SimpleAdapter adapter = new SimpleAdapter(
                getApplicationContext(),
                mList,
                R.layout.list_f10202,
                new String[]{"Fish", "Market", "Avgprice", "Tradedate"},
                new int[]{R.id.t001, R.id.t002, R.id.t003, R.id.t004}
        );
        list001.setAdapter(adapter);
    }

    private void initDB() {
        if (dbHper == null)
            dbHper = new F10202DbHelper(this, DB_FILE, null, DBversion);
        recSet = dbHper.getRecSet();
    }

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
                        try {
                            lidDate = jsonOb1.getString("品種代碼");
                            ridDate = jsonOb2.getString("品種代碼");
                        } catch (JSONException jsone) {
                            jsone.printStackTrace();
                        }
                        return lidDate.compareTo(ridDate);
                    }
                }
        );
        return new JSONArray(json);
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

    @Override
    public boolean onQueryTextChange(String newText) {
        if (TextUtils.isEmpty(newText)) {
            list001.clearTextFilter();
        } else {
            list001.setFilterText(newText);
        }
        return true;
    }

    @Override
    protected void onPause() {
        handler.removeCallbacks(updateTimer);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacks(updateTimer);
        super.onDestroy();
    }

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
