package cay.com.xiaowei.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import cay.com.xiaowei.Adapter.ViewPagerAdapter;
import cay.com.xiaowei.MyApplication;
import cay.com.xiaowei.R;
import cay.com.xiaowei.Util.OkhttpXiao;
import cay.com.xiaowei.VersionUpdate.VersionUpdate;
import cay.com.xiaowei.VersionUpdate.VersionUpdateManager;
import cay.com.xiaowei.fragment.ChongZhiFragment;
import cay.com.xiaowei.fragment.GeRenFragment;
import cay.com.xiaowei.fragment.ShangChengFragment;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private TabLayout mTabLayout;
    private List<Fragment> fragmentList;
    private ViewPager mViewPager;
    private ViewPagerAdapter mAdapter;
    private List<VersionUpdate> updetasList;
    private String VER_URL;
    public static String VersionName;
    public static int ForcedUpdate;
    public static String URLaddress;

    public static String vip;
    public static String UserId;
    public static String Gender;
    public static String NikeName;
    public static String UserName;
    public static String Telphone;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        initFragmentList();
        initUserXinXi();
      //  CrashReport.testJavaCrash();

        initViews();//初始化化所有View
        setToolbar();//TOOLBAR 相关设置
        setTabLayout();//TabLayouot相关设置
        versionUpdateJianCe();

    }

    private void initUserXinXi() {
        Intent intent = getIntent();
        Bundle mBundle = intent.getExtras();
        String user = mBundle.getString("USER_FAN");
        try {
            JSONObject userJsonObject = new JSONObject(user);
            vip = userJsonObject.getString("VIP");
            UserId = userJsonObject.getString("UserId");
            Gender = userJsonObject.getString("Gender");
            NikeName = userJsonObject.getString("NikeName");
            UserName = userJsonObject.getString("UserName");
            Telphone = userJsonObject.getString("Telphone");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initFragmentList() {

        fragmentList = new ArrayList<Fragment>();
        fragmentList.add(new ShangChengFragment());
        fragmentList.add(new ChongZhiFragment());
        fragmentList.add(new GeRenFragment());

    }

    /**
     * TabLyout相关设置
     */
    private void setTabLayout() {
        mAdapter = new ViewPagerAdapter(getSupportFragmentManager(), fragmentList);

        mViewPager.setAdapter(mAdapter);

        mTabLayout.setBackgroundColor(Color.parseColor("#4285f4"));
            mTabLayout.setTabsFromPagerAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

    }

    /**
     * Toolbar 相关设置
     */
    private void setToolbar() {
       // mToolbar.setTitle(R.string.toolbar_title);
    }



    /**
     * 初始化View
     */
    private void initViews() {
     //   mWebView = (YouzanBrowser) findViewById(R.id.youzanWeb);
        mTabLayout = (TabLayout) findViewById(R.id.tablyout);
       // mToolbar = (Toolbar) findViewById(R.id.mtoolbar);
        mViewPager = (ViewPager) findViewById(R.id.viewPager1);

    }
    /**
     * 页面回退
     * bridge.pageGoBack()返回True表示处理的是网页的回退
     */
    @Override
    public void onBackPressed() {
        if (!ShangChengFragment.mWebView.pageGoBack()) {
            super.onBackPressed();
        }

    }
    /**
     * 处理WebView上传文件
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (ShangChengFragment.mWebView.isReceiveFileForWebView(requestCode, data)) {
            return;
        }
        //...Other request things

    }

    /**
     * 版本更新数据监测
     */

    private void versionUpdateJianCe() {

        try {
            VER_URL = MyApplication.URL + "?key=" + MyApplication.API_KEY_XIAOWEI + "&info="
                    + URLEncoder.encode("小微版本更新_JSON", "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        OkhttpXiao mOkhttpXiao = new OkhttpXiao(VER_URL, handler);

    }

    /**
     * 请求版本更新的响应
     */
    public void responseVersionUpdate(List<VersionUpdate> responses) {
        if (responses.size() < 1) {
            return;
        }
        VersionUpdate versionUpdate = responses.get(0);
        VersionUpdateManager update = new VersionUpdateManager(this,
                versionUpdate.getVersion(), versionUpdate.getURLaddress());
        // 强制更新
        if (versionUpdate.getForcedUpdate() == 1) {
            update.setForcedUpdate(true);
            update.setTitle(this.getResources().getString(
                    R.string.version_update_tips_force));
        }
        update.setShowResult(false);
        update.startUpdate();

    }
private Handler handler = new Handler(){

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        String verChuan = msg.obj.toString();
        try {
            JSONObject verTextJsonObject = new JSONObject(verChuan);
            JSONObject verJsonObject = verTextJsonObject.getJSONObject("text");
            VersionName = verJsonObject.getString("VersionName");
            ForcedUpdate = verJsonObject.getInt("ForcedUpdate");
            URLaddress = verJsonObject.getString("URLaddress");

            Log.i("TAG", "VersionName: "+VersionName+ "   ForcedUpdate:"+ForcedUpdate+"    URLaddress:"+URLaddress);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        VersionUpdate versionUpdate = new VersionUpdate();
        versionUpdate.setForcedUpdate(ForcedUpdate);
        versionUpdate.setURLaddress(URLaddress);
        versionUpdate.setVersion(Float.parseFloat(VersionName));
        updetasList = new ArrayList<VersionUpdate>();
        updetasList.add(versionUpdate);
        responseVersionUpdate(updetasList);
    }
};


}
