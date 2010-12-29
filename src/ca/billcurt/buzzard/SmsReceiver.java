package ca.billcurt.buzzard;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class SmsReceiver extends BroadcastReceiver {

	private static final String TAG = "SmsReceiver";

	public SmsReceiver() {

	}

	@Override
	public void onReceive(Context context, Intent intent) {

		doDefaultVibration(context);
		
	}

	private void doDefaultVibration(Context context) {
		
		Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
		
		if (vibrator == null) {
			Log.v(TAG, "Vibrator Service is null.");
			return;
		}
		
		long milliseconds = 1000;
		vibrator.vibrate(milliseconds);

		// Vibrate in a Pattern with 500ms on, 500ms off for 5 times
		// long[] pattern = { 500, 300 };
		// v.vibrate(pattern, 5);

	}

	private void toastMessage(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();
		SmsMessage[] msgs = null;
		String str = "";

		if (bundle != null) {
			Object[] pdus = (Object[]) bundle.get("pdus");
			msgs = new SmsMessage[pdus.length];
			for (int i = 0; i < msgs.length; i++) {
				msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
				str += "SMS from " + msgs[i].getOriginatingAddress();
				str += " :";
				str += msgs[i].getMessageBody().toString();
				str += "\n";
			}

			Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
		}
	}

}
