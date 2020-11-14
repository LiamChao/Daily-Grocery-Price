package tw.tcnr109a05.grocery;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;


import static com.google.android.material.bottomnavigation.LabelVisibilityMode.LABEL_VISIBILITY_LABELED;

public class F10200 extends F000 implements SearchView.OnQueryTextListener {

    private static final String DB_FILE = "price.db";
    private static final int DBversion = 1;
    private Intent intent = new Intent();
    private ListView list001;
    private TableRow tab01;
    private SearchView sv;
    private FriendDbHelper_Price dbHper;
    private ArrayList<String> recSet;
    private Handler handler = new Handler();
    private ArrayList<Map<String, Object>> mList;
    private HashMap<String, Object> item;
    private Handler mHandler = new Handler();
    private ProgressBar f10200_progressBar;
    private TextView f10200_contentdes;
    private JSONArray jsonArray;

    private Runnable updateTimer = new Runnable() {
        @Override
        public void run() {
            try {
                String Task_opendata
                        = new TransTask().execute("https://data.coa.gov.tw/Service/OpenData/FromM/FarmTransData.aspx").get();

                //解析json
                jsonArray = new JSONArray(Task_opendata);

                if (dbHper.RecCount_F10200() == jsonArray.length()) {
                    Toast.makeText(getApplicationContext(), "資料為最新版", Toast.LENGTH_LONG).show();
                } else {
                    dbHper.clearRec_F10200();
                    readOpenData();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    };

    Handler tHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    setupViewComponent();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f10200);
        setupViewComponent();

        BottomNavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setSelectedItemId(R.id.navigation_vegetable);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_poultry:
                        startActivity(new Intent(getApplicationContext(), F10201.class));
                        overridePendingTransition(0, 0);
                        F10200.this.finish();
                        return true;
                    case R.id.navigation_vegetable:
                        return true;
                    case R.id.navigation_fish:
                        startActivity(new Intent(getApplicationContext(), F10202.class));
                        overridePendingTransition(0, 0);
                        F10200.this.finish();
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
                    case R.id.News:
                        startActivity(new Intent(getApplicationContext(), F10100.class));
                        overridePendingTransition(0, 0);
                        F10200.this.finish();
                        return true;
                    case R.id.Price:
                        return true;
                    case R.id.Food:
                        startActivity(new Intent(getApplicationContext(), F10300.class));
                        overridePendingTransition(0, 0);
                        F10200.this.finish();
                        return true;
                    case R.id.Market:
                        startActivity(new Intent(getApplicationContext(), F10400.class));
                        overridePendingTransition(0, 0);
                        F10200.this.finish();
                        return true;
                    case R.id.Book:
                        startActivity(new Intent(getApplicationContext(), F10500.class));
                        overridePendingTransition(0, 0);
                        F10200.this.finish();
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

        list001 = (ListView) findViewById(R.id.f10200_listView01);
        list001.getLayoutParams().height = newscrollheight;
        list001.setLayoutParams(list001.getLayoutParams()); // 重定ScrollView大小

        tab01 = (TableRow) findViewById(R.id.f10200_tab01);

        list001.setTextFilterEnabled(true);
        sv = (SearchView) findViewById(R.id.f10200_SearchView);
        sv.setIconifiedByDefault(false);
        sv.setOnQueryTextListener(this);
        sv.setSubmitButtonEnabled(true);
        sv.setQueryHint("查詢食材");

        f10200_progressBar = (ProgressBar) findViewById(R.id.f10200_progressBar);
        f10200_progressBar.setVisibility(View.INVISIBLE);
        f10200_contentdes = (TextView) findViewById(R.id.f10200_contentdes);
        f10200_contentdes.setVisibility(View.INVISIBLE);

        initDB();
        if (dbHper.RecCount_F10200() != 0) {
            handler.postDelayed(updateTimer, 1000);
            showData();
        } else {
//            dialog = ProgressDialog.show(this, "讀取中", "請稍候");
            readOpenData();
        }
    }

    private void readOpenData() {
        f10200_contentdes.setText(getString(R.string.f10200_contentdes));
        f10200_progressBar.setVisibility(View.VISIBLE);
        f10200_contentdes.setVisibility(View.VISIBLE);
        F10200_SQLWriteThread work = new F10200_SQLWriteThread(this, dbHper);
        work.setHandler(mHandler);//先指定個工人...注意上面的宣告要看一下
        work.setProgressBar(f10200_progressBar);//傳遞引數過去...
        work.start();
    }

    private void showData() {
        f10200_progressBar.setVisibility(View.INVISIBLE);
        f10200_contentdes.setVisibility(View.INVISIBLE);
        mList = new ArrayList<>();
        for (int i = 0; i < recSet.size(); i++) {
            item = new HashMap<>();
            String[] fld = recSet.get(i).split("#");
            item.put("num", fld[1]);
            item.put("crop", fld[2]);
            item.put("market", fld[3]);
            item.put("price", fld[4]);
            item.put("c_date", fld[5]);
            mList.add(item);
        }
        //----------------設定listView----------------
        SimpleAdapter adapter = new SimpleAdapter(
                getApplicationContext(),
                mList,
                R.layout.list_f10200,
                new String[]{"crop", "market", "price", "c_date"},
                new int[]{R.id.t001, R.id.t002, R.id.t003, R.id.t004}
        );
        list001.setAdapter(adapter);
    }

    private void initDB() {
        if (dbHper == null)
            dbHper = new FriendDbHelper_Price(this, DB_FILE, null, DBversion);
        recSet = dbHper.getRecSet_F10200();
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
