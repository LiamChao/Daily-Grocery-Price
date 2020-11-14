package tw.tcnr109a05.grocery;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.internal.SignInButtonImpl;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import tw.tcnr109a05.grocery.providers.FriendsContentProvider;

public class F00000 extends AppCompatActivity implements View.OnClickListener {

    private static final String DB_File = "friends.db";
    private static final int RC_SIGN_IN = 9001;
    public static String myselection = "";
    public static String myargs[] = new String[]{};
    public static String myorder = "id ASC"; // 排序欄位
    public static int mem = 0;
    private static ContentResolver mContRes;
    public int loginon = 1;
    int tcount;
    private Button b001;
    private Dialog mLoginDlg;
    //--------------------------
    private Button b003;
    private Dialog mSignUpDlg;
    private Intent intent = new Intent();
    private SQLiteDatabase mFriendDb;
    private int DBversion = 1;
    private String tMemberID, tEmail, tcheck_Email, tPassword, tcheck_Password;
    private String result;
    private String[] MYCOLUMN = new String[]{"id", "MemberID", "username", "password", "email", "name"
            , "sex", "birthday", "age", "address", "tel", "social", "level"};
    // ------------------
    private Handler handler = new Handler();
    private long startTime;
    private int autotime = 1000;//autotime設定幾秒跑一次匯入MySQL
    private String TAG = "TCNR=>";
    private String msg;
    private SignInButtonImpl b002;
    private GoogleSignInClient mGoogleSignInClient;
    private Button.OnClickListener ON = new Button.OnClickListener() {

        @Override
        public void onClick(View v) {

        }
    };

