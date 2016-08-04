package cay.com.xiaowei;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.Toast;

import cay.com.xiaowei.fragment.ChongZhiFragment;
import cay.com.xiaowei.fragment.GeRenFragment;
import cay.com.xiaowei.fragment.ShangChengFragment;

public class MainActivity extends AppCompatActivity {
    private TabLayout mTabLayout;
    private Toolbar mToolbar;
    private FrameLayout mFrameLayout;
    private FragmentManager fm;
    private FragmentTransaction tx;
    private String URL = "https://wap.koudaitong.com/v2/feature/fgt82zc?&redirect_count=1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        initViews();//初始化化所有View
        setToolbar();//TOOLBAR 相关设置
        setTabLayout();//TabLayouot相关设置
        webViewLoadUrl();//WebView导入地址设置
        tx.add(R.id.fl,new ShangChengFragment());

    }

    /**
     * TabLyout相关设置
     */
    private void setTabLayout() {
        mTabLayout.setBackgroundColor(Color.parseColor("#F5F5DC"));
        mTabLayout.addTab(mTabLayout.newTab().setText("小微商城"));
        mTabLayout.addTab(mTabLayout.newTab().setText("小微充值"));
        mTabLayout.addTab(mTabLayout.newTab().setText("个人中心"));
        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if (tab.getText().equals("小微商城")){
                    tx.add(R.id.fl,new ShangChengFragment());
                } if (tab.getText().equals("小微充值")){
                    tx.add(R.id.fl,new ChongZhiFragment());

                }if (tab.getText().equals("个人中心")){
                    tx.add(R.id.fl,new GeRenFragment());

                }
                tx.commit();
                Toast.makeText(MainActivity.this, tab.getText(), Toast.LENGTH_LONG).show();

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    /**
     * Toolbar 相关设置
     */
    private void setToolbar() {
        mToolbar.setTitle(R.string.toolbar_title);
    }

    private void webViewLoadUrl() {
//        mWebView.loadUrl(URL);

    }

    /**
     * 初始化View
     */
    private void initViews() {
     //   mWebView = (YouzanBrowser) findViewById(R.id.youzanWeb);
        mTabLayout = (TabLayout) findViewById(R.id.tablyout);
        mToolbar = (Toolbar) findViewById(R.id.mtoolbar);
        mFrameLayout = (FrameLayout) findViewById(R.id.fl);
        fm = getFragmentManager();
        tx = fm.beginTransaction();
    }

}
