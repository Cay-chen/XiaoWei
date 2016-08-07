package cay.com.xiaowei.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.youzan.sdk.web.plugin.YouzanBrowser;

import cay.com.xiaowei.R;

/**
 * Created by C on 2016/8/3.
 */
public class GeRenFragment extends Fragment {
    private Button tuchuButton;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.geren, null);
        tuchuButton = (Button) view.findViewById(R.id.me_btn_tuichu);
        tuchuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();

            }
        });
        return view;
        //nihao
    }
}
