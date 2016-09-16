package cay.com.xiaowei.Activity;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.youzan.sdk.YouzanSDK;
import com.youzan.sdk.YouzanUser;
import com.youzan.sdk.http.engine.OnRegister;
import com.youzan.sdk.http.engine.QueryError;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import cay.com.xiaowei.Bean.Person;
import cay.com.xiaowei.MyApplication;
import cay.com.xiaowei.R;
import cay.com.xiaowei.Util.AllDatas;
import cay.com.xiaowei.Util.OkhttpXiao;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;
import okhttp3.Call;


/**
 * 登录界面
 *
 * @author Administrator
 */
public class LoginActivity extends Activity {
  private static final String TAG = "ME1";
    private Button btnLogin;
    private EditText et_name;
    private EditText et_password;
    private CheckBox cb_ischeck;
    private SharedPreferences sp;
    private String meLoginPut;
    private String mUserId;
    private String HUO_USER;
    private String mGender;
    private String mNickName;
    private String mUserName;
    private String mTelphone;
    private ImageButton weixinImageButton;
    private TextView mTextView;
    private String pwd;
    private String name;


    private Handler weiXinUserHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.i(TAG, "msg: " + msg.obj.toString());
            try {
                JSONObject weixinJsonObject = new JSONObject(msg.obj.toString());
                String nickname = weixinJsonObject.getString("nickname");
                String unionid = weixinJsonObject.getString("unionid");
                String sex = weixinJsonObject.getString("sex");
                String headimgurl = weixinJsonObject.getString("headimgurl");
                final String mInput = "{\"VIP\":\"" + "三" + "\",\"UserId\":\"" + unionid + "\",\"Gender\":\"" + sex + "\",\"NikeName\":\"" + nickname + "\",\"UserName\":\"" + "小二" + "\",\"Telphone\":\"" + "13568882973" + "\",\"headurl\":\"" + headimgurl + "\"}";
                Log.i(TAG, "mInput: " + mInput);
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
                user.setUserId(unionid);//用户唯一性ID, 你可以使用用户的ID等表示
                user.setGender(Integer.parseInt(sex));// "1"表示男性, "0"表示女性
                user.setNickName(nickname);//昵称, 会显示在有赞商家版后台
                user.setTelephone(" ");//手机号
                user.setUserName(" ");//用户名

                YouzanSDK.asyncRegisterUser(user, new OnRegister() {
                    @Override
                    public void onFailed(QueryError error) {
                        Toast.makeText(LoginActivity.this, error.getMsg(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess() {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("USER_FAN", mInput);
                        startActivity(intent);
                        finish();
                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 不显示标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 引入布局界面
        setContentView(R.layout.activity_login);

        EventBus.getDefault().register(this);//注册Eventbus
        initView();//初始化控件
        initSp();//进行密码本地提取和初始化SP
        initLogin();//进行登录逻辑判断
        weixinImageButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                weixinLogin();
            }
        });
        mTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);//反注册Eventvus
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onUserEvent(String event) {
        Log.i(TAG, "onUserEvent: " + event);
        try {
            JSONObject tokenJsonObject = new JSONObject(event);
            String access_token = tokenJsonObject.getString("access_token");
            String openid = tokenJsonObject.getString("openid");
            String expires_in = tokenJsonObject.getString("expires_in");
            String scope = tokenJsonObject.getString("scope");
            String unionid = tokenJsonObject.getString("unionid");
            String refresh_token = tokenJsonObject.getString("refresh_token");

            HUO_USER = "https://api.weixin.qq.com/sns/userinfo?access_token=" + access_token + "&openid=" + openid;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new OkhttpXiao(HUO_USER, weiXinUserHandler);
    }

    /**
     * 微信登录判定
     */
    private void weixinLogin() {
        if (MyApplication.mWeiXinApi == null) {
            MyApplication.mWeiXinApi = WXAPIFactory.createWXAPI(this, MyApplication.APP_ID, false);
        }

        if (!MyApplication.mWeiXinApi.isWXAppInstalled()) {
            //提醒用户没有按照微信
            Toast.makeText(this, "请安装微信后在登录！", Toast.LENGTH_LONG).show();
            return;
        }

        MyApplication.mWeiXinApi.registerApp(MyApplication.APP_ID);

        final SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "wechat_sdk_demo_test";
        MyApplication.mWeiXinApi.sendReq(req);

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
                name = et_name.getText().toString().trim();
                pwd = et_password.getText().toString().trim();
                // 判断账户和密码是否为空调
                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(pwd)) {
                    Toast.makeText(LoginActivity.this, "账号或密码不能为空", Toast.LENGTH_LONG).show();
                } else {
                    OkHttpUtils.post().url(AllDatas.SIGN_IN).addParams("username", name).addParams("password", pwd).build().execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            Toast.makeText(LoginActivity.this, "登录异常，请检查网络！", Toast.LENGTH_LONG).show();

                        }

                        @Override
                        public void onResponse(String response, int id) {
                            Person person = JSON.parseObject(response, Person.class);
                            switch (person.resCode) {
                                case "20001":
                                    MyApplication.getInstance().addParam("user", name);
                                    MyApplication.getInstance().addParam("password", pwd);
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
                                    OkHttpUtils.post().url(AllDatas.USER_ALL_MSG).addParams("username",name).addParams("password",pwd).build().execute(new StringCallback() {
                                        @Override
                                        public void onError(Call call, Exception e, int id) {
                                            Log.i(TAG, "onResponse:3333333 ");

                                        }

                                        @Override
                                        public void onResponse(String response, int id) {
                                            Person person = JSON.parseObject(response, Person.class);
                                            Log.i(TAG, "onResponse:222222222 "+person);
                                            mUserId = person.userId;
                                            mGender = person.gender;
                                            mNickName =person.nikeName;
                                            mUserName =person.userName;
                                            mTelphone = person.phone;
                                            registerYouzanUserForWeb();
                                        }
                                    });

                                    break;
                                case "20002":
                                    Toast.makeText(LoginActivity.this, "密码错误", Toast.LENGTH_LONG).show();
                                    break;
                                case "20003":
                                    Toast.makeText(LoginActivity.this, "无此账号", Toast.LENGTH_LONG).show();
                                    break;
                                case "20004":
                                    Toast.makeText(LoginActivity.this, "账号为空", Toast.LENGTH_LONG).show();
                                    break;
                                default:
                                    Toast.makeText(LoginActivity.this, "系统异常", Toast.LENGTH_LONG).show();
                            }

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
        weixinImageButton = (ImageButton) findViewById(R.id.ibt_weixin);
        mTextView = (TextView) findViewById(R.id.tvRegister);
    }

    private void registerYouzanUserForWeb() {



        Log.i(TAG, "onResponse:1111111 ");

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
                Log.i(TAG, "input: " + meLoginPut);
                intent.putExtra("USER_FAN", meLoginPut);
                startActivity(intent);
                finish();
            }
        });
    }

}
