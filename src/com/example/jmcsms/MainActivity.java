package com.example.jmcsms;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.TextView;

import com.example.helper.ServiceHandler;
import com.example.service.SmsDistributorService;

public class MainActivity extends Activity {

	// URL to get JSON Array
	private static String studentsUrl = "http://192.168.23.1/webbasedevaluationsystem/public/api/students.json";
	private static String evaluationUrl = "http://192.168.23.1/webbasedevaluationsystem/public/api/notification.json";
	// JSON Node Names
	private static final String TAG_USER = "user";
	private static final String TAG_USERNAME = "username";
	private static final String TAG_DECRYPTPASS = "decrypted_pass";
	private static final String TAG_EMAIL = "email";
	private static final String TAG_CONTACT = "contact_num";
	private static final String TAG_CREATEDATE = "created_at";

	private static final String TAG_NOTIF = "notif";
	private static final String TAG_SUBJID = "subj_id";
	private static final String TAG_STATUS = "status";
	private static final String TAG_SUBJCODE = "subj_code";
	private static final String TAG_CNTACT = "contact_num";
	private static final String TAG_CREATEDAT = "created_at";

	JSONArray user = null;
	JSONArray notif = null;

	TextView username1;
	TextView decryptpass1;
	TextView email1;
	TextView contact_no1;
	ResponseReceiver receiver;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		username1 = (TextView) findViewById(R.id.username);
		decryptpass1 = (TextView) findViewById(R.id.decryptpass);
		email1 = (TextView) findViewById(R.id.email);
		contact_no1 = (TextView) findViewById(R.id.contact_no);
		
		IntentFilter filter = new IntentFilter(ResponseReceiver.ACTION_RESP);
		filter.addCategory(Intent.CATEGORY_DEFAULT);
		receiver = new ResponseReceiver();
		registerReceiver(receiver, filter);
		
		Intent intent = new Intent(this, SmsDistributorService.class);
		startService(intent);
		
		/*new ParseUser().execute();// Execute the AsyncTask of ParseUser
		new ParseEval().execute();// Execute the AsyncTask of ParseEval
*/
	}// end of onCreate

	// AsyncTask Classes
	public class ParseUser extends AsyncTask<Void, Void, JSONObject> {
		JSONObject c = null;

		@Override
		protected JSONObject doInBackground(Void... params) {
			// TODO Auto-generated method stub

			ServiceHandler serviceHandler = new ServiceHandler();
			String jsonResponse = serviceHandler
					.makeServiceCall(studentsUrl, 1);
			JSONObject json = null;

			try {
				json = new JSONObject(jsonResponse);
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			Log.e("JSON CONTENT", json.toString());

			try {
				// Getting JSON Array
				user = json.getJSONArray(TAG_USER);

			} catch (JSONException e) {
				e.printStackTrace();
			}

			return c;
		}

		@Override
		protected void onPostExecute(JSONObject c) {
			// TODO Auto-generated method stub
			super.onPostExecute(c);

			String username = "";
			String decryptpass = "";
			String email = "";
			String contact_num = "";
			String created_date = "";

			try {
				int size = user.length();

				for (int i = 0; i < size; i++) {

					c = user.getJSONObject(i);

					username = c.getString(TAG_USERNAME);
					decryptpass = c.getString(TAG_DECRYPTPASS);
					email = c.getString(TAG_EMAIL);
					contact_num = c.getString(TAG_CONTACT);
					created_date = c.getString(TAG_CREATEDATE);

					Log.e("CONTENT" + i, username + " " + decryptpass + " "
							+ email + " " + contact_num + "" + created_date);

					sendSMSMessage(contact_num);// function call for the SMS

				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			/*username1.setText(username);
			decryptpass1.setText(decryptpass);
			email1.setText(email);
			contact_no1.setText(contact_num);*/
		}
	}// end of ParseUsers Class
	
	

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(receiver);
	}

	public class ParseEval extends AsyncTask<Void, Void, JSONObject> {
		JSONObject x = null;

		@Override
		protected JSONObject doInBackground(Void... params) {
			// TODO Auto-generated method stub

			ServiceHandler serviceHandler = new ServiceHandler();
			String jsonResponse = serviceHandler.makeServiceCall(evaluationUrl,
					1);
			JSONObject json1 = null;
			try {
				json1 = new JSONObject(jsonResponse);
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			Log.e("JSON CONTENT", json1.toString());

			try {
				// Getting JSON Array
				notif = json1.getJSONArray(TAG_NOTIF);

			} catch (JSONException e) {
				e.printStackTrace();
			}

			return x;
		}

		@Override
		protected void onPostExecute(JSONObject x) {
			// TODO Auto-generated method stub
			super.onPostExecute(x);

			String subj_id = "";
			String status = "";
			String subj_code = "";
			String cntct_num = "";
			String created_at = "";

			try {
				int size = notif.length();

				for (int i = 0; i < size; i++) {

					x = notif.getJSONObject(i);

					subj_id = x.getString(TAG_SUBJID);
					status = x.getString(TAG_STATUS);
					subj_code = x.getString(TAG_SUBJCODE);
					cntct_num = x.getString(TAG_CNTACT);
					created_at = x.getString(TAG_CREATEDAT);

					Log.e("CONTENT" + i, subj_id + " " + status + " "
							+ subj_code + " " + cntct_num + " " + created_at);

					// sendSMSMessage(contact_num);// function call for the
					// SMS
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	// Function for SMS Manager
	protected void sendSMSMessage(String contact_num) {
		Log.i("Send SMS", "");
		String message = "You are already Evaluated";

		try {
			SmsManager smsManager = SmsManager.getDefault();
			smsManager.sendTextMessage(contact_num, null, message, null, null);
//			Toast.makeText(getApplicationContext(), contact_num,
//					Toast.LENGTH_LONG).show();
		} catch (Exception e) {
//			Toast.makeText(getApplicationContext(),
//					"SMS faild, please try again.", Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
	}

	public class ResponseReceiver extends BroadcastReceiver {
		public static final String ACTION_RESP = "com.example.service.MESSAGE_PROCESSED";
		
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			Log.e("ACTION", "SEND");
			/*new ParseUser().execute();// Execute the AsyncTask of ParseUser
			new ParseEval().execute();// Execute the AsyncTask of ParseEval
*/		}

	}

}
