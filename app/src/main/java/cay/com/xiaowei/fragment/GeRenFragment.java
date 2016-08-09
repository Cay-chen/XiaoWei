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

import com.youzan.sdk.YouzanSDK;
import com.youzan.sdk.web.plugin.YouzanBrowser;

import cay.com.xiaowei.Activity.LoginActivity;
import cay.com.xiaowei.R;

/**
 * Created by C on 2016/8/3.
 */
public class GeRenFragment extends Fragment {
    private Button tuchuButton;
    private Button zhuxiaoButton;
    private SharedPreferences sp;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.geren, null);
        tuchuButton = (Button) view.findViewById(R.id.me_btn_tuichu);
        zhuxiaoButton = (Button) view.findViewById(R.id.me_btn_zhuxiao);
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
        return view;
        //nihao
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}
