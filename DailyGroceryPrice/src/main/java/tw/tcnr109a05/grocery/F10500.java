package tw.tcnr109a05.grocery;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import tw.tcnr109a05.grocery.providers.FriendsContentProvider;

import static com.google.android.material.bottomnavigation.LabelVisibilityMode.LABEL_VISIBILITY_LABELED;

public class F10500 extends F000 {

    private static final String DB_FILE = "friends.db";
    private static final String DB_TABLE_f10501 = "recipe";
    public static String myselection = "";
    public static String myargs[] = new String[]{};
    public static String myorder = "id ASC"; // 排序欄位
    // ------------------
    //--------------------------
    private static ContentResolver mContRes;
    int tcount;
    String TAG = "food=";
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private String[] MYCOLUMN = new String[]{"id", "recipe", "introduction", "ingredients", "step", "r_id"};
    private F10501 exploreFragment;
    private FlightsFragment flightsFragment;
    private TravelFragment travelFragment;
    private MenuItem b_item;
    private FriendDbHelper dbHper;
    private int DBver = 1;
    private String dishes_name;
    private String creative;
    private String specialty_dishes;
    private String practic;
    private ArrayList<String> recSet;
    private String msg;
    private Cursor cur;
    private FriendsContentProvider mm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main01);

        u_loaddata();

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
        F00000.mem = 2;
        mContRes = getContentResolver();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.Book);
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
                        F10500.this.finish();
                        return true;
                    case R.id.Price:
                        startActivity(new Intent(getApplicationContext(), F10200.class));
                        overridePendingTransition(0, 0);
                        F10500.this.finish();
                        return true;
                    case R.id.Food:
                        startActivity(new Intent(getApplicationContext(), F10300.class));
                        overridePendingTransition(0, 0);
                        F10500.this.finish();
                        return true;
                    case R.id.Market:
                        startActivity(new Intent(getApplicationContext(), F10400.class));
                        overridePendingTransition(0, 0);
                        F10500.this.finish();
//                        Toast toast = Toast.makeText(getApplicationContext(), "施工中.......", Toast.LENGTH_SHORT);
//                        toast.setGravity(Gravity.CENTER, 50, 50);
//
//                        TextView text = (TextView) toast.getView().findViewById(android.R.id.message);
//                        text.setTextSize(20);
//                        toast.show();
                        return true;

                    case R.id.Book:
