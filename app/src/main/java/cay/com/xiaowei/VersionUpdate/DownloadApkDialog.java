package cay.com.xiaowei.VersionUpdate;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

import cay.com.xiaowei.R;

/**
 * 自定义下载apk dialog
 * @author choy
 *
 */
public class DownloadApkDialog extends Dialog {
	/**
	 * 下载进度条
	 */
	private ProgressBar progressBar;
	/**
	 * 下载进度提示
	 */
	private TextView tvProgress;
	/**
	 * 取消
	 */
	private TextView tvCancel;
	/**
	 * 下载对外接口
	 */
	private OnDownloadDialogClickListener listener;

	public DownloadApkDialog(Context context,OnDownloadDialogClickListener listener) {
		super(context);
		this.listener = listener;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_download_apk);
		findView();
		setCanceledOnTouchOutside(false);
		tvCancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				listener.onCancle(DownloadApkDialog.this);			
			}
		});
	}
	
	private void findView(){
		progressBar = (ProgressBar) findViewById(R.id.progressBarDownload);
		tvProgress = (TextView) findViewById(R.id.tvProgress);
		tvCancel = (TextView) findViewById(R.id.tvCancelDownload);
	}
	/**
	 * 设置进度条
	 * @param progress
	 */
	public void setProgress(int progress){
		progressBar.setProgress(progress);
	}
	/**
	 * 设置进度百分比显示
	 * @param progress
	 */
	public void setProgressText(int progress){
		tvProgress.setText(progress+"%");
	}
	/**
	 * 设置进度条文字颜色
	 * @param resId
	 */
	public void setProgresstextColor(int resId){
		tvProgress.setTextColor(resId);
	}
	
	/**
	 * 处理自定义弹出框用户点击按钮的事件
	 * 
	 * @author Administrator
	 *
	 */
	public interface OnDownloadDialogClickListener {	
		//取消
		void onCancle(Dialog dialog);
	}
}
