package cay.com.xiaowei.fragment;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.youzan.sdk.model.goods.GoodsShareModel;
import com.youzan.sdk.web.bridge.IBridgeEnv;
import com.youzan.sdk.web.event.ShareDataEvent;
import com.youzan.sdk.web.plugin.YouzanBrowser;
import com.youzan.sdk.web.plugin.YouzanChromeClient;
import com.youzan.sdk.web.plugin.YouzanWebClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import cay.com.xiaowei.MyApplication;
import cay.com.xiaowei.R;
import cay.com.xiaowei.Util.OkhttpXiao;

/**
 * Created by C on 2016/8/3.
 */
public class HeHuoJiaMeng extends Fragment {
    private static final String TAG = "webURl";

    private String SHANGPU_URL;
    public static YouzanBrowser mWebView;
    private Handler urlHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String webUrl = (String) msg.obj;
            JSONObject urlJsonObject = null;
            try {
                urlJsonObject = new JSONObject(webUrl);
                final String shangPuUrl = urlJsonObject.getString("text");
                mWebView.loadUrl(shangPuUrl);
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shangcheng, null);
        mWebView = (YouzanBrowser) view.findViewById(R.id.youzan_webView);
        initUrls();
        return view;
    }

    /**
     * 根据需求订阅相应的桥接事件
     */
    private void setupWebView() {
        //订阅分享要响应的内容
        mWebView.subscribe(new ShareDataEvent() {
            @Override
            public void call(IBridgeEnv env, GoodsShareModel data) {
                //也可以集成个推等分享工具

                String content = data.getDesc() + data.getLink();
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, content);
                sendIntent.putExtra(Intent.EXTRA_SUBJECT, data.getTitle());
                sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });

        mWebView.setWebChromeClient(new YouzanChromeClient() {

            @Override
            public void onStartActivityForUpload(Intent intent, int requestId)
                    throws ActivityNotFoundException {
                /**
                 * 父方法默认实现是使用Activity#startActivityForResult.
                 * 如果是在Fragment中调用则需要重写, 并删除回调父方法.
                 * 如果是在Activity中调用, 这块代码就不用再重写了.
                 */
                startActivityForResult(intent, requestId);
            }
        });
    }

    private void initUrls() {
        try {
            SHANGPU_URL = MyApplication.URL + "?key=" + MyApplication.API_KEY_XIAOWEI + "&info="
                    + URLEncoder.encode("小微商城地址_URL", "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        new OkhttpXiao(SHANGPU_URL, urlHandler);

        mWebView.setWebViewClient(new YouzanWebClient() {


            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);

            }
        });


    }


}
