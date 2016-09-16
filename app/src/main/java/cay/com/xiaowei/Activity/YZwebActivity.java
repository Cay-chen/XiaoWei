package cay.com.xiaowei.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.webkit.WebView;
import android.widget.TextView;

import com.youzan.sdk.web.plugin.YouzanBrowser;
import com.youzan.sdk.web.plugin.YouzanWebClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import cay.com.xiaowei.MyApplication;
import cay.com.xiaowei.R;
import cay.com.xiaowei.Util.OkhttpXiao;
import de.greenrobot.event.EventBus;

/**
 * Created by C on 2016/8/11.
 */
public class YZwebActivity extends AppCompatActivity {
    private YouzanBrowser mVYouzanBrowser;
    private String USER_URL;
    private String LIST_ALL_URL;
    private String LIST_DAIFUKUAN_URL;
    private String LIST_DAIFAHUO_URL;
    private String LIST_DAISHOUHUO_URL;
    private String LIST_YIWANCHENG_URL;

    private String isIF;
    private TextView titleTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_yzweb_viwe);
        mVYouzanBrowser = (YouzanBrowser) findViewById(R.id.youzan_UrlwebView);
        titleTextView = (TextView) findViewById(R.id.tv_title);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        isIF = bundle.getString("YZwebURL");


        webUrlLoad();


    }

    private void webUrlLoad() {

        mVYouzanBrowser.setWebViewClient(new YouzanWebClient() {


            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);

            }
        });


        try {
            USER_URL = MyApplication.URL + "?key=" + MyApplication.API_KEY_SHOP + "&info="
                    + URLEncoder.encode("小微购物车_URL", "UTF-8");
            LIST_ALL_URL = MyApplication.URL + "?key=" + MyApplication.API_KEY_SHOP + "&info="
                    + URLEncoder.encode("小微全部订单_URL", "UTF-8");
            LIST_DAIFUKUAN_URL = MyApplication.URL + "?key=" + MyApplication.API_KEY_SHOP + "&info="
                    + URLEncoder.encode("小微待付款订单_URL", "UTF-8");
            LIST_DAIFAHUO_URL = MyApplication.URL + "?key=" + MyApplication.API_KEY_SHOP + "&info="
                    + URLEncoder.encode("小微待发货订单_URL", "UTF-8");
            LIST_DAISHOUHUO_URL = MyApplication.URL + "?key=" + MyApplication.API_KEY_SHOP + "&info="
                    + URLEncoder.encode("小微待收货订单_URL", "UTF-8");
            LIST_YIWANCHENG_URL = MyApplication.URL + "?key=" + MyApplication.API_KEY_SHOP + "&info="
                    + URLEncoder.encode("小微已完成订单_URL", "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        switch (isIF) {
            case "ALL_LIST":
                titleTextView.setText("全部订单");
                new OkhttpXiao(LIST_ALL_URL, allListHandler);
                break;
            case "SHOPPING_CARD":
                titleTextView.setText("购物车");
                new OkhttpXiao(USER_URL, shoppingCard);
                break;
            case "DAI_FUKUAN":
                titleTextView.setText("待付款");
                new OkhttpXiao(LIST_DAIFUKUAN_URL, daifukuaiHandler);
                break;
            case "DAI_FAHUO":
                titleTextView.setText("待发货");
                new OkhttpXiao(LIST_DAIFAHUO_URL, daifahuoHandler);
                break;
            case "DAI_SHOUHUO":
                titleTextView.setText("待收货");
                new OkhttpXiao(LIST_DAISHOUHUO_URL, daishouhuoHandler);
                break;
            case "DAI_YIWANCHENG":
                titleTextView.setText("已完成");
                new OkhttpXiao(LIST_YIWANCHENG_URL, yiwanchengHandler);
                break;
        }
    }

    private Handler allListHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                JSONObject shopJsonObject = new JSONObject(msg.obj.toString());
                mVYouzanBrowser.loadUrl(shopJsonObject.getString("text"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mVYouzanBrowser.setWebViewClient(new YouzanWebClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    String anc = "https://wap.koudaitong.com/v2/showcase/homepage?kdt_id=3749326";
                    String quern = url.substring(0, 62);
                    if (anc.equals(quern)) {
                        EventBus.getDefault().post("BACK_HOME");
                        getActivity().finish();
                        return false;


                    } else {
                        return super.shouldOverrideUrlLoading(view, url);
                    }


                }
            });
        }
    };

    private Handler shoppingCard = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                JSONObject shopJsonObject = new JSONObject(msg.obj.toString());
                mVYouzanBrowser.loadUrl(shopJsonObject.getString("text"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            mVYouzanBrowser.setWebViewClient(new YouzanWebClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    String anc = "https://wap.koudaitong.com/v2/showcase/homepage?kdt_id=3749326";
                    String quern = url.substring(0, 62);
                    if (anc.equals(quern)) {
                        EventBus.getDefault().post("BACK_HOME");
                        getActivity().finish();
                        return false;


                    } else {
                        return super.shouldOverrideUrlLoading(view, url);
                    }


                }
            });


        }
    };
    private Handler daifukuaiHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                JSONObject shopJsonObject = new JSONObject(msg.obj.toString());
                mVYouzanBrowser.loadUrl(shopJsonObject.getString("text"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            mVYouzanBrowser.setWebViewClient(new YouzanWebClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    String anc = "https://wap.koudaitong.com/v2/showcase/homepage?kdt_id=3749326";
                    String quern = url.substring(0, 62);
                    if (anc.equals(quern)) {
                        EventBus.getDefault().post("BACK_HOME");
                        getActivity().finish();
                        return false;


                    } else {
                        return super.shouldOverrideUrlLoading(view, url);
                    }


                }
            });


        }
    };
    private Handler daifahuoHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                JSONObject shopJsonObject = new JSONObject(msg.obj.toString());
                mVYouzanBrowser.loadUrl(shopJsonObject.getString("text"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            mVYouzanBrowser.setWebViewClient(new YouzanWebClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    String anc = "https://wap.koudaitong.com/v2/showcase/homepage?kdt_id=3749326";
                    String quern = url.substring(0, 62);
                    if (anc.equals(quern)) {
                        EventBus.getDefault().post("BACK_HOME");
                        getActivity().finish();
                        return false;


                    } else {
                        return super.shouldOverrideUrlLoading(view, url);
                    }


                }
            });


        }
    };
    private Handler daishouhuoHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                JSONObject shopJsonObject = new JSONObject(msg.obj.toString());
                mVYouzanBrowser.loadUrl(shopJsonObject.getString("text"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            mVYouzanBrowser.setWebViewClient(new YouzanWebClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    String anc = "https://wap.koudaitong.com/v2/showcase/homepage?kdt_id=3749326";
                    String quern = url.substring(0, 62);
                    if (anc.equals(quern)) {
                        EventBus.getDefault().post("BACK_HOME");
                        getActivity().finish();
                        return false;


                    } else {
                        return super.shouldOverrideUrlLoading(view, url);
                    }


                }
            });


        }
    };
    private Handler yiwanchengHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                JSONObject shopJsonObject = new JSONObject(msg.obj.toString());
                mVYouzanBrowser.loadUrl(shopJsonObject.getString("text"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            mVYouzanBrowser.setWebViewClient(new YouzanWebClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    String anc = "https://wap.koudaitong.com/v2/showcase/homepage?kdt_id=3749326";
                    String quern = url.substring(0, 62);
                    if (anc.equals(quern)) {
                        EventBus.getDefault().post("BACK_HOME");
                        getActivity().finish();
                        return false;


                    } else {
                        return super.shouldOverrideUrlLoading(view, url);
                    }


                }
            });


        }
    };

    /**
     * 页面回退
     * bridge.pageGoBack()返回True表示处理的是网页的回退
     */
    @Override
    public void onBackPressed() {
        if (!mVYouzanBrowser.pageGoBack()) {
            super.onBackPressed();
        }

    }


}
