package cay.com.xiaowei.VersionUpdate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import cay.com.xiaowei.R;

/**
 * 版本更新
 * 
 * @author choy
 * 
 */
public class VersionUpdateManager {
	/**
	 * APK 名字
	 */

	private static final String APK_NAME = "xiaowei.apk";

	/**
	 * 版本储存地址
	 */

	private static final String SAVE_DOWNLOAD_APK_PATH = "/xiaowei/download";

	/**
	 * 上下文
	 */
	private Context context;
	/**
	 * 服务器app版本号
	 */
	private float serviceVersionCode;
	/**
	 * 下载apk url
	 */
	private String dowanlodUrl;
	/**
	 * 下载存储路径
	 */
	private String savedDownloadPath = "";
	/**
	 * 下载进度
	 */
	private int progress;
	/**
	 * 是否取消更新
	 */
	private boolean cancelUpdate = false;
	/**
	 * 是否强制更新
	 */
	private boolean isForcedUpdate = false;
	/**
	 * 标题
	 */
	private String title = "";
	/**
	 * 下载dialog
	 */
	private DownloadApkDialog downloadDialog;
	/**
	 * 下载线程
	 */
	private Thread downLoadApkThread = null;
	/**
	 * 下载状态 --- 下载中
	 */
	private static final int DOWNLOADING = 1;
	/**
	 * 下载状态 --- 下载完成
	 */
	private static final int DOWNLOAD_FINISH = 2;
	/**
	 * 下载状态 --- 下载异常
	 */
	private static final int NETEXCEPTION = 3;
	/**
	 * 是否显示更新响应结果,默认显示
	 */
	private boolean isShowResult = true;

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DOWNLOADING:
				// 更新进度条
				downloadDialog.setProgress(progress);
				downloadDialog.setProgressText(progress);
				downloadDialog.setProgresstextColor(context.getResources().getColor(R.color.cl_43cfac));
				break;
			case DOWNLOAD_FINISH:
				// 安装apk
				installApk();
				break;
			// 获取版本信息时网络异常
			case NETEXCEPTION:
				Toast.makeText(context, context.getResources().getString(R.string.version_update_exception), Toast.LENGTH_SHORT).show();
				downloadDialog.setProgresstextColor(context.getResources().getColor(R.color.red));
				break;
			default:
				break;
			}
		}

	};
    /**
     * 
     * @param context
     * @param serviceVersionCode
     * @param url
     */
	public VersionUpdateManager(Context context, float serviceVersionCode,
			String url) {
		this.context = context;
		this.serviceVersionCode = serviceVersionCode;
		this.dowanlodUrl = url;
		this.title = context.getResources().getString(R.string.version_update_tips_normal);
	}
    /**
     * 开始更新
     */
	public void startUpdate() {
		if (isUpdate()) {
			showUpdateDialog();
		} else if(isShowResult) {
			Toast.makeText(context,context.getResources().getString(R.string.version_update_tips_result), Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 检查是否有新版本
	 * 
	 * @return
	 */
	private boolean isUpdate() {
		boolean isUpdate = false;
		float localVersionCode = getLocalVersionCode(context);
		if (localVersionCode < serviceVersionCode) {
			isUpdate = true;
		}
		return isUpdate;
	}

	/**
	 * 显示选择更新dialog
	 */
	private void showUpdateDialog() {
		HXCDialog dialog = new HXCDialog(context, title,
				new HXCDialog.OnDialogClickListener() {

					@Override
					public void onPositive(Dialog dialog) {
						showDownloadDialog();
						downApk();
					}

					@Override
					public void onCancle(Dialog dialog) {
						if(isForcedUpdate){
							System.exit(0);
						}
					}
				});
		dialog.setPositiveText(context.getResources().getString(R.string.version_update_positive));
		dialog.setCancleText(context.getResources().getString(R.string.version_update_oncancel));
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);
		dialog.show();
	}

	/**
	 * 下载中dialog
	 */
	private void showDownloadDialog() {
		downloadDialog = new DownloadApkDialog(context,
				new DownloadApkDialog.OnDownloadDialogClickListener() {

					@Override
					public void onCancle(Dialog dialog) {
						dialog.dismiss();
						cancelUpdate = true;
						if(isForcedUpdate){
							System.exit(0);
						}
					}
				});
		downloadDialog.show();
	}


	/**
	 * 下载apk
	 */
	private void downApk() {
		
		if (null != downLoadApkThread) {
			downLoadApkThread.interrupt();
			downLoadApkThread = null;
		}
		downLoadApkThread = new Thread(new downloadApkRunnable());
		downLoadApkThread.start();
	}

	/**
	 * 下载线程
	 * 
	 * @author choy
	 * 
	 */
	private class downloadApkRunnable implements Runnable {

		@Override
		public void run() {
			try {
				// 判断SD卡是否存在，并且是否具有读写权限
				if (Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED)) {
					// 获取存储卡的路径
					String sdPath = Environment.getExternalStorageDirectory()
							.getPath();
					savedDownloadPath = sdPath
							+SAVE_DOWNLOAD_APK_PATH;
					URL url = new URL(dowanlodUrl);
					// 创建连接
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					// 开始连接
					conn.connect();
					// 获取文件大小
					int length = conn.getContentLength();
					InputStream in = conn.getInputStream();
					File file = new File(savedDownloadPath);
					if (!file.exists()) {
						file.mkdirs();
					}
					File apkFile = new File(savedDownloadPath, APK_NAME);
					FileOutputStream fos = new FileOutputStream(apkFile);
					// 计数下载文件长度

					int count = 0;
					byte[] buf = new byte[1024];
					do {
						int readNum = in.read(buf);
						count += readNum;
						// 计算进度条位置
						progress = (int) (((float) count / length) * 100);
						handler.sendEmptyMessage(DOWNLOADING);
						if (readNum <= 0) {
							// 下载完成
							handler.sendEmptyMessage(DOWNLOAD_FINISH);
							break;
						}
						fos.write(buf, 0, readNum);
					} while (!cancelUpdate);
					fos.flush();
					fos.close();
					in.close();
					downloadDialog.dismiss();
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				handler.sendEmptyMessage(NETEXCEPTION);
			}
			// 取消下载对话框的显示
			cancelUpdate = false;

		}
	}
	
	/**
	 * 安装apk
	 */
	private void installApk() {
		File apkFile = new File(savedDownloadPath, APK_NAME);
		if (!apkFile.exists()) {
			return;
		}
		// 安装apk
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.parse("file://" + apkFile.toString()),
				"application/vnd.android.package-archive");
		context.startActivity(intent);
	}

	/**
	 * 获取本地版本号
	 * 
	 * @param context
	 * @return
	 */
	public float getLocalVersionCode(Context context) {
		float versionCode = 0;
		try {
			// 获取软件版本号，对应AndroidManifest.xml下android:versionCode
			String versionName = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionName;
			versionCode = Float.parseFloat(versionName);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionCode;
	}
    /**
     * 设置是否强制更新
     * @param isForcedUpdate
     */
	public void setForcedUpdate(boolean isForcedUpdate) {
		this.isForcedUpdate = isForcedUpdate;
	}
    /**
     * 设置标题
     * @param title
     */
	public void setTitle(String title) {
		this.title = title;
	}

	public void setShowResult(boolean isShowResult) {
		this.isShowResult = isShowResult;
	}	
}
