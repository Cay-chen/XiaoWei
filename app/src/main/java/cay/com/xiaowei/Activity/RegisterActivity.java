package cay.com.xiaowei.Activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;

import cay.com.xiaowei.Bean.Person;
import cay.com.xiaowei.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Cay on 2016/9/14.
 */
public class RegisterActivity extends AppCompatActivity {
    private final static String url = "http://118.192.157.178:8080/XiaoWei/servlet/Ycode";
    private final static String urlLogin = "http://118.192.157.178:8080/XiaoWei/servlet/SingUp";

    private EditText eMail;//邮箱账号输入
    private EditText ePassword;//密码输入
    private EditText erPpasswprd;//二次密码输入
    private TextView eTextView;//返回登陆
    private EditText eNikeName;//昵称输入
    private Button reButton;//注册按钮
    private String mail = null;//获得邮箱
    private String pwd = null;//获得密码
    private String rpwd = null;//再次获得密码
    private String nikeName = null;//获得昵称
    private TextView tvMail;//发送显示邮箱号
    private LinearLayout llLogin;//注册界面
    private LinearLayout llCode;//验证码界面
    private LinearLayout llScueess;//注册成功界面
    private Person person;
    private Button mLoginNextButton;//验证码 按钮
    private EditText mCode;//验证码输入
    private String yCode;//获取到的验证码
    private Button btnBackLogin;//注册成功返回登陆
    //发送注册Handler
    private Handler codeHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String resuloy = msg.obj.toString();
            Gson gson = new Gson();
            person = gson.fromJson(resuloy, Person.class);
            if (person.resCode.equals("30001")) {
                llLogin.setVisibility(View.GONE);
                llCode.setVisibility(View.GONE);
                llScueess.setVisibility(View.VISIBLE);

            } else if (person.resCode.equals("30002")) {
                Toast.makeText(RegisterActivity.this, "验证码错误", Toast.LENGTH_LONG).show();
            } else if (person.resCode.equals("30003")) {
                Toast.makeText(RegisterActivity.this, "验证码过期", Toast.LENGTH_LONG).show();
            } else if (person.resCode.equals("30004")) {
                Toast.makeText(RegisterActivity.this, "账号已注册成功", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(RegisterActivity.this, "系统异常", Toast.LENGTH_LONG).show();
            }
        }

    };


    //发送验证码Handler
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String resuloy = msg.obj.toString();
            Gson gson = new Gson();
            person = gson.fromJson(resuloy, Person.class);
            if (person.resCode.equals("10001")) {
                llLogin.setVisibility(View.GONE);
                llCode.setVisibility(View.VISIBLE);
                tvMail.setText(mail);

            } else if (person.resCode.equals("10002")) {
                Toast.makeText(RegisterActivity.this, "该账号已经被注册", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(RegisterActivity.this, "系统异常", Toast.LENGTH_LONG).show();
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initViews();
        reButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mail = eMail.getText().toString();
                pwd = ePassword.getText().toString();
                rpwd = erPpasswprd.getText().toString();
                nikeName = eNikeName.getText().toString();
                Log.i("TAG", "onClick: " + mail);
                if (!(mail.equals("")) && !(pwd.equals("")) && !(rpwd.equals("")) && !(nikeName.equals(""))) {
                    if (pwd.equals(rpwd)) {
                        OkHttpClient loginHttpClient = new OkHttpClient();
                        RequestBody requestBody = new FormBody.Builder().add("mail", mail).build();
                        Request request = new Request.Builder().url(url).post(requestBody).build();
                        Call call = loginHttpClient.newCall(request);
                        call.enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {

                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String result = response.body().string();
                                Message message = new Message();
                                message.obj = result;
                                handler.sendMessage(message);

                            }
                        });
                    } else {

                        Toast.makeText(RegisterActivity.this, "两次密码不一样", Toast.LENGTH_LONG).show();

                    }
                } else {
                    if (mail.equals("")) {
                        Toast.makeText(RegisterActivity.this, "请输入邮箱", Toast.LENGTH_LONG).show();

                    } else {
                        if (nikeName.equals("")) {
                            Toast.makeText(RegisterActivity.this, "请输入昵称", Toast.LENGTH_LONG).show();

                        } else {
                            if (pwd.equals("")) {
                                Toast.makeText(RegisterActivity.this, "请输入密码", Toast.LENGTH_LONG).show();

                            } else {
                                if (rpwd.equals("")) {
                                    Toast.makeText(RegisterActivity.this, "请再次输入密码", Toast.LENGTH_LONG).show();

                                }
                            }
                        }
                    }


                }


            }
        });
        eTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mLoginNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Register(urlLogin);
            }
        });
        btnBackLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });
    }

    /**
     * 初始化所有的按钮
     */
    private void initViews() {
        eMail = (EditText) findViewById(R.id.register_mail);
        ePassword = (EditText) findViewById(R.id.register_pwd);
        erPpasswprd = (EditText) findViewById(R.id.register_rpwd);
        reButton = (Button) findViewById(R.id.btn_register);
        eNikeName = (EditText) findViewById(R.id.register_nikeName);
        eTextView = (TextView) findViewById(R.id.tvLogin);
        llCode = (LinearLayout) findViewById(R.id.ll_code);
        llLogin = (LinearLayout) findViewById(R.id.ll_login);
        llScueess = (LinearLayout) findViewById(R.id.ll_login_success);
        tvMail = (TextView) findViewById(R.id.tv_mail);
        mLoginNextButton = (Button) findViewById(R.id.btn_loginNext);
        mCode = (EditText) findViewById(R.id.code);
        btnBackLogin = (Button) findViewById(R.id.btn_fLogin);
    }

    private void Register(String url) {
        yCode = mCode.getText().toString();
        if (!(yCode.equals(""))) {
            OkHttpClient httpClient = new OkHttpClient();
            RequestBody requestBody = new FormBody.Builder().add("mail", mail).add("password", pwd).add("nikeName", nikeName).add("yCode", yCode).build();
            Request request = new Request.Builder().url(url).post(requestBody).build();
            Call call = httpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String result = response.body().string();
                    Message message = new Message();
                    message.obj = result;
                    codeHandler.sendMessage(message);
                }
            });
        } else {
            Toast.makeText(RegisterActivity.this, "请输入验证码", Toast.LENGTH_LONG).show();
        }


    }


}
