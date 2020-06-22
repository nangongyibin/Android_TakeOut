package com.ngyb.takeout.jpush;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.ngyb.takeout.activity.MainActivity;
import com.ngyb.takeout.constant.Constant;
import com.ngyb.takeout.observable.OrderObservable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 * 
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {
	private static final String TAG = "JIGUANG-Example";
	private HashMap<String,String> resultMap = new HashMap<>();
//	@Override
//	public void onReceive(Context context, Intent intent) {
//		try {
//			Bundle bundle = intent.getExtras();
//			Logger.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));
//
//			if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
//				String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
//				Logger.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
//				//send the Registration Id to your server...
//
//			} else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
//				Logger.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
//				processCustomMessage(context, bundle);
//
//			} else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
//				Logger.d(TAG, "[MyReceiver] 接收到推送下来的通知");
//				int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
//				Logger.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);
//
//			} else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
//				Logger.d(TAG, "[MyReceiver] 用户点击打开了通知");
//
//				//打开自定义的Activity
//				Intent i = new Intent(context, TestActivity.class);
//				i.putExtras(bundle);
//				//i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
//				context.startActivity(i);
//
//			} else if(JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
//				boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
//				Logger.w(TAG, "[MyReceiver]" + intent.getAction() +" connected state change to "+connected);
//			} else {
//				Logger.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
//			}
//		} catch (Exception e){
//
//		}
//
//	}

	// 打印所有的 intent extra 数据
	private static String printBundle(Bundle bundle) {
		StringBuilder sb = new StringBuilder();
		for (String key : bundle.keySet()) {
			if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
			}else if(key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)){
				sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
			} else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
				if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
					Logger.i(TAG, "This message has no Extra data");
					continue;
				}

				try {
					JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
					Iterator<String> it =  json.keys();

					while (it.hasNext()) {
						String myKey = it.next();
						sb.append("\nkey:" + key + ", value: [" +
								myKey + " - " +json.optString(myKey) + "]");
					}
				} catch (JSONException e) {
					Logger.e(TAG, "Get message extra JSON error!");
				}

			} else {
				sb.append("\nkey:" + key + ", value:" + bundle.get(key));
			}
		}
		return sb.toString();
	}
	
	//send msg to MainActivity
	private void processCustomMessage(Context context, Bundle bundle) {
		if (Constant.isForeground) {
			String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
			String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
			Intent msgIntent = new Intent(Constant.MESSAGE_RECEIVED_ACTION);
			msgIntent.putExtra(Constant.KEY_MESSAGE, message);
			if (!ExampleUtil.isEmpty(extras)) {
				try {
					JSONObject extraJson = new JSONObject(extras);
					if (extraJson.length() > 0) {
						msgIntent.putExtra(Constant.KEY_EXTRAS, extras);
					}
				} catch (JSONException e) {

				}

			}
			LocalBroadcastManager.getInstance(context).sendBroadcast(msgIntent);
		}
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.e(TAG, "onReceive: "+intent.getAction() );
		if (Constant.MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
			String json = intent.getStringExtra(Constant.KEY_EXTRAS);//自定义消息
//        Bundle bundle = intent.getExtras();
//        if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
//            //json是从极光推送平台下来的附加字段维护的json串
//            String json = bundle.getString(JPushInterface.EXTRA_EXTRA);
//            //在用户收到了通知后,则通过代码更新订单状态,想办法调用observable中notifyobservers方法
//            //json解析转换成bean对象
			try {
				JSONObject jsonObject = new JSONObject(json);
				if (jsonObject.has(Constant.OrderInfo)) {
					String orderInfo = jsonObject.getString(Constant.OrderInfo);
					resultMap.put(Constant.OrderInfo, orderInfo);
				}
				if (jsonObject.has(Constant.TYPE)) {
					String type = jsonObject.getString(Constant.TYPE);
					resultMap.put(Constant.TYPE, type);
				}
				//获取快递小哥所在经纬度坐标,则获取服务器推送下来的lat和lng值
				if (jsonObject.has(Constant.LAT)) {
					String lat = jsonObject.getString(Constant.LAT);
					resultMap.put(Constant.LAT, lat);
				}
				if (jsonObject.has(Constant.LNG)) {
					String lng = jsonObject.getString(Constant.LNG);
					resultMap.put(Constant.LNG, lng);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			Log.e(TAG, "onMessage: " + resultMap + "====" + Thread.currentThread().getId() + "===" + Thread.currentThread().getName());
			OrderObservable.getInstance().changeUI(resultMap);
		}
	}
}
