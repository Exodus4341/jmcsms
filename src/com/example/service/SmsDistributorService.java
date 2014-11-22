package com.example.service;

import java.util.Timer;
import java.util.TimerTask;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.example.jmcsms.MainActivity.ResponseReceiver;

public class SmsDistributorService extends IntentService {

	private static final long SEND_INTERVAL = 5000;
	
	public SmsDistributorService() {
		super("SmsDistributorService");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		Timer timer = new Timer();
		TimerTask task = new TimerTask() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Log.e("SERVICE","sulod");
				Intent intent = new Intent();
				intent.setAction(ResponseReceiver.ACTION_RESP);
				intent.addCategory(Intent.CATEGORY_DEFAULT);
				intent.putExtra("MESSAGE", 100);
				sendBroadcast(intent);
			}
		};
		timer.schedule(task, 10, SEND_INTERVAL);
	}
 
}
