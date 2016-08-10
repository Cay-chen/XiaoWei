package cay.com.xiaowei.Util;

import android.os.Handler;
import android.os.Message;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Cay on 2016/8/10.
 */
public class OkhttpXiao {
    private String mUrl;
    private Handler mHandler;

    public OkhttpXiao(String mUrl, Handler mHandler) {
        this.mUrl = mUrl;
        this.mHandler = mHandler;
        okHttpP();
    }
private void okHttpP(){
    OkHttpClient okHttpClient = new OkHttpClient();
    Request request = new Request.Builder()
            .url(mUrl)
            .build();
    Call call = okHttpClient.newCall(request);
    call.enqueue(new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {

        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            Message message = new Message();
            message.obj = response.body().string();
            mHandler.sendMessage(message);
        }
    });


}





}
