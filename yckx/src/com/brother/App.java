package com.brother;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import io.rong.imkit.RongIM;
import android.app.ActivityManager;
import android.content.Context;
public class App extends BaseApplication{


	/** ȫ��Context��ԭ������ΪApplication����Ӧ���������еģ����������ǵĴ������ʱ����ֵ�Ѿ�����ֵ���� */
	private static App mInstance;
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		mInstance = this;
		super.onCreate();
		//����ͼƬר��
		initImageLoader(getApplicationContext());

		/**
		 * OnCreate �ᱻ����������룬��α������룬ȷ��ֻ������Ҫʹ�� RongIM �Ľ��̺� Push ����ִ���� init��
		 * io.rong.push Ϊ���� push �������ƣ������޸ġ�
		 */
		if (getApplicationInfo().packageName.equals(getCurProcessName(getApplicationContext())) ||
				"io.rong.push".equals(getCurProcessName(getApplicationContext()))) {

			/**
			 * IMKit SDK���õ�һ�� ��ʼ��
			 */
			RongIM.init(this);

		}
	}


	/**
	 * ��ʼ��Imageloader
	 * @param context
	 */
	public static void initImageLoader(Context context) {

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
		.threadPriority(Thread.NORM_PRIORITY - 2)
		.denyCacheImageMultipleSizesInMemory()
		.discCacheFileNameGenerator(new Md5FileNameGenerator())
		.tasksProcessingOrder(QueueProcessingType.LIFO)
		.writeDebugLogs()
		.build();
		ImageLoader.getInstance().init(config);
	}

	/**
	 * ��õ�ǰ���̵�����
	 *
	 * @param context
	 * @return ���̺�
	 */
	public static String getCurProcessName(Context context) {

		int pid = android.os.Process.myPid();

		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);

		for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
				.getRunningAppProcesses()) {

			if (appProcess.pid == pid) {
				return appProcess.processName;
			}
		}
		return null;
	}


	public void exitApp() {
		System.gc();
		android.os.Process.killProcess(android.os.Process.myPid());
	}
	
	
	public static App getApplication() {
        return mInstance;
    }

}
