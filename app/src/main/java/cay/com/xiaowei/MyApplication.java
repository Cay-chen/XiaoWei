package cay.com.xiaowei;

import android.app.Application;

import com.tencent.bugly.crashreport.CrashReport;
import com.youzan.sdk.YouzanSDK;

/**
 * Created by C on 2016/8/3.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CrashReport.initCrashReport(getApplicationContext(), "900046062", true);

        initYouzanSDK();
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
