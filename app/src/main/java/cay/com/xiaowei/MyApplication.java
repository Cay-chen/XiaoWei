package cay.com.xiaowei;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.tencent.bugly.crashreport.CrashReport;
import com.youzan.sdk.YouzanSDK;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by C on 2016/8/3.
 */
public class MyApplication extends Application {

    public static final String URL = "http://www.tuling123.com/openapi/api";  //图灵机器人请求地址
    public static final String API_KEY = "d5417d825f704b0aad7279f5e07963f4";//图灵账号API KEY
    private String USER_URL;
    private String name;
    private String pwd;
    private SharedPreferences sp;
    private static final String TAG = "ME1";


    @Override
    public void onCreate() {
        super.onCreate();
        CrashReport.initCrashReport(getApplicationContext(), "900046062", true);

        initYouzanSDK();
        initSp();

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

    /**
     * 初始化SP和密码本地提取
     * 如果第二次登陆就免密码登陆
     */
    private void initSp() {


        //初始化sp
        // 使用sharPerferences去保存数据
        //name 会自动生成一个XML文件  mode 自己选择模式
        sp = getSharedPreferences("password", 0);
        //把SharedPreferences数据调出来
        name = sp.getString("name", "");
        pwd = sp.getString("pwd", "");
        try {
            USER_URL = MyApplication.URL + "?key=" + MyApplication.API_KEY + "&info="
                    + URLEncoder.encode(name, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        OkHttpClient ueerOkHttpClient = new OkHttpClient();
        Request userRequest = new Request.Builder()
                .url(USER_URL)
                .build();
        Call ueerCall = ueerOkHttpClient.newCall(userRequest);
        ueerCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String fPasswordJson = response.body().string();
                try {
                    JSONObject passwordJsonObject = new JSONObject(fPasswordJson);
                    JSONObject fPasswordObject = passwordJsonObject.getJSONObject("text");

                    String fPassword = fPasswordObject.getString("password");
                    Log.i(TAG, "fPassword: " + fPassword);
                    if (pwd.equals(fPassword)) {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getApplicationContext().startActivity(intent);

                    } else {
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getApplicationContext().startActivity(intent);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        //把name和pwd显示到 edittext


    }
}
