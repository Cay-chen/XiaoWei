package cay.com.xiaowei.fragment;

import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.sunfusheng.marqueeview.MarqueeView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import cay.com.xiaowei.Activity.MainActivity;
import cay.com.xiaowei.MyApplication;
import cay.com.xiaowei.R;
import cay.com.xiaowei.Util.OkhttpXiao;

/**
 * Created by C on 2016/8/3.
 */
public class ChongZhiFragment extends Fragment implements View.OnClickListener {
    private View view;
    private ImageView mImageView;
    private String NOW_JINE = "";
    private String YUAN_JINE="";
    private String PHONE = "";
    private LinearLayout ll10;
    private LinearLayout ll20;
    private LinearLayout ll30;
    private LinearLayout ll50;
    private LinearLayout ll100;
    private LinearLayout ll200;
    private TextView tv10;
    private TextView tv20;
    private TextView tv30;
    private TextView tv50;
    private TextView tv100;
    private TextView tv200;

    private Button chaxun;
    private EditText phoneEd;
    private MarqueeView mMarqueeView;
    private String dialogItem[];
    private String PAOMADENG_URL;
    private String IMAGE_URL;
    private String VIP_MONEY;

    /**
     * 充值界面图片网络加载
     */
    private Handler imageHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            JSONObject imageJsonObject = null;
            try {
                imageJsonObject = new JSONObject(msg.obj.toString());
                Glide.with(getActivity()).load(imageJsonObject.getString("text")).into(mImageView);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    /**
     * 更具不同的代理等级，显示不同的金额
     */
    private Handler moneyHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String backMoney = msg.obj.toString();
            Log.i("TAG", "ont: " + backMoney);

            try {
                JSONObject moneyJsonObject = new JSONObject(backMoney);
                JSONObject arryJsonObject = moneyJsonObject.getJSONObject("text");
                JSONArray vipOne = arryJsonObject.getJSONArray("vip1");
                JSONArray vipTwo = arryJsonObject.getJSONArray("vip2");
                JSONArray vipStr = arryJsonObject.getJSONArray("vip3");
                switch (MainActivity.vip) {
                    case "一":
                        tv10.setText("￥" + vipOne.get(0));
                        tv20.setText("￥" + vipOne.get(1));
                        tv30.setText("￥" + vipOne.get(2));
                        tv50.setText("￥" + vipOne.get(3));
                        tv100.setText("￥" + vipOne.get(4));
                        tv200.setText("￥" + vipOne.get(5));
                        break;
                    case "二":
                        tv10.setText("￥" + vipTwo.get(0));
                        tv20.setText("￥" + vipTwo.get(1));
                        tv30.setText("￥" + vipTwo.get(2));
                        tv50.setText("￥" + vipTwo.get(3));
                        tv100.setText("￥" + vipTwo.get(4));
                        tv200.setText("￥" + vipTwo.get(5));
                        break;
                    case "三":
                        tv10.setText("￥" + vipStr.get(0));
                        tv20.setText("￥" + vipStr.get(1));
                        tv30.setText("￥" + vipStr.get(2));
                        tv50.setText("￥" + vipStr.get(3));
                        tv100.setText("￥" + vipStr.get(4));
                        tv200.setText("￥" + vipStr.get(5));
                        break;
                    case "null":
                        tv10.setText("￥" + vipStr.get(0));
                        tv20.setText("￥" + vipStr.get(1));
                        tv30.setText("￥" + vipStr.get(2));
                        tv50.setText("￥" + vipStr.get(3));
                        tv100.setText("￥" + vipStr.get(4));
                        tv200.setText("￥" + vipStr.get(5));
                        break;

                }

                int mSize = vipOne.length();
                for (int i = 0; i < mSize; i++) {

                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
    /**
     * 充值手机号码网络验证 并调用充值dialog
     */
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
                String jinedu = "充值面值： " + YUAN_JINE + "元";
                String kouchu = "支付金额： " + NOW_JINE.substring(1) + "元";
                Log.i("TAG", "jinedu: " + jinedu);

                dialogItem = new String[]{haoma, difang1, jinedu,kouchu};
                Log.i("TAG", "dialogItem: " + dialogItem);

                dialog();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    };
    /**
     * 跑马灯文字网络加载
     */
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
        view = inflater.inflate(R.layout.fargment_chongzhi, null);
        initView();//初始化View
        ll10.setOnClickListener(this);
        ll20.setOnClickListener(this);
        ll30.setOnClickListener(this);
        ll50.setOnClickListener(this);
        ll100.setOnClickListener(this);
        ll200.setOnClickListener(this);
        chaxun.setOnClickListener(this);
        mMarqueeView = (MarqueeView) view.findViewById(R.id.marqueeView);
        showImage();
        showMarqueeView();
        showVipMenoy();
        return view;
    }

    /**
     * 初始化View
     */
    private void initView() {
        phoneEd = (EditText) view.findViewById(R.id.et_phone1);
        ll10 = (LinearLayout) view.findViewById(R.id.ll_10);
        ll20 = (LinearLayout) view.findViewById(R.id.ll_20);
        ll30 = (LinearLayout) view.findViewById(R.id.ll_30);
        ll50 = (LinearLayout) view.findViewById(R.id.ll_50);
        ll100 = (LinearLayout) view.findViewById(R.id.ll_100);
        ll200 = (LinearLayout) view.findViewById(R.id.ll_200);
        tv10 = (TextView) view.findViewById(R.id.tv_10);
        tv20 = (TextView) view.findViewById(R.id.tv_20);
        tv30 = (TextView) view.findViewById(R.id.tv_30);
        tv50 = (TextView) view.findViewById(R.id.tv_50);
        tv100 = (TextView) view.findViewById(R.id.tv_100);
        tv200 = (TextView) view.findViewById(R.id.tv_200);
        chaxun = (Button) view.findViewById(R.id.chaxu_btn);
        mImageView = (ImageView) view.findViewById(R.id.image_chongzhi);
    }

    /**
     * 图片显示
     */

    private void showImage() {
        try {
            IMAGE_URL = MyApplication.URL + "?key=" + MyApplication.API_KEY_XIAOWEI + "&info="
                    + URLEncoder.encode("小微充值图片_URL", "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        new OkhttpXiao(IMAGE_URL, imageHandler);


    }

    /**
     * 跑马灯文字
     */
    private void showMarqueeView() {
        try {
            PAOMADENG_URL = MyApplication.URL + "?key=" + MyApplication.API_KEY_XIAOWEI + "&info="
                    + URLEncoder.encode("小微充值跑马灯_TEXT", "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        new OkhttpXiao(PAOMADENG_URL, marqueHandler);
    }

    /**
     * 请求VIP充值金额
     */

    private void showVipMenoy() {
        try {
            VIP_MONEY = MyApplication.URL + "?key=" + MyApplication.API_KEY_SHOP + "&info="
                    + URLEncoder.encode("小微充值代理等级金额_JSON", "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        new OkhttpXiao(VIP_MONEY, moneyHandler);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_10:
                YUAN_JINE = "10";
                NOW_JINE = tv10.getText().toString();
                ll10.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_edittext2));
                ll20.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_edittext));
                ll30.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_edittext));
                ll50.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_edittext));
                ll100.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_edittext));
                ll200.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_edittext));
                break;
            case R.id.ll_20:
                YUAN_JINE = "20";
                NOW_JINE = tv20.getText().toString();

                ll10.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_edittext));
                ll20.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_edittext2));
                ll30.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_edittext));
                ll50.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_edittext));
                ll100.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_edittext));
                ll200.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_edittext));
                break;
            case R.id.ll_30:
                YUAN_JINE = "30";
                NOW_JINE = tv30.getText().toString();

                ll10.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_edittext));
                ll20.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_edittext));
                ll30.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_edittext2));
                ll50.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_edittext));
                ll100.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_edittext));
                ll200.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_edittext));
                break;
            case R.id.ll_50:
                YUAN_JINE = "50";
                NOW_JINE = tv50.getText().toString();
                ll10.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_edittext));
                ll20.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_edittext));
                ll30.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_edittext));
                ll50.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_edittext2));
                ll100.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_edittext));
                ll200.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_edittext));
                break;
            case R.id.ll_100:
                YUAN_JINE = "100";
                NOW_JINE = tv100.getText().toString();
                ll10.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_edittext));
                ll20.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_edittext));
                ll30.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_edittext));
                ll50.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_edittext));
                ll100.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_edittext2));
                ll200.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_edittext));
                break;
            case R.id.ll_200:
                YUAN_JINE = "200";
                NOW_JINE = tv200.getText().toString();
                ll10.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_edittext));
                ll20.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_edittext));
                ll30.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_edittext));
                ll50.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_edittext));
                ll100.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_edittext));
                ll200.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_edittext2));
                break;

            case R.id.chaxu_btn:
                PHONE = phoneEd.getText().toString();
                if (PHONE.equals("")) {
                    Toast.makeText(getActivity(), "请输入手机号码", Toast.LENGTH_SHORT).show();
                    break;
                }
                if (YUAN_JINE.equals("")) {
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
                String urlurl = "http://op.juhe.cn/ofpay/mobile/telquery?cardnum=" + YUAN_JINE + "&phoneno=" + PHONE + "&key=" + MyApplication.CZ_KEY;
                new OkhttpXiao(urlurl, handler);
                break;
        }
    }

    /**
     * 确认手机号码dialog
     */

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
