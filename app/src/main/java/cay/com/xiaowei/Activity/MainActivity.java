package cay.com.xiaowei.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Window;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cay.com.xiaowei.Adapter.ViewPagerAdapter;
import cay.com.xiaowei.R;
import cay.com.xiaowei.fragment.ChongZhiFragment;
import cay.com.xiaowei.fragment.GeRenFragment;
import cay.com.xiaowei.fragment.ShangChengFragment;

public class MainActivity extends AppCompatActivity {
    private TabLayout mTabLayout;
    private List<Fragment> fragmentList;
    private ViewPager mViewPager;
    private ViewPagerAdapter mAdapter;
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

}