    public static String u_md5(String content) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(content.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("NoSuchAlgorithmException", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("UnsupportedEncodingException", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) {
                hex.append("0");
            }
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }
//    private Runnable updateTimer = new Runnable() {
//        @Override
//        public void run() {
//            handler.postDelayed(this, autotime*1000);//真正延遲的時間
//            //------執行匯入MySQL----
//            dbmysql();
//            //-----------------------------
//        }
//    };
//    private void dbmysql() {
//        mContRes = getContentResolver();
//        Cursor cur = mContRes.query(FriendsContentProvider.CONTENT_URI_f00000, MYCOLUMN, null, null, null);
//        cur.moveToFirst(); // 一定要寫，不然會出錯
//        // // ---------------------------
//        try {
//            String result = DBConnector.executeQuery("SELECT * FROM members");
////        String r = result.toString().trim();
////==========================================
//            //以下程式碼一定要放在前端藍色程式碼執行之後，才能取得狀態碼
//            //存取類別成員 DBConnector.httpstate 判定是否回應 200(連線要求成功)
//            Log.d(TAG, "httpstate=" + DBConnector.httpstate);
//
//            if (DBConnector.httpstate == 200) {
//                Uri uri = FriendsContentProvider.CONTENT_URI_f00000;
//                mContRes.delete(uri, null, null);  //清空SQLite
//                Toast.makeText(getBaseContext(), "已經完成由伺服器匯入資料",
//                        Toast.LENGTH_LONG).show();
//            } else {
//                int checkcode = DBConnector.httpstate / 100;
//                switch (checkcode) {
//                    case 1:
//                        msg = "資訊回應(code:" + DBConnector.httpstate + ")";
//                        break;
//                    case 2:
//                        msg = "已經完成由伺服器匯入資料(code:" + DBConnector.httpstate + ")";
//                        break;
//                    case 3:
//                        msg = "伺服器重定向訊息，請稍後再試(code:" + DBConnector.httpstate + ")";
//                        break;
//                    case 4:
//                        msg = "用戶端錯誤回應，請稍後再試(code:" + DBConnector.httpstate + ")";
//                        break;
//                    case 5:
//                        msg = "伺服器error responses，請稍後再試(code:" + DBConnector.httpstate + ")";
//                        break;
//                }
//                Toast.makeText(getBaseContext(), msg, Toast.LENGTH_LONG).show();
//            }
////======================================
//            // 選擇讀取特定欄位
//            // String result = DBConnector.executeQuery("SELECT id,name FROM
//            // members");
//            /*******************************************************************************************
//             * SQL 結果有多筆資料時使用JSONArray 只有一筆資料時直接建立JSONObject物件 JSONObject
//             * jsonData = new JSONObject(result);
//             *******************************************************************************************/
//            JSONArray jsonArray = new JSONArray(result);
//
//            // -------------------------------------------------------
//            if (jsonArray.length() > 0) { // MySQL 連結成功有資料
//                Uri uri = FriendsContentProvider.CONTENT_URI_f00000;
//                mContRes.delete(uri, null, null); // 匯入前,刪除所有SQLite資料
//
//                // ----------------------------
//                // 處理JASON 傳回來的每筆資料
//                for (int i = 0; i < jsonArray.length(); i++) {
//                    JSONObject jsonData = jsonArray.getJSONObject(i);
//                    //
//                    ContentValues newRow = new ContentValues();
//                    // --(1) 自動取的欄位
//                    // --取出 jsonObject
//                    // 每個欄位("key","value")-----------------------
//                    Iterator itt = jsonData.keys();
//                    while (itt.hasNext()) {
//                        String key = itt.next().toString();
//                        String value = jsonData.getString(key); // 取出欄位的值
//                        if (value == null) {
//                            continue;
//                        } else if ("".equals(value.trim())) {
//                            continue;
//                        } else {
//                            jsonData.put(key, value.trim());
//                        }
//                        // ------------------------------------------------------------------
//                        newRow.put(key, value.toString()); // 動態找出有幾個欄位
//                        // -------------------------------------------------------------------
//                        Log.d(TAG, "第" + i + "個欄位 key:" + key + " value:" + value);
//
//                    }
//                    // ---(2) 使用固定已知欄位---------------------------
//                    // newRow.put("id", jsonData.getString("id").toString());
//                    // newRow.put("name",
//                    // jsonData.getString("name").toString());
//                    // newRow.put("grp", jsonData.getString("grp").toString());
//                    // newRow.put("address", jsonData.getString("address")
//                    // .toString());
//                    // -------------------加入SQLite---------------------------------------
//                    mContRes.insert(FriendsContentProvider.CONTENT_URI_f00000, newRow);
//                }
//                // ---------------------------
//            } else {
//                Toast.makeText(F00000.this, "主機資料庫無資料", Toast.LENGTH_LONG).show();
//            }
//            // --------------------------------------------------------
//
//        } catch (Exception e) {
//            // Log.e("log_tag", e.toString());
//        }
//        cur.close();
//        //------------------------------------
//        sqliteupdate();//抓取SQLite資料
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f00000);
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
        b001 = (Button) findViewById(R.id.f00000_b001);
        b003 = (Button) findViewById(R.id.f00000_b003);
//        b002= (SignInButton)findViewById(R.id.f00000_b002);//google login


