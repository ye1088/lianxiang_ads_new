package com.google.lianxiang_ads_new;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.littleDog.LittleDog;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MAINACTIVITY : ";

    final String  INTER_ID = "1703290819937.app.lnj0uno5jg";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LittleDog.onCreate(MainActivity.this);


    }

    @Override
    protected void onResume() {
        super.onResume();
        LittleDog.onResume(this);
    }


    @Override
    protected void onPause() {
        super.onPause();
        LittleDog.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


    public void showLog(String log_str){
        Log.e(TAG,log_str);
    }

}
