package cay.com.xiaowei.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.youzan.sdk.YouzanSDK;

import java.util.ArrayList;
import java.util.List;

import cay.com.xiaowei.Activity.LoginActivity;
import cay.com.xiaowei.Activity.MainActivity;
import cay.com.xiaowei.R;
import cay.com.xiaowei.VersionUpdate.VersionUpdate;
import cay.com.xiaowei.VersionUpdate.VersionUpdateManager;

/**
 * Created by C on 2016/8/3.
 */
public class GeRenFragment extends Fragment {
    private Button tuchuButton;
    private Button update;
    private Button zhuxiaoButton;
    private List<VersionUpdate> updetasList;

    private SharedPreferences sp;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.geren, null);
        tuchuButton = (Button) view.findViewById(R.id.me_btn_tuichu);
        zhuxiaoButton = (Button) view.findViewById(R.id.me_btn_zhuxiao);
        update = (Button) view.findViewById(R.id.me_btn_gengxin);
        sp = getActivity().getSharedPreferences("password", 0);

        tuchuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();

            }
        });
        zhuxiaoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor edit = sp.edit();
                edit.putString("name", "");
                edit.putString("pwd", "");
                //存储checkbpox的状态
                edit.putBoolean("ischecked", false);
                // 记得edit的提交
                edit.commit();
                YouzanSDK.userLogout(getActivity());
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("CrashReport", "update: ");
                VersionUpdate versionUpdate = new VersionUpdate();
                versionUpdate.setForcedUpdate(MainActivity.ForcedUpdate);
                versionUpdate.setURLaddress(MainActivity.URLaddress);
                versionUpdate.setVersion(Float.parseFloat(MainActivity.VersionName));
                updetasList = new ArrayList<VersionUpdate>();
                updetasList.add(versionUpdate);
                responseVersionUpdate(updetasList);

            }
        });
        return view;
        //nihao
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    /**
     * 请求版本更新的响应
     */
    public void responseVersionUpdate(List<VersionUpdate> responses) {
        if (responses.size() < 1) {
            return;
        }
        VersionUpdate versionUpdate = responses.get(1);
        VersionUpdateManager update = new VersionUpdateManager(getActivity(),
                versionUpdate.getVersion(), versionUpdate.getURLaddress());
        // 强制更新
        if (versionUpdate.getForcedUpdate() == 1) {
            update.setForcedUpdate(true);
            update.setTitle(getActivity().getResources().getString(
                    R.string.version_update_tips_force));
        }
        update.setShowResult(false);
        update.startUpdate();

    }
}