        findViewById(R.id.f00000_b002).setOnClickListener(this);
        b001.setOnClickListener(this);
        b003.setOnClickListener(this);
//        b002.setOnClickListener(this);


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
//        handler.postDelayed(updateTimer, 1000);
//        sqliteupdate();//抓取SQLite資料

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        signOut();

    }

    private void sqliteupdate() {
        Cursor c = mContRes.query(FriendsContentProvider.CONTENT_URI_f00000, MYCOLUMN, null, null, null);
        tcount = c.getCount();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item);
        for (int i = 0; i < tcount; i++) {
            c.moveToPosition(i);
            adapter.add(c.getString(0) + "," + c.getString(1) + "," + c.getString(2) + "," + c.getString(3));
        }
        c.close();
        //--設定spinner
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        mSpnName.setAdapter(adapter);
//        mSpnName.setOnItemSelectedListener(this);
//        u_spinmove(up_item);//spinner 小窗跳到第幾筆
        //------ 宣告鈴聲 ---------------------------
        ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 100); // 100=max
        toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_NETWORK_LITE, 500);
        toneG.release();
    }

    private void mysql_insert_f000000() {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("MemberID", tMemberID));
        nameValuePairs.add(new BasicNameValuePair("username", tEmail));
        nameValuePairs.add(new BasicNameValuePair("password", tPassword));
        nameValuePairs.add(new BasicNameValuePair("name", ""));
        nameValuePairs.add(new BasicNameValuePair("sex", ""));
        nameValuePairs.add(new BasicNameValuePair("birthday", "0000-00-00"));
        nameValuePairs.add(new BasicNameValuePair("age", ""));
        nameValuePairs.add(new BasicNameValuePair("address", ""));
        nameValuePairs.add(new BasicNameValuePair("email", ""));
        nameValuePairs.add(new BasicNameValuePair("tel", ""));
        nameValuePairs.add(new BasicNameValuePair("social", ""));
        nameValuePairs.add(new BasicNameValuePair("level", "100"));

        try {
            Thread.sleep(500); //  延遲Thread 睡眠0.5秒
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//-----------------------------------------------
        String result = DBConnector.executeInsert_f00000("SELECT * FROM members", nameValuePairs);
//-----------------------------------------------
    }

    private String FindRec(String tusername, String tpassword) {
        mContRes = getContentResolver();
        Cursor cur = mContRes.query(FriendsContentProvider.CONTENT_URI_f00000,
                MYCOLUMN, null, null, null);
        cur.moveToFirst(); // 一定要寫，不然會出錯
        String fldSet = null;
        try {

            String sql = "SELECT * FROM members WHERE username LIKE " + "'" + tusername + "'" + " AND password LIKE " + "'" + tpassword + "'";

            String result = DBConnector.executeQuery(sql);
            JSONArray jsonArray = new JSONArray(result);

            for (int i = 0; i < jsonArray.length(); i++) {

                fldSet += "\n";
            }
        } catch (Exception e) {
            // Log.e("log_tag", e.toString());
        }
        cur.close();
        return fldSet;
    }

    private String FindRec1(String tusername) {
        mContRes = getContentResolver();
        Cursor cur = mContRes.query(FriendsContentProvider.CONTENT_URI_f00000,
                MYCOLUMN, null, null, null);
        cur.moveToFirst(); // 一定要寫，不然會出錯

        String fldSet = null;
        try {
            String sql = "SELECT * FROM members WHERE username LIKE " + "'" + tusername + "'" + " ORDER BY id ASC";

            String result = DBConnector.executeQuery(sql);
            JSONArray jsonArray = new JSONArray(result);

            for (int i = 0; i < jsonArray.length(); i++) {

                fldSet += "\n";
            }
        } catch (Exception e) {
            // Log.e("log_tag", e.toString());
        }
        cur.close();
        return fldSet;
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                this.finish();
                break;

        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.f00000_b001:

                mLoginDlg = new Dialog(F00000.this);
                mLoginDlg.setTitle(getString(R.string.f00001_title));
                mLoginDlg.setCancelable(false);

                mLoginDlg.setContentView(R.layout.login_dlg);

                Button loginBtnOK = (Button) mLoginDlg.findViewById(R.id.f00001_btnOK);
                Button loginBtnCancel = (Button) mLoginDlg.findViewById(R.id.f00001_btnCancel);

                loginBtnCancel.setOnClickListener(this);
                loginBtnOK.setOnClickListener(this);
                mLoginDlg.show();

                break;

            case R.id.f00001_btnOK:
                EditText loginEmail = (EditText) mLoginDlg.findViewById(R.id.f00001_e001);
                EditText loginPassword = (EditText) mLoginDlg.findViewById(R.id.f00001_e002);


                String reslt = null;
                //查詢name在name是否有資料
                String t1Email = loginEmail.getText().toString().trim();
                String t1Password = loginPassword.getText().toString().trim();

                if (t1Email.length() != 0 && t1Password.length() != 0) {
                    String rec = FindRec(t1Email, t1Password);//執行sqlite的定義
                    if (rec != null) {
                        result = "登入成功!";
                        loginon = 1;//帳密正確 進入登入

                        SharedPreferences login =
                                getSharedPreferences("GAME_RESULT", 0);
                        login
                                .edit()
                                .putInt("flag", loginon)
                                .putString("email", t1Email)
                                .commit();

                        Intent it = new Intent();
                        Bundle bundle = new Bundle();

                        bundle.putInt("flag", loginon);
                        bundle.putString("email", t1Email);
                        it.putExtras(bundle);

                        setResult(RESULT_OK, it); //0
                        this.finish();
                        mLoginDlg.cancel();
                    } else {
                        result = "帳號密碼輸入錯誤";
                    }
                    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG)
                            .show();
                } else {
                    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
                }


                break;
            case R.id.f00000_b002://google登入
                signIn();

                break;
            case R.id.f00001_btnCancel:
                mLoginDlg.cancel();
                break;
            case R.id.f00000_b003://註冊會員
                mSignUpDlg = new Dialog(F00000.this);
                mSignUpDlg.setTitle(getString(R.string.f00002_title));
                mSignUpDlg.setCancelable(false);

                mSignUpDlg.setContentView(R.layout.signup_dlg);

                Button loginBtn2OK = (Button) mSignUpDlg.findViewById(R.id.f00002_btnOK);
                Button loginBtn2Cancel = (Button) mSignUpDlg.findViewById(R.id.f00002_btnCancel);

                loginBtn2Cancel.setOnClickListener(this);
                loginBtn2OK.setOnClickListener(this);
                mSignUpDlg.show();
                break;
            case R.id.f00002_btnOK:
                EditText MemberID = (EditText) mSignUpDlg.findViewById(R.id.f00002_e001);
                EditText Email = (EditText) mSignUpDlg.findViewById(R.id.f00002_e002);
                EditText checkEmail = (EditText) mSignUpDlg.findViewById(R.id.f00002_e003);
                EditText Password = (EditText) mSignUpDlg.findViewById(R.id.f00002_e004);
                EditText checkPassword = (EditText) mSignUpDlg.findViewById(R.id.f00002_e005);

                tMemberID = MemberID.getText().toString().trim();
                tEmail = Email.getText().toString().trim();
                tcheck_Email = checkEmail.getText().toString().trim();
                tPassword = Password.getText().toString().trim();
                tcheck_Password = checkPassword.getText().toString().trim();


                if (tMemberID.equals("") || tEmail.equals("") || tPassword.equals("") || tcheck_Password.equals("")) {
                    Toast.makeText(getApplicationContext(), "輸入有誤!", Toast.LENGTH_SHORT).show();
                    return;
                }
