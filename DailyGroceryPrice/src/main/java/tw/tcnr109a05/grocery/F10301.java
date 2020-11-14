package tw.tcnr109a05.grocery;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.DecimalFormat;


import static com.google.android.material.bottomnavigation.LabelVisibilityMode.LABEL_VISIBILITY_LABELED;

public class F10301 extends F000 implements View.OnClickListener {

    private Intent intent = new Intent();
    private Button b001, b002;
    private TextView t001, t002, t003;
    private EditText e001, e002, e003;
    private RadioGroup radiogroup;
    private RadioButton rb001, rb002, rb003, rb004, rb005;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f10301);
        setupViewComponent();

        BottomNavigationView navigationView = findViewById(R.id.nav_view);
        //頂部menu切換
        navigationView.setSelectedItemId(R.id.navigation_bmr);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_food:
                        startActivity(new Intent(getApplicationContext(), F10300.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.navigation_bmr:
                        return true;
                }
                return false;
            }
        });

        // 底部menu切換
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.Food);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem mrnuitem) {
                switch (mrnuitem.getItemId()) {
                    case R.id.News:
                        startActivity(new Intent(getApplicationContext(), F10100.class));
                        overridePendingTransition(0, 0);
                        F10301.this.finish();
                        return true;
                    case R.id.Price:
                        startActivity(new Intent(getApplicationContext(), F10200.class));
                        overridePendingTransition(0, 0);
                        F10301.this.finish();
                        return true;
                    case R.id.Food:
                        return true;
                    case R.id.Market:
                        startActivity(new Intent(getApplicationContext(), F10400.class));
                        overridePendingTransition(0, 0);
                        F10301.this.finish();
                        return true;

                    case R.id.Book:
                        startActivity(new Intent(getApplicationContext(), F10500.class));
                        overridePendingTransition(0, 0);
                        F10301.this.finish();
                        return true;
                }
                return false;
            }
        });
        BottomNavigationHelper.removeShiftMode(bottomNavigationView);
        bottomNavigationView.setLabelVisibilityMode(LABEL_VISIBILITY_LABELED);
        // Bottom Navigation JAVA 結束
    }

    private void setupViewComponent() {

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        b001 = (Button) findViewById(R.id.f10301_b001);  //  男生按鈕
        b002 = (Button) findViewById(R.id.f10301_b002); //女生按鈕
        t001 = (TextView) findViewById(R.id.f10301_t007); //計算結果
        t002 = (TextView) findViewById(R.id.f10301_t010); //計算結果
        t003 = (TextView) findViewById(R.id.f10301_t012);//BMR建議
        e001 = (EditText) findViewById(R.id.f10301_e001); //age
        e002 = (EditText) findViewById(R.id.f10301_e002); // height
        e003 = (EditText) findViewById(R.id.f10301_e003); // weight
        radiogroup = (RadioGroup) findViewById(R.id.radiogroup);
        rb001 = (RadioButton) findViewById(R.id.f10301_rb001); // 久坐、幾乎沒運動	BMR X 1.2
        rb002 = (RadioButton) findViewById(R.id.f10301_rb002);//  每週低強度運動1~3天	BMR X 1.375
        rb003 = (RadioButton) findViewById(R.id.f10301_rb003);// 每週中強度運動3~5天	BMR X 1.55
        rb004 = (RadioButton) findViewById(R.id.f10301_rb004);// 每週高強度運動6~7天	BMR X 1.725
        rb005 = (RadioButton) findViewById(R.id.f10301_rb005);// 勞力密集工作或是每天高強度訓練	BMR X 1.9
        b001.setOnClickListener(this);//男性計算
        b002.setOnClickListener(this);//女性計算

    }

    @Override
    public void onClick(View v) {
        if (e001.getText().toString().length() != 0 && e002.getText().toString().length() != 0 && e003.getText().toString().length() != 0) {
            double Weight;   //e003
            double Height;  //e002
            double Age;  //e001
            double bmrMan, bmrWoman, TDEE01, TDEE02, TDEE03, TDEE04, TDEE05, TDEE06, TDEE07, TDEE08, TDEE09, TDEE10;
            Weight = Double.parseDouble(e003.getText().toString());
            Height = Double.parseDouble(e002.getText().toString());
            Age = Double.parseDouble(e001.getText().toString());
            DecimalFormat nf = new DecimalFormat("0.000");
            //計算BMR
            // BMR(男)=(13.7×體重(公斤))+(5.0×身高(公分))-(6.8×年齡)+66
            // BMR(女)=(9.6×體重(公斤))+(1.8×身高(公分))-(4.7×年齡)+655
            bmrMan = ((13.7 * Weight) + (5 * Height) - (6.8 * Age) + 66);
            bmrWoman = ((9.6 * Weight) + (1.8 * Height) - (4.7 * Age) + 655);
//         計算TDEE
            TDEE01 = bmrMan * 1.2;
            TDEE02 = bmrMan * 1.375;
            TDEE03 = bmrMan * 1.55;
            TDEE04 = bmrMan * 1.725;
            TDEE05 = bmrMan * 1.9;
            TDEE06 = bmrWoman * 1.2;
            TDEE07 = bmrWoman * 1.375;
            TDEE08 = bmrWoman * 1.55;
            TDEE09 = bmrWoman * 1.725;
            TDEE10 = bmrWoman * 1.9;
            //限制小數點位數

//============開始計算===============
            if (Age == 0 || Height == 0 || Weight == 0) {
                Toast toast1 = Toast.makeText(this, R.string.f10301_e001, Toast.LENGTH_SHORT);
                toast1.show();
            } else {
                switch (v.getId()) {
                    case R.id.f10301_b001:  //男
                        t001.setText(nf.format(bmrMan) + "");
                        if (bmrMan < 1400) {
                            t003.setText(R.string.f10301_t011);
                        } else if (bmrMan >= 1400 && bmrMan <= 1700) {
                            t003.setText(R.string.f10301_t012);
                        } else if (bmrMan > 1700) {
                            t003.setText(R.string.f10301_t013);
                        }
//男性基礎代謝率1400～1700 大卡的範圍較為常見。
                        switch (radiogroup.getCheckedRadioButtonId()) {
                            case R.id.f10301_rb001:
                                t002.setText(nf.format(TDEE01) + "");
                                break;
                            case R.id.f10301_rb002:
                                t002.setText(nf.format(TDEE02) + "");
                                break;
                            case R.id.f10301_rb003:
                                t002.setText(nf.format(TDEE03) + "");
                                break;
                            case R.id.f10301_rb004:
                                t002.setText(nf.format(TDEE04) + "");
                                break;
                            case R.id.f10301_rb005:
                                t002.setText(nf.format(TDEE05) + "");
                                break;
                        }
                        break;

                    case R.id.f10301_b002:  //女
                        t001.setText(nf.format(bmrWoman) + "");
                        if (bmrMan < 1100) {
                            t003.setText(R.string.f10301_t011);
                        } else if (bmrMan >= 1100 && bmrMan <= 1400) {
                            t003.setText(R.string.f10301_t012);
                        } else if (bmrMan > 1400) {
                            t003.setText(R.string.f10301_t013);
                        }
                        //女性的基礎代謝率會落在 1100～1400 大卡、
                        switch (radiogroup.getCheckedRadioButtonId()) {
                            case R.id.f10301_rb001:
                                t002.setText(nf.format(TDEE06) + "");
                                break;
                            case R.id.f10301_rb002:
                                t002.setText(nf.format(TDEE07) + "");
                                break;
                            case R.id.f10301_rb003:
                                t002.setText(nf.format(TDEE08) + "");
                                break;
                            case R.id.f10301_rb004:
                                t002.setText(nf.format(TDEE09) + "");
                                break;
                            case R.id.f10301_rb005:
                                t002.setText(nf.format(TDEE10) + "");
                                break;
                        }
                        break;
                }
            }
        } else {
            Toast toast = Toast.makeText(this, R.string.f10301_e002, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
