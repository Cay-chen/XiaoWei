package cay.com.xiaowei.fragment;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.youzan.sdk.model.goods.GoodsShareModel;
import com.youzan.sdk.web.bridge.IBridgeEnv;
import com.youzan.sdk.web.event.ShareDataEvent;
import com.youzan.sdk.web.plugin.YouzanBrowser;
import com.youzan.sdk.web.plugin.YouzanChromeClient;
import com.youzan.sdk.web.plugin.YouzanWebClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import cay.com.xiaowei.MyApplication;
import cay.com.xiaowei.R;
import cay.com.xiaowei.Util.OkhttpXiao;

/**
 * Created by C on 2016/8/3.
 */
public class ShangChengFragment extends Fragment implements View.OnClickListener{
    private static final String TAG = "webURl";
    private ImageView shareShowPop;
    private String SHANGPU_URL;
    public static final String SIGN_URL = "URL";
    public static YouzanBrowser mWebView;
    private String mImagUrl;
    private String mLink;
    private String mTitle;
    private String mDesc;
    private PopupWindow popup;
    private Button shareQuxiaoButton;
    private ImageButton shareQQImageButton;
    private ImageButton shareWeiXinImageButton;
    private ImageButton sharePengYouImageButton;
    private ImageButton shareWeiBoImageButton;
    private ImageButton shareKongJianImageButton;
    private View customView;
    private Bitmap shareImagBitmap;

    private Handler shareHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
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
        shareShowPop = (ImageView) view.findViewById(R.id.tv_share_showpop);
        shareShowPop.setOnClickListener(this);
        // openWebview();
        setupWebView();
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
                Log.i(TAG, "content: " + content);
                Log.i(TAG, "getImgUrl: " + data.getImgUrl());
                Log.i(TAG, "getTitle: " + data.getTitle());
                Log.i(TAG, "getDesc: " + data.getDesc());
                Log.i(TAG, "getLink: " + data.getLink());
                mImagUrl = data.getImgUrl();
                mDesc = data.getDesc();
                mLink = data.getLink();
                mTitle = data.getTitle();
                new Thread(networkTask).start();

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


    /**
     * 微信分享 （这里仅提供一个分享网页的示例，其它请参看官网示例代码）
     *
     * @param flag(0:分享到微信好友，1：分享到微信朋友圈)
     */
    private void wechatShare(int flag, Bitmap bitmap) {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = mLink;  //分享的链接
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = mTitle;
        msg.description = mDesc;
        //这里替换一张自己工程里的图片资源
        // Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        msg.setThumbImage(bitmap);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = flag == 0 ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
        MyApplication.mWeiXinApi.sendReq(req);
    }

    private Bitmap returnBitmap(String url) {
        URL fileUrl = null;
        Bitmap bitmap = null;

        try {
            fileUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        try {
            HttpURLConnection conn = (HttpURLConnection) fileUrl
                    .openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;

    }

    /**
     * 网络操作相关的子线程
     */
    Runnable networkTask = new Runnable() {

        @Override
        public void run() {

            Bitmap mBitmap = returnBitmap(mImagUrl);
            Message msg = new Message();
            msg.obj = mBitmap;
            handler.sendMessage(msg);
        }
    };
    /**
     * 分享至朋友圈
     */
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            shareImagBitmap = (Bitmap) msg.obj;

        }
    };

    private void initPop() {
        customView = LayoutInflater.from(getActivity()).inflate(R.layout.share_item, null, false);
        shareQuxiaoButton = (Button) customView.findViewById(R.id.btn_share_quxiao);
        shareWeiBoImageButton = (ImageButton) customView.findViewById(R.id.btn_share_weibo);
        shareWeiXinImageButton = (ImageButton) customView.findViewById(R.id.btn_share_weixin);
        sharePengYouImageButton = (ImageButton) customView.findViewById(R.id.btn_share_pengyouquan);
        shareQQImageButton = (ImageButton) customView.findViewById(R.id.btn_share_qq);
        shareKongJianImageButton = (ImageButton) customView.findViewById(R.id.btn_share_kongjian);
       popup = new PopupWindow(customView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popup.setBackgroundDrawable(new ColorDrawable());
        popup.setOutsideTouchable(true);
        popup.setFocusable(true);
        customView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (popup !=null&& popup.isShowing()) {
                    popup.dismiss();
                    popup = null;
                }


                return false;
            }
        });
        shareWeiXinImageButton.setOnClickListener(this);
        sharePengYouImageButton.setOnClickListener(this);
        shareQQImageButton.setOnClickListener(this);
        shareKongJianImageButton.setOnClickListener(this);
        shareWeiBoImageButton.setOnClickListener(this);
        shareQuxiaoButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_share_showpop:
                mWebView.sharePage();
                if (popup != null && popup.isShowing()) {
                    popup.dismiss();
                    return;
                } else {
                    initPop();
                    popup.showAtLocation(shareShowPop, Gravity.BOTTOM,0,5);
                }
                break;
            case R.id.btn_share_weixin:
                wechatShare(1,shareImagBitmap);

                break;
            case R.id.btn_share_pengyouquan:
                wechatShare(0,shareImagBitmap);

                break;
            case R.id.btn_share_qq:

                break;
            case R.id.btn_share_kongjian:

                break;
            case R.id.btn_share_weibo:

                break;
            case R.id.btn_share_quxiao:
                popup.dismiss();
                break;

        }

    }

    private void initOnClick() {





    }
}
