package util;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.example.winsonyang.imtest.MainActivity;
import com.example.winsonyang.imtest.R;
import com.tencent.android.talk.IMMessage;

public class DemoUtil {
	private static int notifyID = 0525;

	public static boolean isAppOnForeground(Context context) {
		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> appProcesses = activityManager
				.getRunningAppProcesses();
		if (appProcesses == null)
			return false;
		for (RunningAppProcessInfo appProcess : appProcesses) {

			if (appProcess.processName.equals(context.getPackageName())
					&& appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
				return true;
			}
		}
		return false;
	}

	public static void showToast(final String toast, final Context context) {
		if (!isAppOnForeground(context))
			return;
		new Thread(new Runnable() {

			@Override
			public void run() {
				Looper.prepare();
				Toast.makeText(context, toast, Toast.LENGTH_SHORT).show();
				Looper.loop();
			}
		}).start();
	}

	public static int isBackground(Context context) {
		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		String packageName = " com.example.winsonyang.imtest";
		String activityClassName = " com.example.winsonyang.imtest.MainActivity";
		List<RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(1);
		if (tasksInfo.size() > 0) {
			ComponentName topConponent = tasksInfo.get(0).topActivity;
			if (packageName.equals(topConponent.getPackageName())) {
				// 当前的APP在前台运行
				if (topConponent.getClassName().equals(activityClassName)) {
					// 当前正在运行的是不是期望的Activity
					return 2;
				}
				return 1;
			} else {
				// 当前的APP在后台运行
				return 0;
			}
		}
		return 0;
	}

	public static boolean showNotifation(Context context, IMMessage message) {
		String from_user = message.fromUser;
		String msg = message.content;
		long maxId = message.msgId;
		try {
			if (isBackground(context) == 2)
				return true;
			Uri alarmSound = RingtoneManager
					.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
			//int d = 0;
			/*if (message.msgType == 1) {
				d = R.drawable.qunliao;
				maxId = Integer.valueOf(message.toUser);
			} else {
				d = R.drawable.ic_launcher;
			}*/

			Bitmap btm = BitmapFactory.decodeResource(context.getResources(), R.drawable.abc_ic_menu_copy_mtrl_am_alpha);
			NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
					context).setContentTitle(from_user).setSmallIcon(R.drawable.ic_launcher)
					.setContentText(msg).setSound(alarmSound);
			//mBuilder.setTicker("New message");// 第一次提示消息的时候显示在通知栏上
			// mBuilder.setNumber(12);
			mBuilder.setLargeIcon(btm);
			mBuilder.setAutoCancel(true);// 自己维护通知的消失

			/* 构建一个Intent
			Intent resultIntent = new Intent(context, MainActivity.class);
			 resultIntent.putExtra("user_name", from_user);
			resultIntent.putExtra("immsg", message);
			// 封装一个Intent
			PendingIntent resultPendingIntent = PendingIntent
					.getActivity(context, 0, resultIntent,
							PendingIntent.FLAG_UPDATE_CURRENT);
			// 设置通知主题的意图
			mBuilder.setContentIntent(resultPendingIntent);*/
			// 获取通知管理器对象
			NotificationManager mNotificationManager = (NotificationManager) context
					.getSystemService(Context.NOTIFICATION_SERVICE);
			mNotificationManager.notify((int) maxId, mBuilder.build());
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public static boolean isExitsSdcard() {
		if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
			return true;
		else
			return false;
	}

}
