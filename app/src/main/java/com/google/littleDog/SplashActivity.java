package com.google.littleDog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.lianxiang_ads_new.MainActivity;
import com.google.utils.DoMain;
import com.google.utils.SUtils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;

import java.io.IOException;

/**
 * Created by appchina on 2017/3/7.
 */

public class SplashActivity extends Activity {

    static final String ADPID = "1705100002";
    private static final boolean ASK_BANNER_AD = true;
    static final String UMENG_KEY = "5913c44f75ca35733500131d";
    private static boolean isAdClick = false;   // 广告是不是被点击了
    private static boolean isAdSkip = true; // 是否点广告跳过
    private static final String TAG = "SplashActivity";
    private static final boolean ISDEBUG = true;
    private boolean dataIsCopy = false;
    private boolean splashIsShow = false;


    static Handler handler;
    private boolean isIntented = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
        showSplash(this);

    }

    private void init() {

        try {
            DoMain.initPara(this);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        /**
         * 友盟 初始化
         * cGold : 是渠道号
         * 584912f375ca3528ff00056d : 是友盟 key
         */
        MobclickAgent.startWithConfigure(new MobclickAgent.UMAnalyticsConfig(this, DoMain.umeng_key, getPackageName()));
        if (ASK_BANNER_AD){
//            initBanner(this);
        }





        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {

                if (msg.what != 4){
                    splashIsShow = true;
                }
                switch (msg.what){
                    case 0:
                        gotoNextActivity("onSplashAdFailed");
                        break;
                    case 1:
                        gotoNextActivity("onSplashAdDismiss");
                        break;
                    case 2:
                        gotoNextActivity("isAdSkip ads");
                        break;
                    case 3:
                        gotoNextActivity("onResume");
                        break;
                    case 4:
                        gotoNextActivity("copyData");
                        break;
                    case 5:
                        gotoNextActivity("Copy Error");
                        break;
                    default:
                        splashIsShow = true;
                        gotoNextActivity("default");
                        break;
                }


            }
        };


        try {
            if (!SUtils.isFirstRun(this)||SUtils.isNewObbVersion(this)){
                splashIsShow = true;
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        try {
                            SUtils.copy_data(SplashActivity.this);
                            Message msg = handler.obtainMessage();
                            dataIsCopy = true;
                            msg.what = 4;
                            handler.sendMessage(msg);
                        } catch (Exception e) {
                            e.printStackTrace();
                            showLog("copy exception hehe");
                            dataIsCopy = true;
                            handler.sendEmptyMessage(5);
                        }
                    }
                }.start();
            }else {
                dataIsCopy = true;
                handler.sendEmptyMessage(2);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    /**
     * 开屏广告
     * @param context
     */
    public void showSplash(final Context context){

        }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);


    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);

    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    private  void gotoNextActivity(String msg) {
        showLog(msg);
        if (dataIsCopy){
            isIntented = true;
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }

    }



    public static void showLog(String msg){
        if (ISDEBUG){
            Log.e(TAG,msg);
        }
    }



}
