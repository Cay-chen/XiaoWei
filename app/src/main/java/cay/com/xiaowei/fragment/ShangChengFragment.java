package cay.com.xiaowei.fragment;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
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

import cay.com.xiaowei.R;

/**
 * Created by C on 2016/8/3.
 */
public class ShangChengFragment extends Fragment {
    public static YouzanBrowser mWebView;
    private String URL = "https://wap.koudaitong.com/v2/feature/fgt82zc?&redirect_count=1967461";
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shangcheng, null);
        mWebView = (YouzanBrowser) view.findViewById(R.id.youzan_webView);
        mWebView.loadUrl(URL);
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





}
