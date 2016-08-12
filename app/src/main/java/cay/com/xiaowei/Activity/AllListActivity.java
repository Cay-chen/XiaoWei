package cay.com.xiaowei.Activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
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
import de.greenrobot.event.EventBus;

/**
 * Created by C on 2016/8/11.
 */
public class AllListActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_all_list);
         }
}
