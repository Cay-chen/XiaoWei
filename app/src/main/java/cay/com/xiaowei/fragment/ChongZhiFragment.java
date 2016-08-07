package cay.com.xiaowei.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.sunfusheng.marqueeview.MarqueeView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import cay.com.xiaowei.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by C on 2016/8/3.
 */
public class ChongZhiFragment extends Fragment implements View.OnClickListener {
    private static final String URL = "http://www.tuling123.com/openapi/api";
    private static final String API_KEY = "be19bed1d5a44286bad4ab24c4910cba";
    private String KEY = "e2b55f4b2a335d3e8627ff226d7cd9d5";
    private View view;
    private ImageView mImageView;
    private String JINE = "";
    private String PHONE = "";
    private Button btn10;
    private Button btn20;
    private Button btn30;
    private Button btn50;
    private Button btn100;
    private Button btn200;
    private Button chaxun;
    private EditText phoneEd;
    private MarqueeView mMarqueeView;
    private String dialogItem[];
    private String PAOMADENG_URL;
    private String IMAGE_URL;
    private Handler imageHandler = new Handler();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String rest = (String) msg.obj;
            JSONObject jsonArray = null;
            try {
                jsonArray = new JSONObject(rest);
                String rse = jsonArray.getString("reason");
                if (rse.equals("错误的手机号码")) {
                    Toast.makeText(getActivity(), rse, Toast.LENGTH_SHORT).show();
                }
                JSONObject abc = jsonArray.getJSONObject("result");
                String neixing = abc.getString("cardname");
                String difang = abc.getString("game_area");
                String haoma = "手机号码： " + PHONE;
                String difang1 = "号码归属地：" + difang;
                String jinedu = "充值金额： " + JINE + "元";


                dialogItem = new String[]{haoma, difang1, jinedu};
                dialog();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    };
    private Handler marqueHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String marque = (String) msg.obj;
            try {
                JSONObject marqueJsonObject = new JSONObject(marque);

                String notice = marqueJsonObject.getString("text");
                mMarqueeView.startWithText(notice);

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.chongzhi, null);
        initView();//初始化View

