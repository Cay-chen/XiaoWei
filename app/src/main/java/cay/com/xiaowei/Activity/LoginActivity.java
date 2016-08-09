package cay.com.xiaowei.Activity;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

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
 * 登录界面
 *
 * @author Administrator
 */
public class LoginActivity extends Activity {
    private static final String TAG = "ME1";
    private String USER_URL;
    private Button btnLogin;
    private EditText et_name;
    private EditText et_password;
    private CheckBox cb_ischeck;
    private SharedPreferences sp;
    private String input;
    private String IMAGE_URL;
    private String mUserId;
    private String mGender;
    private String mNickName;
    private String mUserName;
    private String mTelphone;

    private Handler userHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 不显示标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 引入布局界面
        setContentView(R.layout.activity_login);

        initView();//初始化控件
        initSp();//进行密码本地提取和初始化SP
        initLogin();//进行登录逻辑判断
    }


    /**
     * 初始化SP和密码本地提取
     */
    private void initSp() {
        //初始化sp
        // 使用sharPerferences去保存数据
        //name 会自动生成一个XML文件  mode 自己选择模式
        sp = getSharedPreferences("password", 0);
        //把SharedPreferences数据调出来
        String name = sp.getString("name", "");
        String pwd = sp.getString("pwd", "");
        Log.d(TAG, "name: " + name + "pwd:" + pwd);
        //把name和pwd显示到 edittext
        et_name.setText(name);
        et_password.setText(pwd);
        boolean result = sp.getBoolean("ischecked", false);
        if (result) {
            cb_ischeck.setChecked(true);

        }
    }

    /**
     * 进行登录逻辑判断
     */
    private void initLogin() {
        btnLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                // 获取用户名和密码
                final String name = et_name.getText().toString().trim();
                final String pwd = et_password.getText().toString().trim();
                // 判断账户和密码是否为空调
                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(pwd)) {
                    Toast.makeText(LoginActivity.this, "账号或密码不能为空", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        USER_URL = MyApplication.URL + "?key=" + MyApplication.API_KEY_ON + "&info="
                                + URLEncoder.encode(name, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    OkHttpClient userHttpClient = new OkHttpClient();
                    Request userRequest = new Request.Builder()
                            .url(USER_URL)
                            .build();
                    Call call = userHttpClient.newCall(userRequest);
                    call.enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Toast.makeText(LoginActivity.this, "登录异常，请检查网络！", Toast.LENGTH_LONG).show();

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            final String sussc = response.body().string();
                            userHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        JSONObject userJsonObject1 = new JSONObject(sussc);
                                        Log.i("TAG", "run: " + userJsonObject1.getString("text"));
                                        input = userJsonObject1.getString("text");
                                        String panduanhou = input.substring(0, 1);

                                        if (panduanhou.equals("{")) {
                                            JSONObject ueerJsonObject2 = userJsonObject1.getJSONObject("text");
                                            mUserId = ueerJsonObject2.getString("UserId");
                                            mGender = ueerJsonObject2.getString("Gender");
                                            mNickName = ueerJsonObject2.getString("NikeName");
                                            mUserName = ueerJsonObject2.getString("UserName");
                                            mTelphone = ueerJsonObject2.getString("Telphone");
                                            String getPassword = ueerJsonObject2.getString("password");
                                            if (getPassword.equals(pwd)) {
                                                if (cb_ischeck.isChecked()) {
                                                    // 获取SP的编辑器
                                                    Editor edit = sp.edit();
                                                    edit.putString("name", name);
                                                    edit.putString("pwd", pwd);
                                                    //存储checkbpox的状态
                                                    edit.putBoolean("ischecked", true);
                                                    // 记得edit的提交
                                                    edit.commit();

                                                } else {
                                                    Editor edit = sp.edit();
                                                    edit.putString("name", "");
                                                    edit.putString("pwd", "");
                                                    //存储checkbpox的状态
                                                    edit.putBoolean("ischecked", false);
                                                    // 记得edit的提交
                                                    edit.commit();


                                                }
                                                registerYouzanUserForWeb();
                                            } else {
                                                Toast.makeText(LoginActivity.this, "密码错误", Toast.LENGTH_LONG)
                                                        .show();
                                            }

                                        } else {
                                            Toast.makeText(LoginActivity.this, "账号错误", Toast.LENGTH_LONG)
                                                    .show();

                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            });
                        }
                    });


                }

            }
        });
    }

    /**
     * 初始化控件
     */
    private void initView() {
        btnLogin = (Button) findViewById(R.id.btn_Login);
        et_name = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_userpassword);
        cb_ischeck = (CheckBox) findViewById(R.id.cb_login_ischeck);
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
                Toast.makeText(LoginActivity.this, error.getMsg(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess() {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("USER_FAN", input);
                startActivity(intent);
                finish();
            }
        });
    }
}
