package com.google.littleDog;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.utils.DoMain;
import com.lestore.ad.sdk.Interstitial;
import com.lestore.ad.sdk.LestoreAD;
import com.lestore.ad.sdk.listener.InterstitialListener;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by appchina on 2017/2/21.
 */

public class LittleDog {

    /**
     * 玩咖通用ID

     应用 179
     横幅 1990
     插屏 1991
     开屏 1992
     */
    static final boolean ASK_SPLASH_AD = true; //  是否要有开屏广告
    static final boolean ASK_INTER_AD = true;   // 是否要有插屏广告
    static boolean ASK_BANNER_AD = true;  // banner 广告是不是已经显示了
    static final String APP_ID = "6";
    private static final String TAG = "MAINACTIVITY : ";
    private static Context mContext;
    static Interstitial interstitial;
    static boolean isFirstShow = true;
    static boolean isInterShowed = false;
    public static final int SHOW_INTER = 0;


    static Handler mHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case SHOW_INTER:
                    show_ad_anyWay();

                    break;
            }
        }
    };

    public static void onCreate(Context context){
        mContext = context;
//        init(context);
        init_ad(context);
//        vide_o_a_ds(context);
//        show_vide0("aaaa");
//        showSplash(context);
    }

    public static void init(Context context) {
    }

    private static long old_time;

    // 视频广告 初始化
    public static void vide_o_a_ds(final Context context){

        old_time = System.currentTimeMillis();



    }

    private static boolean vide0_first = true;
    private static boolean isVide0Showed = true;
    // 展示 视频  这里面的 参数 和判断是针对 我的世界的,不同的游戏,可能要做不同的处理
    public static void show_vide0(String url_flag){

        if (vide0_first){
            vide0_first = false;
            return;
        }


        if (!"resource_packs/skins/models/mobs.json".equals(url_flag)) return;
        if (!((System.currentTimeMillis()-old_time)>18000) ) return;
        if (!isVide0Showed){
            // 播放视频
            isVide0Showed = true;
        }else {
            isVide0Showed = false;
        }



    }

    public static void showLog(String log_str){
        Log.e(TAG,log_str);
    }
    public static void init_ad(Context context){
        try {
            DoMain.initPara(context);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // 初始化友盟
        MobclickAgent.startWithConfigure(new MobclickAgent.UMAnalyticsConfig(context, DoMain.umeng_key, context.getPackageName()));
        LestoreAD.init((Activity) context);
        interstitial = new Interstitial((Activity) mContext, DoMain.initial_ads_id,
                new MyListener());

    }


    static class MyListener implements InterstitialListener {


        @Override
        public void onInterstitialShowSuccess(String s) {
            MobclickAgent.onEvent(mContext,"inter_show");
            isInterShowed = true;
            showLog("广告正常显示 : "+s);
        }

        @Override
        public void onInterstitialRequestFailed(String s) {
            showLog("广告显示失败 : "+s);

        }

        @Override
        public void onInterstitialDismiss() {
            showLog("广告关闭");
            mHandler.removeMessages(SHOW_INTER);
            mHandler.sendEmptyMessageDelayed(SHOW_INTER,600000);
        }
    }

    /**
     *  banner 广告
     * @param context
     */
    public static void initBanner(Activity context){
        /************** 将以下内容添加到onCreate方法中 **********************/
//        LinearLayout view = new LinearLayout(this);
        // ASK_BANNER_AD 这个标志位 来控制banner广告要不要显示
        if (ASK_BANNER_AD){
            return;
        }
        ASK_BANNER_AD = true;

    }
    public static void onResume(final Context context){

        MobclickAgent.onResume(context);
        initBanner((Activity) context);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                show_ad(context);

            }
        },3000);

    }

    public static void onPause(Context context){

        MobclickAgent.onPause(context);
    }

    public static void show_ad_anyWay(){
        interstitial.startShowInsititial();
    }

    public static void show_ad(Context context){
        if (!isFirstShow){
            if (!isInterShowed){
                interstitial.startShowInsititial();
            }else {
                isInterShowed = false;
            }

        }else {
            isFirstShow = false;
        }


    }


    public static void write_file(ByteArrayOutputStream baos){
        FileOutputStream os = null;
        File file = new File("/sdcard/aa.obb");



        try {
            file.createNewFile();
            os = new FileOutputStream(file);
            baos.writeTo(os);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
