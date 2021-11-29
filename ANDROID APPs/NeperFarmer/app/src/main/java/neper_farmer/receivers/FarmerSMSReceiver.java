package sih.cvrce.neper_farmer.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.SmsMessage;

public class FarmerSMSReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        SmsMessage[] smsm = null;
        String sms_str ="";

        if (bundle != null)
        {
            // Get the SMS message
            Object[] pdus = (Object[]) bundle.get("pdus");
            smsm = new SmsMessage[pdus.length];
            for (int i=0; i<smsm.length; i++){
                smsm[i] = SmsMessage.createFromPdu((byte[])pdus[i]);

                //Check here sender is ours
                String Sender = smsm[i].getOriginatingAddress();
                if(Sender.equalsIgnoreCase("VK-TFCTOR")) {
                    sms_str += "\r\n";
                    sms_str += smsm[i].getMessageBody().substring(0, 4).toString();
                    sms_str += "\r\n";

                    Intent smsIntent = new Intent("otp");
                    smsIntent.putExtra("message",sms_str);

                    LocalBroadcastManager.getInstance(context).sendBroadcast(smsIntent);
                }
            }
        }
    }
}