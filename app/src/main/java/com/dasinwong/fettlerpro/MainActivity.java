package com.dasinwong.fettlerpro;

import android.Manifest;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.dasinwong.fettler.Fettler;
import com.dasinwong.fettler.FixListener;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initPermission();
    }

    private void initPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, 1);
    }

    public void calculate(View view) {
        new CalculateUtils().calculate(this);
    }

    public void hotfix(View view) {
        File dexFile = new File(Environment.getExternalStorageDirectory(), "CalculateUtils.dex");
        Fettler.with(this).add(dexFile).listen(new FixListener() {
            @Override
            public void onComplete() {
                Toast.makeText(MainActivity.this, "修复完成", Toast.LENGTH_SHORT).show();
            }
        }).start();
    }
}
