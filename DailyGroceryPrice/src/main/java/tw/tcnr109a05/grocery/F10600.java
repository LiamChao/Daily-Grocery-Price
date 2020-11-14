package tw.tcnr109a05.grocery;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;


//--------------其他人看這支--------直接跳到onOptionsItemSelected-----------------
public class F10600 extends AppCompatActivity implements View.OnClickListener {

    private Intent intent = new Intent();
    private Menu menu;
    private Dialog mLoginDlg;
    private Dialog mSignUpDlg;
    private Button loginBtnOK, loginBtnCancel;
    private Button b001;
    private Button b002;
    private Uri uri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f10600);
        setupViewComponent();
    }


    private void setupViewComponent() {
        b001 = (Button) findViewById(R.id.f10600_b001);
        b002 = (Button) findViewById(R.id.f10600_b002);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        b001.setOnClickListener(this);
        b001.setOnClickListener(this);
    }

    //----------返回鍵的部分--------------------------------------------------------
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//-----------複製以下這段 改完這些就好----------------------------------------
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
            case R.id.f10600_b001:
                Uri uri = Uri.parse("https://www.facebook.com/%E9%A3%9F%E5%9C%A8%E5%A5%BD%E4%BE%BF%E5%AE%9CCost-Reducer-Food-103876088066666/?modal=admin_todo_tour&notif_id=1594197534783209&notif_t=page_invite");
                Intent it = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(it);
                break;
            case R.id.f10600_b002:
                Uri uri1 = Uri.parse("http://google.com");
                Intent It = new Intent(Intent.ACTION_VIEW, uri1);
                startActivity(It);
                break;

        }
    }

//-----------下面的東西先不用管-------------------------------------------------

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode!=LAUNCH_GAME){
//            return;
//        };
//        switch (resultCode){
//            case RESULT_OK:
//                int flag1 = intent.getExtras().getInt("flag");
//                flag =flag1;
//                u_menu_main();
//
//                break;
//            case RESULT_CANCELED:
//                break;
//        }
//    }
//
//    private Button.OnClickListener on = new Button.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            switch (v.getId()) {
//                case R.id.f00001_btnOK:
//                    EditText edtUserName = (EditText) mLoginDlg.findViewById(R.id.f00001_e001);
//                    EditText edtPassword = (EditText) mLoginDlg.findViewById(R.id.f00001_e002);
//
////                ans.setText(getString(R.string.f00000_ans)+"\n"+
////                        getString(R.string.f00000_t001)+
////                        edtUserName.getText().toString()+"\n"+
////                                getString(R.string.f00000_t002)+
////                                edtPassword.getText().toString()
////                        );
//                    mLoginDlg.cancel();
//                    u_menu_login();
////                    u_bundle();
////                    startActivityForResult(intent, LAUNCH_GAME);
//                    break;
//                case R.id.f00001_btnCancel:
//                    mLoginDlg.cancel();
//                    break;
//                case R.id.f00002_btnOK:
//                    EditText f02_e001 = (EditText) mSignUpDlg.findViewById(R.id.f00002_e001);
//                    EditText f02_e002 = (EditText) mSignUpDlg.findViewById(R.id.f00002_e002);
//
//
////                ans.setText(getString(R.string.f00000_ans)+"\n"+
////                        getString(R.string.f00000_t001)+
////                        edtUserName.getText().toString()+"\n"+
////                                getString(R.string.f00000_t002)+
////                                edtPassword.getText().toString()
////                        );
//                    mSignUpDlg.cancel();
//                    break;
//                case R.id.f00002_btnCancel:
//                    mSignUpDlg.cancel();
//                    break;
//            }
//        }
//    };
//    public static String u_md5(String content) {
//        byte[] hash;
//        try {
//            hash = MessageDigest.getInstance("MD5").digest(content.getBytes("UTF-8"));
//        } catch (NoSuchAlgorithmException e) {
//            throw new RuntimeException("NoSuchAlgorithmException", e);
//        } catch (UnsupportedEncodingException e) {
//            throw new RuntimeException("UnsupportedEncodingException", e);
//        }
//
//        StringBuilder hex = new StringBuilder(hash.length * 2);
//        for (byte b : hash) {
//            if ((b & 0xFF) < 0x10) {
//                hex.append("0");
//            }
//            hex.append(Integer.toHexString(b & 0xFF));
//        }
//        return hex.toString();
//    }
//
//
//    //---------登入登出的item隱藏方法----------------------------
//    private void u_menu_main() {
//        menu.setGroupVisible(R.id.m_g01, true);
//        menu.setGroupVisible(R.id.m_g02, false);
//    }
//
//    private void u_menu_login() {
//        menu.setGroupVisible(R.id.m_g01, false);
//        menu.setGroupVisible(R.id.m_g02, true);
//    }
//
//    //---------登入及註冊的dialog----------------------------------------------------------------
//    private void u_loginDialog() {
//
//        mLoginDlg = new Dialog(F10600.this);
//        mLoginDlg.setTitle(getString(R.string.f00001_title));
//        mLoginDlg.setCancelable(false);
//
//        mLoginDlg.setContentView(R.layout.login_dlg);
//
//        loginBtnOK = (Button) mLoginDlg.findViewById(R.id.f00001_btnOK);
//        loginBtnCancel = (Button) mLoginDlg.findViewById(R.id.f00001_btnCancel);
//
//
//        loginBtnCancel.setOnClickListener(on);
//        loginBtnOK.setOnClickListener(on);
//        mLoginDlg.show();
//
//    }
//
//    private void u_SignDialog() {
//        mSignUpDlg = new Dialog(this);
//        mSignUpDlg.setTitle(getString(R.string.f00002_title));
//        mSignUpDlg.setCancelable(false);
//
//        mSignUpDlg.setContentView(R.layout.signup_dlg);
//
//        Button loginBtn2OK = (Button) mSignUpDlg.findViewById(R.id.f00002_btnOK);
//        Button loginBtn2Cancel = (Button) mSignUpDlg.findViewById(R.id.f00002_btnCancel);
//
//        loginBtn2Cancel.setOnClickListener(on);
//        loginBtn2OK.setOnClickListener(on);
//        mSignUpDlg.show();
//
//    }
//
//    //----------------------Menu的部分 複製到java最下方---------------------------------------
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.main, menu);
//        this.menu = menu;
//        menu.setGroupVisible(R.id.m_g01, true);
//        menu.setGroupVisible(R.id.m_g02, false);
//        u_menu_main();
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                this.finish();
//                break;
//            case R.id.action_settings:
//                this.finish();
//                break;
//            case R.id.Item1:
//
//                u_loginDialog();
//                Toast.makeText(getApplicationContext(), getString(R.string.f00000_i001), Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.Item2:
//                Toast.makeText(getApplicationContext(), getString(R.string.f00000_i002), Toast.LENGTH_SHORT).show();
//                u_SignDialog();
////                edtPassword.setVisibility(View.GONE);
////                t002.setVisibility(View.GONE);
//                break;
//            case R.id.Item3:
//                Toast.makeText(getApplicationContext(), getString(R.string.f00000_i003), Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.Item4:
//                Toast.makeText(getApplicationContext(), getString(R.string.f00000_i004), Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.Item5:
//                Toast.makeText(getApplicationContext(), getString(R.string.f00000_i005), Toast.LENGTH_SHORT).show();
//                u_menu_main();
//                break;
//
//        }
//        return super.onOptionsItemSelected(item);
//    }

}