//                        startActivity(new Intent(getApplicationContext(),F10500.class));
//                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });
        BottomNavigationHelper.removeShiftMode(bottomNavigationView);
        bottomNavigationView.setLabelVisibilityMode(LABEL_VISIBILITY_LABELED);

        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tab_layout);
        exploreFragment = new F10501();
        flightsFragment = new FlightsFragment();
        travelFragment = new TravelFragment();

        tabLayout.setupWithViewPager(viewPager);
        dbmysql();
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), 0);
        viewPagerAdapter.addFragment(exploreFragment, "最新食譜");
        viewPagerAdapter.addFragment(flightsFragment, "熱門食譜");
        viewPagerAdapter.addFragment(travelFragment, "個人食譜");
        viewPager.setAdapter(viewPagerAdapter);

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        b_item = menu.findItem(R.id.f10501_main);
        this.menu = menu;
        u_menu_main();
        b_item.setVisible(true);
        return true;
    }

    private void dbmysql() {
        F00000.mem = 2;
        mContRes = getContentResolver();

        cur = mContRes.query(FriendsContentProvider.CONTENT_URI, MYCOLUMN, null, null, null);
        int jjkjl = 0;
        cur.moveToFirst(); // 一定要寫，不然會出錯
        // // ---------------------------
        try {
            String result = DBConnector.executeQuery("SELECT * FROM recipe");
//        String r = result.toString().trim();
//==========================================
            //以下程式碼一定要放在前端藍色程式碼執行之後，才能取得狀態碼
            //存取類別成員 DBConnector.httpstate 判定是否回應 200(連線要求成功)
            Log.d(TAG, "httpstate=" + DBConnector.httpstate);

            if (DBConnector.httpstate == 200) {
                Uri uri = FriendsContentProvider.CONTENT_URI;
                mContRes.delete(uri, null, null);  //清空SQLite
                Toast.makeText(getBaseContext(), "已經完成由伺服器會入資料",
                        Toast.LENGTH_LONG).show();
            } else {
                int checkcode = DBConnector.httpstate / 100;
                switch (checkcode) {
                    case 1:
                        msg = "資訊回應(code:" + DBConnector.httpstate + ")";
                        break;
                    case 2:
                        msg = "已經完成由伺服器會入資料(code:" + DBConnector.httpstate + ")";
                        break;
                    case 3:
                        msg = "伺服器重定向訊息，請稍後在試(code:" + DBConnector.httpstate + ")";
                        break;
                    case 4:
                        msg = "用戶端錯誤回應，請稍後在試(code:" + DBConnector.httpstate + ")";
                        break;
                    case 5:
                        msg = "伺服器error responses，請稍後在試(code:" + DBConnector.httpstate + ")";
                        break;
                }
                Toast.makeText(getBaseContext(), msg, Toast.LENGTH_LONG).show();
            }
//======================================
            // 選擇讀取特定欄位
            // String result = DBConnector.executeQuery("SELECT id,name FROM
            // member");
            /*******************************************************************************************
             * SQL 結果有多筆資料時使用JSONArray 只有一筆資料時直接建立JSONObject物件 JSONObject
             * jsonData = new JSONObject(result);
             *******************************************************************************************/
            JSONArray jsonArray = new JSONArray(result);

            // -------------------------------------------------------
            if (jsonArray.length() > 0) { // MySQL 連結成功有資料
                Uri uri = FriendsContentProvider.CONTENT_URI;
                mContRes.delete(uri, null, null); // 匯入前,刪除所有SQLite資料

                // ----------------------------
                // 處理JASON 傳回來的每筆資料
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonData = jsonArray.getJSONObject(i);
                    //
                    ContentValues newRow = new ContentValues();
                    // --(1) 自動取的欄位
                    // --取出 jsonObject
                    // 每個欄位("key","value")-----------------------
                    Iterator itt = jsonData.keys();
                    while (itt.hasNext()) {
                        String key = itt.next().toString();
                        String value = jsonData.getString(key); // 取出欄位的值
                        if (value == null) {
                            continue;
                        } else if ("".equals(value.trim())) {
                            continue;
                        } else {
                            jsonData.put(key, value.trim());
                        }
                        // ------------------------------------------------------------------
                        newRow.put(key, value.toString()); // 動態找出有幾個欄位
                        // -------------------------------------------------------------------
                        Log.d(TAG, "第" + i + "個欄位 key:" + key + " value:" + value);

                    }
                    // ---(2) 使用固定已知欄位---------------------------
                    // newRow.put("id", jsonData.getString("id").toString());
                    // newRow.put("name",
                    // jsonData.getString("name").toString());
                    // newRow.put("grp", jsonData.getString("grp").toString());
                    // newRow.put("address", jsonData.getString("address")
                    // .toString());
                    // -------------------加入SQLite---------------------------------------
                    mContRes.insert(FriendsContentProvider.CONTENT_URI, newRow);
                }
                // ---------------------------
            } else {
                Toast.makeText(F10500.this, "主機資料庫無資料", Toast.LENGTH_LONG).show();
            }
            // --------------------------------------------------------

        } catch (Exception e) {
            Log.e("log_tag", e.toString());
        }
        cur.close();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragments = new ArrayList<>();
        private List<String> fragmentTitle = new ArrayList<>();

        public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        public void addFragment(Fragment fragment, String title) {
            fragments.add(fragment);
            fragmentTitle.add(title);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitle.get(position);
        }
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
    }
}