//                if  (!tEmail.equals(tcheck_Email)) {
//                    Toast.makeText(getApplicationContext(), "確認信箱錯誤", Toast.LENGTH_SHORT).show();
//                    return;
//                }
                if (!tPassword.equals(tcheck_Password)) {
                    Toast.makeText(getApplicationContext(), "密碼與確認密碼不符", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (tEmail.length() != 0 || tPassword.length() != 0) {
                    String rec = FindRec1(tEmail);//執行mysql的定義,判斷帳號重複的方法
                    if (rec != null) {
                        Email.setText("");
                        Toast.makeText(getApplicationContext(), "帳號重複!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                String msg = null;
                mysql_insert_f000000();//真正執行SQL
//                dbmysql();

//                if (rowID != -1) {//-1代表失敗
////                    MemberID.setHint("請繼續輸入");
//                    msg = "註冊成功 !";
//                    mSignUpDlg.cancel();
//                } else {
//                    MemberID.setText("");
//                    Email.setText("");
//                    checkEmail.setText("");
//                    Password.setText("");
//                    checkPassword.setText("");
//                    msg = "註冊失敗!";
//                }
//                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

                mSignUpDlg.cancel();
                break;
            case R.id.f00002_btnCancel:

                Toast.makeText(getApplicationContext(), "你已取消註冊!", Toast.LENGTH_SHORT).show();
                mSignUpDlg.cancel();
                break;

        }
    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //--START_EXCLUDE--
                        updateUI(null);
                        // [END_EXCLUDE]
//                        img.setImageResource(R.drawable.googleg_color); //還原圖示
                    }
                });

    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void updateUI(GoogleSignInAccount account) {
        int aaa = 1;
        GoogleSignInAccount aa = account;
        if (account != null) {
            //            mStatusTextView.setText(getString(R.string.signed_in_fmt, account.getDisplayName()));
            String g_DisplayName = account.getDisplayName(); //暱稱
            String g_Email = account.getEmail();  //信箱

            result = "登入成功!";
            loginon = 1;//帳密正確 進入登入
            String check_google = Findid(g_Email);
            if (check_google == null) {
//                tEmail=g_Email;
                mysql_insert_gmail(g_Email);
            }
            SharedPreferences login =
                    getSharedPreferences("GAME_RESULT", 0);
            login
                    .edit()
                    .putInt("flag", loginon)
                    .putString("email", g_Email)
                    .commit();

            Intent it = new Intent();
            Bundle bundle = new Bundle();

            bundle.putInt("flag", loginon);
            bundle.putString("email", g_Email);
            it.putExtras(bundle);
            setResult(RESULT_OK, it); //0
            this.finish();
//            String g_GivenName=account.getGivenName(); //Firstname
//            String g_FamilyName=account.getFamilyName(); //Last name

//            mStatusTextView.setText(getString(R.string.signed_in_fmt, account.getDisplayName()) + "\n Email:" +
//                    account.getEmail() + "\n Firstname:" +
//                    account.getGivenName() + "\n Last name:" +
//                    account.getFamilyName());

            //-------改變圖像--------------
//            User_IMAGE = account.getPhotoUrl();
//            if (User_IMAGE == null) {
//                return;
//            }
//            img = (CircleImgView) findViewById(R.id.google_icon);

//            String ss ="http://-------";
//            Bitmap bbb = getBitmapFromURL(ss);
//            img.setImageBitmap(bbb);

//            new AsyncTask<String, Void, Bitmap>() {
//                @Override
//                protected Bitmap doInBackground(String... params) {
//                    String url = params[0];
//                    return getBitmapFromURL(url);
//                }
//
//                @Override
//                protected void onPostExecute(Bitmap result) {
//                    img.setImageBitmap(result);
//                    super.onPostExecute(result);
//                }
//            }.execute(User_IMAGE.toString().trim());
//
//            //            String g_id=account.getId();
//            findViewById(R.id.sign_in_button).setVisibility(View.GONE);
//            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.VISIBLE);
        } else {
//            mStatusTextView.setText(R.string.signed_out);
//
//            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
//            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.GONE);
//
        }

    }

    private void mysql_insert_gmail(String g_mail) {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("MemberID", tMemberID));
        nameValuePairs.add(new BasicNameValuePair("username", g_mail));
        nameValuePairs.add(new BasicNameValuePair("password", tPassword));
        nameValuePairs.add(new BasicNameValuePair("name", ""));
        nameValuePairs.add(new BasicNameValuePair("sex", ""));
        nameValuePairs.add(new BasicNameValuePair("birthday", "0000-00-00"));
        nameValuePairs.add(new BasicNameValuePair("age", ""));
        nameValuePairs.add(new BasicNameValuePair("address", ""));
        nameValuePairs.add(new BasicNameValuePair("email", ""));
        nameValuePairs.add(new BasicNameValuePair("tel", ""));
        nameValuePairs.add(new BasicNameValuePair("social", ""));
        nameValuePairs.add(new BasicNameValuePair("level", "100"));

        try {
            Thread.sleep(500); //  延遲Thread 睡眠0.5秒
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//-----------------------------------------------
        String result = DBConnector.executeInsert_f00000("SELECT * FROM members", nameValuePairs);
//-----------------------------------------------
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.d(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }

    private Bitmap getBitmapFromURL(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();// 不執行這行
        Toast.makeText(getApplication(), "禁用返回鍵", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
//        handler.removeCallbacks(updateTimer);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
//        handler.removeCallbacks(updateTimer);
        super.onDestroy();
    }

}
