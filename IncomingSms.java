package com.android_examples.Connect2ControlHome;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

/**
 * Created by Asha.Devaraja on 11/10/2016.
 */
public class IncomingSms extends BroadcastReceiver {


    public static final String SMS_BUNDLE = "pdus";
    String otpTag = "Your Esp registration code is ";
    String sender = "+918861981132";
    int OtpStart;
    int OtpEnd;

    public void onReceive(Context context, Intent intent) {
        Bundle intentExtras = intent.getExtras();
        if (intentExtras != null) {
            Object[] sms = (Object[]) intentExtras.get(SMS_BUNDLE);
            String smsMessageStr = "";
            for (int i = 0; i < sms.length; ++i) {
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) sms[i]);

                String smsBody = smsMessage.getMessageBody().toString();
                String address = smsMessage.getOriginatingAddress();

                Log.i(this.getClass().getCanonicalName(), "SMS recvd : " + smsBody );

                if(address.equals(sender)&& smsBody.startsWith(otpTag))
                {
                    OtpStart = otpTag.length();
                    OtpEnd   = OtpStart + 6;

                    SystemClass appInstance = SystemClass.getInstance();
                    //appInstance.otpReceived(smsBody.substring(OtpStart, OtpEnd));
                }
            }

        }
    }
}
