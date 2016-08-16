package cay.com.xiaowei;

import android.app.Application;

import com.tencent.bugly.crashreport.CrashReport;
import com.youzan.sdk.YouzanSDK;

/**
 * Created by C on 2016/8/3.
 */
public class MyApplication extends Application {

    public static final String URL = "http://www.tuling123.com/openapi/api";  //图灵机器人请求地址
    public static final String API_KEY_ON = "d5417d825f704b0aad7279f5e07963f4";//图灵账号API KEY
    public static final String API_KEY_XIAOWEI = "be19bed1d5a44286bad4ab24c4910cba";//图灵账号小微其他请求
    public static final String CZ_KEY = "e2b55f4b2a335d3e8627ff226d7cd9d5";//聚合手机充值KEY
    public static final String API_KEY_SHOP = "4b3ccfe5f01a4adda5d02733b06718da";//图灵购物车等





    @Override
    public void onCreate() {
        super.onCreate();

        initYouzanSDK();
        //initSp();
        CrashReport.initCrashReport(this, "900046062", false);

    }

    private void initYouzanSDK() {
        /**
         * 初始化SDK
         *
         * @param Context application Context
         * @param userAgent 用户代理 UA, 调试可以使用"demo"这个UA
         */
        YouzanSDK.init(this, "demo");
    }


}
