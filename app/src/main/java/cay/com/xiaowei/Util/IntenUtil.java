package cay.com.xiaowei.Util;

import android.content.Context;
import android.content.Intent;

/**
 * Created by Cay on 2016/8/12.
 */
public class IntenUtil {
    private Context context;
    private Class<?> aClass;
    private String tag;
    private String content;

    public IntenUtil(Context context, Class<?> aClass, String tag, String content) {
        this.context = context;
        this.aClass = aClass;
        this.tag = tag;
        this.content = content;
        inten2();
    }

    public IntenUtil(Context context, Class<?> aClass) {
        this.context = context;
        this.aClass = aClass;
        inten();
    }

    private void inten() {
        Intent intent = new Intent(context, aClass);
        context.startActivity(intent);

    }
    private void inten2() {
        Intent intent = new Intent(context, aClass);
        intent.putExtra(tag, content);
        context.startActivity(intent);

    }
}
