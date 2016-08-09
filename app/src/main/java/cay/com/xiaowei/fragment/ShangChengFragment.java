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

import com.youzan.sdk.model.goods.GoodsShareModel;
import com.youzan.sdk.web.bridge.IBridgeEnv;
import com.youzan.sdk.web.event.ShareDataEvent;
import com.youzan.sdk.web.plugin.YouzanBrowser;
import com.youzan.sdk.web.plugin.YouzanChromeClient;

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
 * Created by C on 2016/8/3.
 */
public class ShangChengFragment extends Fragment {
    private String SHANGPU_URL;
    public static YouzanBrowser mWebView;
    private Handler urlHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String webUrl = (String) msg.obj;
            mWebView.loadUrl(webUrl);

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
                    + URLEncoder.encode("小微Splash_URL", "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        OkHttpClient urlHttpClient = new OkHttpClient();
        Request urlRequest = new Request.Builder()
                .url(SHANGPU_URL)
                .build();
        Call urlCall = urlHttpClient.newCall(urlRequest);
        urlCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String shangPuUrlJson = response.body().string();
                try {
                    JSONObject urlJsonObject = new JSONObject(shangPuUrlJson);
                    final String shangPuUrl = urlJsonObject.getString("text");
                    Message message = new Message();
                    message.obj = shangPuUrl;
                    urlHandler.sendMessage(message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


    }


}
