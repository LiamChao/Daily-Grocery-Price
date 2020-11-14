package tw.tcnr109a05.grocery;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class F10102_announcement extends AppCompatActivity implements View.OnClickListener {

    private static final String DB_FILE = "news.db";
    private static final int DBversion = 1;
    private TextView title;
    private ArrayList<String> recSet;
    private int index;
    private F10100_FriendDbHelper dbHper;
    private ProgressBar proBar;
    private TextView tReadData;
    private TextView content;
    private ArrayList<Map<String, Object>> mList;
    private HashMap<String, Object> item;
    private SimpleAdapter adapter;
    private ArrayList<Map<String, Object>> mList02;
    private HashMap<String, Object> item02;
    private String anno_title;
    private String anno_content;
    private String anno_date;
    private TextView date;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f10106_announcement_detail);
        setUpviewComponent();
    }

    private void setUpviewComponent() {
        title = (TextView) findViewById(R.id.f10106_title);  //公告標題
        content = (TextView) findViewById(R.id.f10106_content); //內容
        date = (TextView) findViewById(R.id.f10106_date); //   日期
        //-------連結資料庫-----------------
        dbHper = new F10100_FriendDbHelper(this, DB_FILE, null, DBversion);
        recSet = dbHper.getRecSet_anno();
        //-----------------------從F10100接收資料----------------------------------
        Intent intent = this.getIntent();
        index = intent.getIntExtra("KEY_Anno", 0);//getIntExtra後面要給個預設值避免取不到值
        //取得資料
        try {
            String[] fld = recSet.get(index).split("#");
            anno_title = fld[1];  //公告標題
            anno_date = fld[2];
            anno_content = fld[3]; //公告內文
        } catch (RuntimeException e) {
        }
//        //------------將接收資料傳到layout--------------------
        title.setText(anno_title);
        title.setTextColor(getColor(R.color.Red));
        content.setText(anno_content);
        date.setText(anno_date);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.end) {
            this.finish();
        }
        return true;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(), getString(R.string.onBackPressed), Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.news_end, menu);
        return true;
    }
}
