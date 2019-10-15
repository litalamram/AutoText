package com.example.lital.autotext.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import com.example.lital.autotext.db.DbCursor;
import com.example.lital.autotext.db.DbMethods;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        long messageID = intent.getLongExtra(AlarmSetter.MESSAGE_ID, 0);
        //get the message details
        DbCursor c = DbMethods.getMessage(messageID);
        String message = c.getMessageTxt();
        String phone = c.getMessagePhoneNum();
        c.close();
        //send SMS
        try {
            SmsManager smgr = SmsManager.getDefault();
            smgr.sendTextMessage(phone, null, message, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

