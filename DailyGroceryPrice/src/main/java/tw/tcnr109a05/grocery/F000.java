package tw.tcnr109a05.grocery;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class F000 extends AppCompatActivity implements View.OnClickListener {

    public static final int LAUNCH_GAME = 0;
    public Menu menu;
    private Dialog mLoginDlg;
    private Button loginBtnOK;
    private Button loginBtnCancel;
    private EditText edtUserName, edtPassword;
    private Dialog mSignUpDlg;
    private Bundle bundle;
    private EditText Email;
    private EditText Password;
    private Intent intent;
    private int loginon;
    private MenuItem about, fans, end;
    //-----------------Dialog的監聽------------------------------------------------------------------
    public Button.OnClickListener on = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.f00001_btnOK:
                    EditText edtUserName = (EditText) mLoginDlg.findViewById(R.id.f00001_e001);
                    EditText edtPassword = (EditText) mLoginDlg.findViewById(R.id.f00001_e002);


//                ans.setText(getString(R.string.f00000_ans)+"\n"+
//                        getString(R.string.f00000_t001)+
//                        edtUserName.getText().toString()+"\n"+
//                                getString(R.string.f00000_t002)+
//                                edtPassword.getText().toString()
//                        );

                    mLoginDlg.cancel();
//                    u_menu_login();
//                    Intent it = new Intent();
//                    it.putExtra("flag",false);
//                    setResult(RESULT_OK, it); //0
                    u_menu_main();
//                    u_bundle();
                    return;
                case R.id.f00001_btnCancel:
                    cancel();
                    mLoginDlg.cancel();
                    break;
                case R.id.f00002_btnOK:
                    EditText f02_e001 = (EditText) mSignUpDlg.findViewById(R.id.f00002_e001);
                    EditText f02_e002 = (EditText) mSignUpDlg.findViewById(R.id.f00002_e002);


//                ans.setText(getString(R.string.f00000_ans)+"\n"+
//                        getString(R.string.f00000_t001)+
//                        edtUserName.getText().toString()+"\n"+
//                                getString(R.string.f00000_t002)+
//                                edtPassword.getText().toString()
//                        );
                    mSignUpDlg.cancel();
                    break;
                case R.id.f00002_btnCancel:
                    mSignUpDlg.cancel();
                    break;
            }
        }
    };
    private F000 mGoogleSignInClient;

    //-------------------加密------------------------------------------------------------------------------------------
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f00000);
        setupView();
        u_loaddata();//傳flag進來 1是登入 0是登出

    }

    public void setupView() {

        this.setTitle("食在好便宜");

//        onPrepareOptionsMenu(menu);

    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != LAUNCH_GAME) {
            return;
        }
        switch (resultCode) {
            case RESULT_OK:
                Bundle bundle = data.getExtras();

                int flag1 = bundle.getInt("flag");

                loginon = flag1;
                u_menu_main();
                break;
            case RESULT_CANCELED:

                break;
        }
    }

    public void u_loaddata() {
        SharedPreferences login =
                getSharedPreferences("GAME_RESULT", 0);

        loginon = login.getInt("flag", 0);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    private void u_bundle() {
        Intent it = new Intent();
        bundle = new Bundle();
        bundle.putString("Email", String.valueOf(Email));
        bundle.putString("Password", String.valueOf(Password));
        it.putExtras(bundle);


        setResult(RESULT_OK, it);
        this.finish();
    }

    private void cancel() {

        setResult(RESULT_CANCELED);
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode != LAUNCH_GAME) {
//            return;
//        }
//        ;
//        switch (resultCode) {
//            case RESULT_OK:
//                Bundle bundle = data.getExtras();
//
//                String iCountPlayerWin = bundle.getString("Email");
//                String iCountComWin = bundle.getString("Password");
//            case RESULT_CANCELED:
//                Toast.makeText(this, "你選擇取消", Toast.LENGTH_SHORT);
//                break;
//        }
//    }

    @Override
    public void onClick(View v) {


    }

    //---------登入登出的item隱藏方法----------------------------
    public void u_menu_main() {
        about = menu.findItem(R.id.menuItemAbout);
        fans = menu.findItem(R.id.fans);
        end = menu.findItem(R.id.action_settings);

        about.setVisible(true);
        fans.setVisible(true);
        end.setVisible(true);
        if (loginon == 0) {//0時--------登出系統後
            menu.setGroupVisible(R.id.m_g01, true);
            menu.setGroupVisible(R.id.m_g02, false);
        } else {//0以外時----------登入系統後
            menu.setGroupVisible(R.id.m_g01, false);
            menu.setGroupVisible(R.id.m_g02, true);
        }
    }
//    private void signOut() {
//        mGoogleSignInClient.signOut()
//                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        //--START_EXCLUDE--
//                        updateUI(null);
//                        // [END_EXCLUDE]
////                        img.setImageResource(R.drawable.googleg_color); //還原圖示
//                    }
//                });
//
//    }

    //---------登入及註冊的dialog----------------------------------------------------------------
    private void u_loginDialog() {

        mLoginDlg = new Dialog(F000.this);
        mLoginDlg.setTitle(getString(R.string.f00001_title));
        mLoginDlg.setCancelable(false);

        mLoginDlg.setContentView(R.layout.login_dlg);

        loginBtnOK = (Button) mLoginDlg.findViewById(R.id.f00001_btnOK);
        loginBtnCancel = (Button) mLoginDlg.findViewById(R.id.f00001_btnCancel);

        edtUserName = (EditText) mLoginDlg.findViewById(R.id.f00001_e001);
        edtPassword = (EditText) mLoginDlg.findViewById(R.id.f00001_e002);
//        edtPassword.setVisibility(View.INVISIBLE);

        loginBtnCancel.setOnClickListener(on);
        loginBtnOK.setOnClickListener(on);
        mLoginDlg.show();

    }

    private void u_SignDialog() {
        mSignUpDlg = new Dialog(this);
        mSignUpDlg.setTitle(getString(R.string.f00002_title));
        mSignUpDlg.setCancelable(false);

        mSignUpDlg.setContentView(R.layout.signup_dlg);

        Button loginBtn2OK = (Button) mSignUpDlg.findViewById(R.id.f00002_btnOK);
        Button loginBtn2Cancel = (Button) mSignUpDlg.findViewById(R.id.f00002_btnCancel);

        loginBtn2Cancel.setOnClickListener(on);
        loginBtn2OK.setOnClickListener(on);
        mSignUpDlg.show();

    }


    @Override
    public void onBackPressed() {
//        super.onBackPressed();
//        this.finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        u_loaddata();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        this.finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
//        this.finish();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        u_loaddata();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        u_loaddata();
//        this.finish();
    }

    //----------------------Menu的部分 複製到java最下方---------------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        this.menu = menu;
        u_menu_main();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
            case R.id.action_settings:
                this.finish();
                break;
            case R.id.Item1:
                Intent it = new Intent();
                it.setClass(F000.this, F00000.class);
                startActivityForResult(it, LAUNCH_GAME);
                Toast.makeText(getApplicationContext(), getString(R.string.f00000_i001), Toast.LENGTH_SHORT).show();
                return true;
//            case R.id.Item2:
//                Toast.makeText(getApplicationContext(), getString(R.string.f00000_i002), Toast.LENGTH_SHORT).show();
//                u_SignDialog();
////                edtPassword.setVisibility(View.GONE);
////                t002.setVisibility(View.GONE);
//                break;
//            case R.id.Item3:
//                Toast.makeText(getApplicationContext(), getString(R.string.f00000_i003), Toast.LENGTH_SHORT).show();
//                break;
            case R.id.Item4:
                Intent intent = new Intent();
                intent.setClass(F000.this, F10700.class);
                startActivityForResult(intent, LAUNCH_GAME);
                Toast.makeText(getApplicationContext(), getString(R.string.f00000_i004), Toast.LENGTH_SHORT).show();

                break;
            case R.id.Item5:
                Toast.makeText(getApplicationContext(), getString(R.string.f00000_i005), Toast.LENGTH_SHORT).show();
//                Intent it = new Intent();
//                it.putExtra("flag",true);
//                setResult(RESULT_OK, it); //0
//
//                startActivityForResult(intent,LAUNCH_GAME);
                new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.logout))
                        .setMessage(getString(R.string.sure))
                        .setCancelable(true)
                        .setIcon(R.drawable.logo)
                        .setPositiveButton(getString(R.string.out),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        loginon = 0;//帳密正確 進入登入

                                        SharedPreferences login =
                                                getSharedPreferences("GAME_RESULT", 0);
                                        login
                                                .edit()
                                                .putInt("flag", loginon)
                                                .commit();
                                        u_menu_main();
//                                        signOut();
                                    }
                                })
                        .setNeutralButton(getString(R.string.cancel),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        cancel();
                                    }
                                })
                        .show();
                return true;
            case R.id.menuItemAbout:
                new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.title))
                        .setMessage(getString(R.string.maker))
                        .setCancelable(false)
                        .setIcon(R.drawable.logo)
                        .setPositiveButton(getString(R.string.tel),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
//                                        Uri uri = Uri.parse("tel:xxxx-xxx-xxx");
//                                        Intent it = new Intent(Intent.ACTION_DIAL, uri);
//                                        startActivity(it);
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
            case R.id.fans:
                it = new Intent();
                it.setClass(this, F10600.class);
                startActivity(it);
                break;
            case R.id.f10501_main:
                it = new Intent(this, F000.class);
                it.setClass(this, F10502.class);
                startActivity(it);
                this.finish();

                break;
        }
        return true;
    }

}
