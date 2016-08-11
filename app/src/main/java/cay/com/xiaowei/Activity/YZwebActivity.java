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

import com.youzan.sdk.web.plugin.YouzanBrowser;
import com.youzan.sdk.web.plugin.YouzanWebClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import cay.com.xiaowei.MyApplication;
import cay.com.xiaowei.R;
import cay.com.xiaowei.Util.OkhttpXiao;

/**
 * Created by C on 2016/8/11.
 */
public class YZwebActivity extends AppCompatActivity {
    private YouzanBrowser mVYouzanBrowser;
    private String USER_URL;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_yzwebviwe);
        mVYouzanBrowser = (YouzanBrowser) findViewById(R.id.youzan_UrlwebView);
       /* Intent intent = getIntent();
        Bundle bundle = intent.getExtras();*/


        webUrlLoad();

    }

    private void webUrlLoad() {


        try {
            USER_URL = MyApplication.URL + "?key=" + MyApplication.API_KEY_SHOP + "&info="
                    + URLEncoder.encode("小微购物车_URL", "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        new OkhttpXiao(USER_URL, shoppingCard);
    }

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
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
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
