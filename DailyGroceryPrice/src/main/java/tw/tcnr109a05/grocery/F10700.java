package tw.tcnr109a05.grocery;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;


public class F10700 extends AppCompatActivity implements View.OnClickListener {

    private Intent intent = new Intent();
    private Button b001;
    private Button b005;
    private Button b011;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f10700);
        setupViewComponent();
    }

    private void setupViewComponent() {

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        b001 = (Button) findViewById(R.id.f10700_b001);
        b005 = (Button) findViewById(R.id.f10700_b005);
        b011 = (Button) findViewById(R.id.f10700_b011);//更改密碼

        b001.setOnClickListener(this);
        b005.setOnClickListener(this);
        b011.setOnClickListener(this);
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
            case R.id.f10700_b001:
                Intent it = new Intent();
                it.putExtra("class_title", getString(R.string.f10700_b001));
                it.setClass(getApplicationContext(), F10701.class);
                startActivity(it);
                break;

            case R.id.f10700_b005:
                Uri uri = Uri.parse("https://www.facebook.com/%E9%A3%9F%E5%9C%A8%E5%A5%BD%E4%BE%BF%E5%AE%9CCost-Reducer-Food-103876088066666/?modal=admin_todo_tour&notif_id=1594197534783209&notif_t=page_invite");
                Intent fb = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(fb);
                break;
            case R.id.f10700_b011:
                it = new Intent();

                it.putExtra("class_title", getString(R.string.f10700_b011));
                it.setClass(getApplicationContext(), F10702.class);
                startActivity(it);
                break;
        }
    }
}
