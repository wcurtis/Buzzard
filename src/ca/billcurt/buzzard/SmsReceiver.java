package ca.billcurt.buzzard;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.telephony.SmsMessage;
import android.util.Log;

public class SmsReceiver extends BroadcastReceiver {

	private static final String TAG = "SmsReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {

		List<SmsMessage> messages = getSmsMessages(intent);
		
		for (SmsMessage message : messages) {
			
			String from = message.getOriginatingAddress();
			if (from.contains(("5192779678"))) {
				Log.v("Message from Bill! (%s)", from);
				playBillSound(context);
			} 

			doDefaultVibration(context);
		}
	}

	/**
	 * Vibrates the phone with the default setting
	 * @param context
	 */
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
	
	/**
	 * Hopefully creep jimmy out when I text him
	 * @param context
	 */
	private void playBillSound(Context context) {
		playAudioFile(context, R.raw.billvoice);
	}
	
	private void playAudioFile(Context context, int resource) {
		MediaPlayer mp = MediaPlayer.create(context, resource);
	    mp.start();
	}
	
	/**
	 * Extract SMS data from intent
	 * @param intent
	 * @return list of SmsMessages
	 */
	private List<SmsMessage> getSmsMessages(Intent intent) {
		Bundle bundle = intent.getExtras();
		ArrayList<SmsMessage> messages = new ArrayList<SmsMessage>();

		if (bundle != null) {
			Object[] pdus = (Object[]) bundle.get("pdus");
			for (int i = 0; i < pdus.length; i++) {
				messages.add(SmsMessage.createFromPdu((byte[]) pdus[i]));
			}
			Log.v(TAG, "Received " + pdus.length + " messages.");
		}
		
		return messages;
	}

}
