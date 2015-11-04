package com.mixxitevaluatecall.callreception;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.provider.CallLog;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.mixxitevaluatecall.R;
import com.mixxitevaluatecall.Utils;
import com.mixxitevaluatecall.R.string;
import com.mixxitevaluatecall.db.ContactsDB;
import com.mixxitevaluatecall.models.Contact;

public class EndCallReceiver extends BroadcastReceiver {

	static PhonecallStartEndDetector listener;
	String outgoingSavedNumber;
	protected Context savedContext;
	private static final ScheduledExecutorService worker = Executors
			.newSingleThreadScheduledExecutor();

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i("evaluate", "receiving");
		savedContext = context;
		if (listener == null) {
			listener = new PhonecallStartEndDetector();
		}

		if (intent.getAction()
				.equals("android.intent.action.NEW_OUTGOING_CALL")) {
			Log.i("evaluate", "NEW_OUTGOING_CALL");
			listener.setOutgoingNumber(intent.getExtras().getString(
					"android.intent.extra.PHONE_NUMBER"));
			return;
		}

		TelephonyManager telephony = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		telephony.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
	}

	private void onIncomingCallEnded(String number, Date start, Date end) {
		Log.i("evaluate", "onIncomingCallEnded");
		if (start != null && start.getTime() < end.getTime())
			getCallDetails(start.getTime());
	}

	private void onOutgoingCallEnded(String number, Date start, Date end) {
		Log.i("evaluate", "onOutgoingCallEnded");
		if (start != null && start.getTime() < end.getTime())
			getCallDetails(start.getTime());

	}

	public class PhonecallStartEndDetector extends PhoneStateListener {
		int lastState = TelephonyManager.CALL_STATE_IDLE;
		Date callStartTime;
		boolean isIncoming;
		String savedNumber;

		public PhonecallStartEndDetector() {
		}

		public void setOutgoingNumber(String number) {
			savedNumber = number;
		}

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			super.onCallStateChanged(state, incomingNumber);

			if (lastState == state) {
				return;
			}
			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING:
				isIncoming = true;
				savedNumber = incomingNumber;
				Log.i("evaluate", "CALL_STATE_RINGING");
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK:
				Log.i("evaluate", "CALL_STATE_OFFHOOK");
				callStartTime = new Date();
				break;
			case TelephonyManager.CALL_STATE_IDLE:
				Log.i("evaluate", "CALL_STATE_IDLE");
				if (isIncoming) {
					onIncomingCallEnded(savedNumber, callStartTime, new Date());
				} else {
					onOutgoingCallEnded(savedNumber, callStartTime, new Date());
				}
				break;
			}
			lastState = state;
		}

	}

	public void getCallDetails(final Long time) {
		Log.i("evaluate", "getCallDetails");
		Runnable task = new Runnable() {
			public void run() {
				Cursor managedCursor;
				String[] mProjection = { CallLog.Calls.NUMBER,
						CallLog.Calls.TYPE, CallLog.Calls.DATE,
						CallLog.Calls.DURATION, CallLog.Calls.COUNTRY_ISO,

				};

				managedCursor = savedContext.getContentResolver().query(
						CallLog.Calls.CONTENT_URI, mProjection,
						CallLog.Calls.DATE + "> ?",
						new String[] { "" + (time - 50000) }, null);

				int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
				int duration = managedCursor
						.getColumnIndex(CallLog.Calls.DURATION);
				int numberIndex = managedCursor
						.getColumnIndex(CallLog.Calls.NUMBER);

				int callDuration = 0;

				boolean isUnswared = false;
				String Number = null;
				Log.i("evaluate",
						"managedCursor size" + managedCursor.getCount());
				while (managedCursor.moveToNext()) {

					callDuration = managedCursor.getInt(duration);
					String callDate = managedCursor.getString(date);
					Number = managedCursor.getString(numberIndex);
					if (callDuration > 0)
						isUnswared = true;

				}

				int minutes = callDuration / 60;
				int seconds = callDuration % 60;

				managedCursor.close();
				if (isUnswared) {
					// Log.i("evaluate", "isUnswared true");
					if (callDuration > 0) {
						if (minutes == 0)
							manageShow(Number, seconds + " sec");
						else
							manageShow(Number, minutes + " min " + seconds
									+ " sec");
					}
				}
				// else
				// Log.i("evaluate", "isUnswared false");
			}
		};
		worker.schedule(task, 2, TimeUnit.SECONDS);

	}

	private void manageShow(String number, String callLength) {
		

		int state = -1;
		ContactsDB db = ContactsDB.getInstance(savedContext);
		String name = Utils.getContactName(savedContext, number);

		if (name != null) { // Number existe in contacts
			Log.i("EvaluateCall", "Number existe in contacts");
			Contact contact = db.getContact(Utils.getIdContact(savedContext,
					number));
			

			if (contact == null) {// contact not exist in db
				state = 1;

			} else {
				if (contact.getColor() == -1)  //contact exist but still have no color
					state = 1;
				else {
					if (contact.getType() == -1) //contact exist but still have no type ( pro/perso)
						state = 2;
					else {
						if (contact.getIdCat() == -1) //contact exist but still have no category
							state = 3;
						else {
							boolean isMsgv = db.IsContactMsgv(
									contact.getIdContact(),
									savedContext.getString(R.string.cat_msgv));
							if (isMsgv) //contact exist but still in MSGV
								state = 4;
						}
					}
				}

			}
		} else {
			//Number not exist in contacts
			state = 0;
		}
Log.i("Evaluate", "State="+state);
		if (state >= 0) {
			Intent newIntent = new Intent(savedContext, MainActivity.class);
			newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			newIntent.putExtra("number", number);
			newIntent.putExtra("call_length", callLength);
			newIntent.putExtra("state", state);
			savedContext.startActivity(newIntent);
		}
		else
		{Log.i("Evaluate", "else");
			Intent newIntent = new Intent(savedContext, EvaluateActivity.class);
			newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			savedContext.startActivity(newIntent);
		}
	}

}