package cay.com.xiaowei.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.youzan.sdk.YouzanSDK;

import java.util.ArrayList;
import java.util.List;

import cay.com.xiaowei.Activity.AllListActivity;
import cay.com.xiaowei.Activity.InformUsActivity;
import cay.com.xiaowei.Activity.LoginActivity;
import cay.com.xiaowei.Activity.MainActivity;
import cay.com.xiaowei.Activity.MyVipActivity;
import cay.com.xiaowei.Activity.YZwebActivity;
import cay.com.xiaowei.R;
import cay.com.xiaowei.Util.IntenUtil;
import cay.com.xiaowei.VersionUpdate.VersionUpdate;
import cay.com.xiaowei.VersionUpdate.VersionUpdateManager;

/**
 * Created by C on 2016/8/3.
 */
public class GeRenFragment extends Fragment implements View.OnClickListener {
    private Button tuchuButton;
    private Button zhuxiaoButton;
    private List<VersionUpdate> updetasList;
    private RelativeLayout mShopCardRl;
    private RelativeLayout mAllList;
    private RelativeLayout mMyVip;
    private RelativeLayout mInformUs;
    private RelativeLayout mVersionUpdate;
    private RelativeLayout mMvp1;
    private RelativeLayout mMvp2;
    private RelativeLayout mMvp3;
    private boolean isVip = false;
    private ImageView upDownImageView;
    private TextView userName;
    private View view;
    private SharedPreferences sp;
    private TextView vipTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_geren, null);
        initView();//初始化View
        setOCL();//设置所有按钮监听
        sp = getActivity().getSharedPreferences("password", 0); //初始化SP 注销清除记录使用
        userName.setText("用户名：" + MainActivity.NikeName);
        vipTextView.setText(MainActivity.vip+"级代理");
        return view;
    }

    /**
     * 注册所有监听
     */
    private void setOCL() {
        tuchuButton.setOnClickListener(this);
        zhuxiaoButton.setOnClickListener(this);
        mShopCardRl.setOnClickListener(this);
        mAllList.setOnClickListener(this);
        mMyVip.setOnClickListener(this);
        mInformUs.setOnClickListener(this);
        mVersionUpdate.setOnClickListener(this);

    }

    /**
     * 初始化View
     */

    private void initView() {
        vipTextView = (TextView) view.findViewById(R.id.tv_vip);
        mShopCardRl = (RelativeLayout) view.findViewById(R.id.rl_shopping_card);
        userName = (TextView) view.findViewById(R.id.tv_username);
        tuchuButton = (Button) view.findViewById(R.id.me_btn_tuichu);
        zhuxiaoButton = (Button) view.findViewById(R.id.me_btn_zhuxiao);
        mShopCardRl = (RelativeLayout) view.findViewById(R.id.rl_shopping_card);
        mAllList = (RelativeLayout) view.findViewById(R.id.rl_allList);
        mMyVip = (RelativeLayout) view.findViewById(R.id.rl_myVip);
        mInformUs = (RelativeLayout) view.findViewById(R.id.rl_InformUs);
        mVersionUpdate = (RelativeLayout) view.findViewById(R.id.rl_versionUpdate);
        mMvp1 = (RelativeLayout) view.findViewById(R.id.rl_vip1);
        mMvp2 = (RelativeLayout) view.findViewById(R.id.rl_vip2);
        mMvp3 = (RelativeLayout) view.findViewById(R.id.rl_vip3);
        upDownImageView = (ImageView) view.findViewById(R.id.me_vip_upDown);
    }

    /**
     * 请求版本更新的响应
     */

    public void responseVersionUpdate(List<VersionUpdate> responses) {
        if (responses.size() < 1) {
            return;
        }
        VersionUpdate versionUpdate = responses.get(0);

        VersionUpdateManager update = new VersionUpdateManager(getActivity(),
                versionUpdate.getVersion(), versionUpdate.getURLaddress());
        if (versionUpdate.getVersion() <=update.getLocalVersionCode(getActivity()) ) {
            Toast.makeText(getActivity(),"你现在是最新版本",Toast.LENGTH_LONG).show();
        }
        // 强制更新
        if (versionUpdate.getForcedUpdate() == 1) {
            update.setForcedUpdate(true);
            update.setTitle(getActivity().getResources().getString(
                    R.string.version_update_tips_force));
        }
        update.setShowResult(false);
        update.startUpdate();

    }


    /**
     * 设置点击事件
     * @param view
     */

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.rl_shopping_card:
                Intent intent = new Intent(getActivity(), YZwebActivity.class);
                intent.putExtra("YZwebURL", "SHOPPING_CARD");
                startActivity(intent);
                break;
            case R.id.me_btn_tuichu:
                getActivity().finish();

                break;
            case R.id.me_btn_zhuxiao:
                SharedPreferences.Editor edit = sp.edit();
                edit.putString("name", "");
                edit.putString("pwd", "");
                //存储checkbpox的状态
                edit.putBoolean("ischecked", false);
                // 记得edit的提交
                edit.commit();
                YouzanSDK.userLogout(getActivity());
                Intent intent1 = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent1);
                getActivity().finish();
                break;
            case R.id.rl_versionUpdate:
                VersionUpdate versionUpdate = new VersionUpdate();
                versionUpdate.setForcedUpdate(MainActivity.ForcedUpdate);
                versionUpdate.setURLaddress(MainActivity.URLaddress);
                versionUpdate.setVersion(Float.parseFloat(MainActivity.VersionName));
                updetasList = new ArrayList<VersionUpdate>();
                updetasList.add(versionUpdate);
                responseVersionUpdate(updetasList);
                break;
            case R.id.rl_allList:
                Intent intentA = new Intent(getActivity(), YZwebActivity.class);
                intentA.putExtra("YZwebURL", "ALL_LIST");
                startActivity(intentA);
                break;
            case R.id.rl_myVip:
                //new IntenUtil(getActivity(), MyVipActivity.class);
                if (isVip) {
                    isVip = false;
                    upDownImageView.setImageResource(R.mipmap.vip_down);
                    mMvp1.setVisibility(View.GONE);
                    mMvp2.setVisibility(View.GONE);
                    mMvp3.setVisibility(View.GONE);

                } else {
                    isVip = true;
                    upDownImageView.setImageResource(R.mipmap.vip_up);
                    mMvp1.setVisibility(View.VISIBLE);
                    mMvp2.setVisibility(View.VISIBLE);
                    mMvp3.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.rl_InformUs:
                new IntenUtil(getActivity(), InformUsActivity.class);
                break;

        }

    }
}
