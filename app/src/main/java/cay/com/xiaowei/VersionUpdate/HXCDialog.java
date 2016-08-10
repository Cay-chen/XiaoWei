package cay.com.xiaowei.VersionUpdate;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import cay.com.xiaowei.R;

/**
 * @author XieZhongMing 
 * E-mail: zhongming-xie@manzz.com
 * @version 1.0
 * @date 2015-7-21 上午11:57:22
 * 默认弹出对话框
 */
public class HXCDialog extends Dialog implements View.OnClickListener {
    
    //private Context mContext;
    private TextView tv;  
    private TextView cancle;
    private TextView ok;
    private String content;
    private String positiveText = "";
    private String cancelText = "";
    private boolean isSig = false;
    private OnDialogClickListener listener;
  
    public HXCDialog(Context context, String content, OnDialogClickListener onDialogClickListener) {  
    	super(context, R.style.DefaultDialogStyle);  
    	//mContext = context;
    	this.content = content;
    	this.listener = onDialogClickListener;
    } 
    
    public HXCDialog(Context context, String content, boolean isSig, OnDialogClickListener onDialogClickListener) {  
    	super(context, R.style.DefaultDialogStyle);
    	//mContext = context;
    	this.isSig = isSig;
    	this.content = content;
    	this.listener = onDialogClickListener;
    }
  
    private HXCDialog(Context context, int theme) {  
        super(context, theme);  
        //mContext = context;
    }  
  
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.dialog_default);  
        tv = (TextView)findViewById(R.id.dialog_content);  
        tv.setText(content);  
        cancle = (TextView)findViewById(R.id.dialog_cancle);
        ok = (TextView)findViewById(R.id.dialog_ok);
        if(!positiveText.equals("")){
        	ok.setText(positiveText);
        }
        
        if(!cancelText.equals("")){
        	cancle.setText(cancelText);
        }
        if(isSig) 
        	cancle.setVisibility(View.GONE);
        else
        	cancle.setVisibility(View.VISIBLE);
        cancle.setOnClickListener(this);
        ok.setOnClickListener(this);
    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		dismiss();
		switch(v.getId()){
		case R.id.dialog_cancle:{
			listener.onCancle(this);
			break;
		}
		case R.id.dialog_ok:{
			listener.onPositive(this);
			break;
		}
		}
	}
	
	/**
	 * 处理自定义弹出框用户点击按钮的事件
	 * 
	 * @author Administrator
	 *
	 */
	public interface OnDialogClickListener {
		
		//确定
		void onPositive(Dialog dialog);
		
		//取消
		void onCancle(Dialog dialog);

	}
	/**
	 * 设置Positive显示的Text
	 * @param text
	 */
	public void setPositiveText(String text){
		positiveText = text;
	}
	/**
	 * 设置Cancel显示的text
	 * @param text
	 */
	public void setCancleText(String text){
		cancelText = text;
	}
}
	