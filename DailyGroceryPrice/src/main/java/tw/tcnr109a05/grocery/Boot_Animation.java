package tw.tcnr109a05.grocery;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public class Boot_Animation extends AppCompatActivity {
    private RelativeLayout boot_layout;
    private Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.boot_animation);
        boot_layout = (RelativeLayout) findViewById(R.id.boot_r01);
        boot_layout.setAnimation(AnimationUtils.loadAnimation(this, R.anim.anim_scale_rotate_in));
//        MediaPlayer startMusic = MediaPlayer.create(getApplication(), R.raw.pearls);
//        startMusic.start();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(Boot_Animation.this, F10200.class));
                finish();
            }
        }, 5000);
    }
}
