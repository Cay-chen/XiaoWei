package cay.com.xiaowei.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cay.com.xiaowei.Adapter.ViewPagerAdapter;
import cay.com.xiaowei.Bean.Tap;
import cay.com.xiaowei.MyApplication;
import cay.com.xiaowei.R;
import cay.com.xiaowei.Util.OkhttpXiao;
import cay.com.xiaowei.VersionUpdate.VersionUpdate;
import cay.com.xiaowei.VersionUpdate.VersionUpdateManager;
import cay.com.xiaowei.fragment.ChongZhiFragment;
import cay.com.xiaowei.fragment.GeRenFragment;
import cay.com.xiaowei.fragment.ShangChengFragment;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;
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
    private static Boolean isExit = false;
    public static String vip;
    public static String UserId;
    public static String Gender;
    public static String NikeName;
    public static String UserName;
    public static String Telphone;
    public static String HeadUrl;
    private List<Tap> mTaps = new ArrayList<Tap>();
    private FragmentTabHost mTabhost;
    private LayoutInflater mInflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main1);
        initFragmentList();
        initUserXinXi();
        initViews();//初始化化所有View
        initTab();
        versionUpdateJianCe();
        EventBus.getDefault().register(this);//注册Eventbus

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);//反注册Eventvus
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onUserEvent(String event) {
        if (event.equals("BACK_HOME")) {
            mViewPager.setCurrentItem(0);//点击购物车的时候 显示
        }
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
            HeadUrl = userJsonObject.getString("headurl");
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

    private void exitBy2Click() {
        Timer tExit = null;
        if (isExit == false) {
            isExit = true; // 准备退出
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_LONG).show();
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                public void run() {
                    isExit = false; // 取消退出
                }
            }, 1500); // 如果1..5秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

        } else {
            finish();

        }
    }

    /**
     * TabLyout相关设置
     */
    private void setTabLayout() {
        mAdapter = new ViewPagerAdapter(getSupportFragmentManager(), fragmentList);

        mViewPager.setAdapter(mAdapter);

        mTabLayout.setBackgroundColor(Color.parseColor("#CE3B1A"));
        mTabLayout.setSelectedTabIndicatorColor(Color.parseColor("#377BE1"));
        mTabLayout.setTabsFromPagerAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

    }


    /**
     * 初始化View
     */
    private void initViews() {
        mTabLayout = (TabLayout) findViewById(R.id.tablyout);
        mViewPager = (ViewPager) findViewById(R.id.viewPager1);

    }

    /**
     * 页面回退
     * bridge.pageGoBack()返回True表示处理的是网页的回退
     */
    @Override
    public void onBackPressed() {
        if (!ShangChengFragment.mWebView.pageGoBack()) {
            exitBy2Click();
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

        new OkhttpXiao(VER_URL, handler);

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

    private Handler handler = new Handler() {
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

    private void initTab() {
        mTaps.add(new Tap(R.drawable.selector_shop, ShangChengFragment.class, R.string.shop));
        mTaps.add(new Tap(R.drawable.selector_chongzhi, ChongZhiFragment.class, R.string.chongzhi));
        // mTaps.add(new Tap( R.drawable.selector_map, HeHuoJiaMeng.class, R.string.huohejiameng));
        mTaps.add(new Tap(R.drawable.selector_me, GeRenFragment.class, R.string.me));
        mInflater = LayoutInflater.from(this);
        mTabhost = (FragmentTabHost) this.findViewById(android.R.id.tabhost);
        mTabhost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        for (Tap tap : mTaps) {
            TabHost.TabSpec tabSpec = mTabhost.newTabSpec(getString(tap
                    .getTitle()));
            tabSpec.setIndicator(buildIndicator(tap));
            mTabhost.addTab(tabSpec, tap.getFragement(), null);

        }
        //去掉分割线
        mTabhost.getTabWidget().setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);
        mTabhost.setCurrentTab(0);

    }

    private View buildIndicator(Tap tap) {
        View view = mInflater.inflate(R.layout.tabhost_item, null);
        ImageView img = (ImageView) view.findViewById(R.id.imageview);
        TextView text = (TextView) view.findViewById(R.id.textview);
        img.setBackgroundResource(tap.getIcon());
        text.setText(tap.getTitle());
        return view;


    }
}
