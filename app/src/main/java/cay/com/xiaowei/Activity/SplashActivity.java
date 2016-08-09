package cay.com.xiaowei.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.youzan.sdk.YouzanSDK;
import com.youzan.sdk.YouzanUser;
import com.youzan.sdk.http.engine.OnRegister;
import com.youzan.sdk.http.engine.QueryError;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import cay.com.xiaowei.MyApplication;
import cay.com.xiaowei.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Cay on 2016/8/9.
 */
public class SplashActivity extends AppCompatActivity {
    private ImageView mImageView;
    private String USER_URL;
    private String name;
    private String pwd;
    private long TIME = 3000;
    private SharedPreferences sp;
    private String input;
    private String IMAGE_URL;
    private  String mUserId;
    private  String mGender;
    private  String mNickName;
    private  String mUserName;
    private  String mTelphone;
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String url = msg.obj.toString();
            Glide.with(getApplicationContext()).load(url).into(mImageView);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        mImageView = (ImageView) findViewById(R.id.splash_image);
        initImage();//请求图片的网络地址
        setTime();//
    }

    /**
     * 设置Splash的定时
     */
    private void setTime() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initSp();
            }
        }, TIME);
    }

    /**
     * 请求图片的网络地址
     */
    private void initImage() {
        try {
            IMAGE_URL = MyApplication.URL + "?key=" + MyApplication.API_KEY_XIAOWEI + "&info="
                    + URLEncoder.encode("小微Splash_URL", "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        OkHttpClient imageHttpClient = new OkHttpClient();
        Request imageRequest = new Request.Builder()
                .url(IMAGE_URL)
                .build();
        Call imageCall = imageHttpClient.newCall(imageRequest);
        imageCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String imageUrl = response.body().string();
                try {
                    JSONObject imageJsonObject = new JSONObject(imageUrl);
                    String url = imageJsonObject.getString("text");
                    Message message = new Message();
                    message.obj = url;
                    handler.sendMessage(message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
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
        if (name.equals("")) {
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            try {
                USER_URL = MyApplication.URL + "?key=" + MyApplication.API_KEY_ON + "&info="
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
                        mUserId = fPasswordObject.getString("UserId");
                        mGender = fPasswordObject.getString("Gender");
                        mNickName = fPasswordObject.getString("NikeName");
                        mUserName = fPasswordObject.getString("UserName");
                        mTelphone = fPasswordObject.getString("Telphone");
                        input = passwordJsonObject.getString("text");
                        String fPassword = fPasswordObject.getString("password");
                        if (pwd.equals(fPassword)) {
                            registerYouzanUserForWeb();


                        } else {
                            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        }


    }

    private void registerYouzanUserForWeb() {
        /**
         * 演示 - 异步注册有赞用户(AsyncRegisterUser)
         *
         * 打开有赞入口网页需先注册有赞用户
         * <pre>
         * 如果你们App的用户这个时候还没有登录, 请先跳转你们的登录页面, 然后再回来同步用户信息
         *
         * 或者参考{@link LoginWebActivity}
         * </pre>
         */
        YouzanUser user = new YouzanUser();
        user.setUserId(mUserId);//用户唯一性ID, 你可以使用用户的ID等表示
        user.setGender(Integer.parseInt(mGender));// "1"表示男性, "0"表示女性
        user.setNickName(mNickName);//昵称, 会显示在有赞商家版后台
        user.setTelephone(mTelphone);//手机号
        user.setUserName(mUserName);//用户名

        YouzanSDK.asyncRegisterUser(user, new OnRegister() {
            @Override
            public void onFailed(QueryError error) {
                Toast.makeText(SplashActivity.this, error.getMsg(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                intent.putExtra("USER_FAN", input);
                startActivity(intent);
                finish();
            }
        });
    }
}
