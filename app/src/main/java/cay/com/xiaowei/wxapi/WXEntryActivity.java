package cay.com.xiaowei.wxapi;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cay.com.xiaowei.Activity.LoginActivity;
import cay.com.xiaowei.Util.OkhttpXiao;
import de.greenrobot.event.EventBus;

/**
 * Created by Cay on 2016/8/16.
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    private Context mContext;
    private String urll;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String jjm = msg.obj.toString();
            EventBus.getDefault().post(jjm);
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        LoginActivity.mWeiXinApi.handleIntent(getIntent(), this);
        Log.i("TAG", "onCreate: ");
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                String code = ((SendAuth.Resp) baseResp).code;
                Log.i("TAG", "code: " + code);

                if (code != null) {
                    urll = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=wxab940fc44ffda729&secret=19a13a1fe8c4aa79d95d1e18f66c9ca5&code=" + code + "&grant_type=authorization_code";
                    new OkhttpXiao(urll, handler);
                }
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                finish();
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                finish();
                break;
        }

    }


}
