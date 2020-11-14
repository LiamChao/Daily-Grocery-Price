package tw.tcnr109a05.grocery;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.TableRow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import static com.google.android.material.bottomnavigation.LabelVisibilityMode.LABEL_VISIBILITY_LABELED;

public class F10300 extends F000 implements SearchView.OnQueryTextListener {

    private static final String DB_FILE = "nutrition.db";
    private static final String DB_TABLE = "nutrition";
    private static final int DBversion = 1;
    private Intent intent = new Intent();
    private F10300DbHelper dbHper;
    private ArrayList<String> recSet;
    private ListView list001;
    private TableRow tab01;
    private SearchView sv;
    private List<Map<String, Object>> mList;
    private HashMap<String, Object> item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f10300);
        setupViewComponent();

        BottomNavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setSelectedItemId(R.id.navigation_food);

        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_food:
                        return true;
                    case R.id.navigation_bmr:
                        startActivity(new Intent(getApplicationContext(), F10301.class));
                        overridePendingTransition(0, 0);
                        F10300.this.finish();
                        return true;
                }
                return false;
            }
        });

        // Bottom Navigation JAVA 開始
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.Food);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem mrnuitem) {
                switch (mrnuitem.getItemId()) {
                    case R.id.News:
                        startActivity(new Intent(getApplicationContext(), F10100.class));
                        overridePendingTransition(0, 0);
                        F10300.this.finish();
                        return true;
                    case R.id.Price:
                        startActivity(new Intent(getApplicationContext(), F10200.class));
                        overridePendingTransition(0, 0);
                        F10300.this.finish();
                        return true;
                    case R.id.Food:
                        return true;
                    case R.id.Market:
                        startActivity(new Intent(getApplicationContext(), F10400.class));
                        overridePendingTransition(0, 0);
                        F10300.this.finish();
                        return true;

                    case R.id.Book:
                        startActivity(new Intent(getApplicationContext(), F10500.class));
                        overridePendingTransition(0, 0);
                        F10300.this.finish();
                        return true;
                }
                return false;
            }
        });
        BottomNavigationHelper.removeShiftMode(bottomNavigationView);
        bottomNavigationView.setLabelVisibilityMode(LABEL_VISIBILITY_LABELED);
        // Bottom Navigation JAVA 結束
        F10300DbHelper dbHper = new F10300DbHelper(getApplicationContext(), DB_FILE, null, DBversion);
    }

    private void setupViewComponent() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int newscrollheight = displayMetrics.heightPixels * 95 / 100; // 設定ScrollView使用尺寸

        list001 = (ListView)

                findViewById(R.id.f10300_listView01);
        list001.getLayoutParams().height = newscrollheight;
        list001.setLayoutParams(list001.getLayoutParams()); // 重定ScrollView大小

        tab01 = (TableRow)

                findViewById(R.id.f10300_tab01);

        list001.setTextFilterEnabled(true);
        sv = (SearchView)

                findViewById(R.id.f10300_SearchView);
        sv.setIconifiedByDefault(false);
        sv.setOnQueryTextListener(this);
        sv.setSubmitButtonEnabled(true);
        sv.setQueryHint("查詢食材");

        final ProgressDialog progress = new ProgressDialog(this);
        progress.setTitle("資料讀取中");
        progress.setMessage("請稍後片刻");
        progress.show();

        Runnable progressRunnable = new Runnable() {
            @Override
            public void run() {
                progress.cancel();
            }
        };

        Handler pdCanceller = new Handler();
        pdCanceller.postDelayed(progressRunnable, 1000);

        //--------連接SQLite------
        initDB();//注意呼叫SQLite順序很重要
        showdata();
    }

    private void showdata() {
        mList = new ArrayList<>();
        for (int i = 0; i < recSet.size(); i++) {
            item = new HashMap<String, Object>();// TODO 要注意物件new在for迴圈內, 不然會出錯變重複
            String[] fld = recSet.get(i).split("#");
            item.put("name", fld[1]);
            item.put("item", fld[2]);
            item.put("content", fld[3]);
            item.put("description", fld[4]);
            mList.add(item);
        }
        //==========設定listView============
        SimpleAdapter adapter = new SimpleAdapter(
                getApplicationContext(),
                mList,
                R.layout.list_f10300,
                new String[]{"name", "item", "content", "description"},
                new int[]{R.id.t001, R.id.t002, R.id.t003, R.id.t004}
        );
        list001.setAdapter(adapter);//將抓取的資料設定到表格視窗
    }


    private void initDB() {
        if (dbHper == null)//沒有連線的話，就開啟連線
            dbHper = new F10300DbHelper(this, DB_FILE, null, DBversion);
        recSet = dbHper.getRecSet();
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

}