        btn10.setOnClickListener(this);
        btn20.setOnClickListener(this);
        btn30.setOnClickListener(this);
        btn50.setOnClickListener(this);
        btn100.setOnClickListener(this);
        btn200.setOnClickListener(this);
        chaxun.setOnClickListener(this);
        mMarqueeView = (MarqueeView) view.findViewById(R.id.marqueeView);
        showImage();
        showMarqueeView();
        return view;
    }

    /**
     * 初始化View
     */
    private void initView() {
        phoneEd = (EditText) view.findViewById(R.id.et_phone1);
        btn10 = (Button) view.findViewById(R.id.btn_10);
        btn20 = (Button) view.findViewById(R.id.btn_20);
        btn30 = (Button) view.findViewById(R.id.btn_30);
        btn50 = (Button) view.findViewById(R.id.btn_50);
        btn100 = (Button) view.findViewById(R.id.btn_100);
        btn200 = (Button) view.findViewById(R.id.btn_200);
        chaxun = (Button) view.findViewById(R.id.chaxu_btn);
        mImageView = (ImageView) view.findViewById(R.id.image_chongzhi);
    }

    /**
     * 图片显示
     */

    private void showImage() {
        try {
            IMAGE_URL = URL + "?key=" + API_KEY + "&info="
                    + URLEncoder.encode("小微充值图片Cay-chen", "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        OkHttpClient imageHttpClient = new OkHttpClient();
        Request imageRequest = new Request.Builder()
                .url(IMAGE_URL)
                .build();
        Call imageCall = imageHttpClient.newCall(imageRequest);
        imageCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String sussc = response.body().string();
                imageHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject imageJsonObject = new JSONObject(sussc);
                            Glide.with(getActivity()).load(imageJsonObject.getString("text")).into(mImageView);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });


    }

    private void showMarqueeView() {
        try {
            PAOMADENG_URL = URL + "?key=" + API_KEY + "&info="
                    + URLEncoder.encode("小微充值跑马灯Cay-chen", "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        OkHttpClient marqueClient = new OkHttpClient();
        Request marqueRequest = new Request.Builder()
                .url(PAOMADENG_URL)
                .build();
        Call call1 = marqueClient.newCall(marqueRequest);
        call1.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String sussc = response.body().string();
                Message message = new Message();
                message.obj = sussc;
                marqueHandler.sendMessage(message);

            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btn_10:
                JINE = "10";
                btn10.setTextColor(Color.parseColor("#FF0000"));
                btn20.setTextColor(Color.parseColor("#000000"));
                btn30.setTextColor(Color.parseColor("#000000"));
                btn50.setTextColor(Color.parseColor("#000000"));
                btn100.setTextColor(Color.parseColor("#000000"));
                btn200.setTextColor(Color.parseColor("#000000"));
                break;
            case R.id.btn_20:
                JINE = "20";
                btn10.setTextColor(Color.parseColor("#000000"));
                btn20.setTextColor(Color.parseColor("#FF0000"));
                btn30.setTextColor(Color.parseColor("#000000"));
                btn50.setTextColor(Color.parseColor("#000000"));
                btn100.setTextColor(Color.parseColor("#000000"));
                btn200.setTextColor(Color.parseColor("#000000"));
                break;
            case R.id.btn_30:
                JINE = "30";
                btn10.setTextColor(Color.parseColor("#000000"));
                btn20.setTextColor(Color.parseColor("#000000"));
                btn30.setTextColor(Color.parseColor("#FF0000"));
                btn50.setTextColor(Color.parseColor("#000000"));
                btn100.setTextColor(Color.parseColor("#000000"));
                btn200.setTextColor(Color.parseColor("#000000"));
                break;
            case R.id.btn_50:
                JINE = "50";
                btn10.setTextColor(Color.parseColor("#000000"));
                btn20.setTextColor(Color.parseColor("#000000"));
                btn30.setTextColor(Color.parseColor("#000000"));
                btn50.setTextColor(Color.parseColor("#FF0000"));
                btn100.setTextColor(Color.parseColor("#000000"));
                btn200.setTextColor(Color.parseColor("#000000"));
                break;
            case R.id.btn_100:
                JINE = "100";
                btn10.setTextColor(Color.parseColor("#000000"));
                btn20.setTextColor(Color.parseColor("#000000"));
                btn30.setTextColor(Color.parseColor("#000000"));
                btn50.setTextColor(Color.parseColor("#000000"));
                btn100.setTextColor(Color.parseColor("#FF0000"));
                btn200.setTextColor(Color.parseColor("#000000"));
                break;
            case R.id.btn_200:
                JINE = "200";
                btn10.setTextColor(Color.parseColor("#000000"));
                btn20.setTextColor(Color.parseColor("#000000"));
                btn30.setTextColor(Color.parseColor("#000000"));
                btn50.setTextColor(Color.parseColor("#000000"));
                btn100.setTextColor(Color.parseColor("#000000"));
                btn200.setTextColor(Color.parseColor("#FF0000"));
                break;

            case R.id.chaxu_btn:
                PHONE = phoneEd.getText().toString();
                if (PHONE.equals("")) {
                    Toast.makeText(getActivity(), "请输入手机号码", Toast.LENGTH_SHORT).show();
                    break;
                }
                if (JINE.equals("")) {
                    Toast.makeText(getActivity(), "请选择充值金额", Toast.LENGTH_SHORT).show();
                    break;
                }

                /**
                 * 检测网络是否连接
                 * @return
                 */
                boolean flag = false;
                //得到网络连接信息
                ConnectivityManager manager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                //去进行判断网络是否连接
                if (manager.getActiveNetworkInfo() != null) {
                    flag = manager.getActiveNetworkInfo().isAvailable();
                }
                if (!flag) {
                    Toast.makeText(getActivity(), "网络不可用，请检查网络", Toast.LENGTH_SHORT).show();
                }

                OkHttpClient httpClient1 = new OkHttpClient();
                final Request request1 = new Request.Builder()
                        .url("http://op.juhe.cn/ofpay/mobile/telquery?cardnum=" + JINE + "&phoneno=" + PHONE + "&key=" + KEY)
                        .build();
                Call call1 = httpClient1.newCall(request1);
                call1.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        final String sussc = response.body().string();
                        Message message = new Message();
                        message.obj = sussc;
                        handler.sendMessage(message);
                    }
                });
                break;
        }
    }

    private void dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());//先得到构器
        builder.setTitle("确认信息");//设置标题
        builder.setItems(dialogItem, null);
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // dialogInterface.dismiss();
                Toast.makeText(getActivity(), "API接口没认证", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.create().show();


    }

}
