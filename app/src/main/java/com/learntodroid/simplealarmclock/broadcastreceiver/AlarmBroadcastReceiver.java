package com.learntodroid.simplealarmclock.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.telephony.SmsManager;
import android.widget.Toast;

import com.learntodroid.simplealarmclock.application.App;
import com.learntodroid.simplealarmclock.data.contact.Contact;
import com.learntodroid.simplealarmclock.data.contact.ContactRepository;
import com.learntodroid.simplealarmclock.emergency.OnResponseTimerFiredListener;
import com.learntodroid.simplealarmclock.service.AlarmService;
import com.learntodroid.simplealarmclock.service.EmergencyMessageSendingTimerService;
import com.learntodroid.simplealarmclock.service.EmergencyTextValueHolder;
import com.learntodroid.simplealarmclock.service.RescheduleAlarmsService;
import com.learntodroid.simplealarmclock.service.SavedTimeoutValueHolder;

import java.util.ArrayList;
import java.util.Calendar;

public class AlarmBroadcastReceiver extends BroadcastReceiver implements OnResponseTimerFiredListener {
    public static final String MONDAY = "MONDAY";
    public static final String TUESDAY = "TUESDAY";
    public static final String WEDNESDAY = "WEDNESDAY";
    public static final String THURSDAY = "THURSDAY";
    public static final String FRIDAY = "FRIDAY";
    public static final String SATURDAY = "SATURDAY";
    public static final String SUNDAY = "SUNDAY";
    public static final String RECURRING = "RECURRING";
    public static final String TITLE = "TITLE";

    private ContactRepository contactRepository;
    private String messageToSend;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (contactRepository == null) {
            App mApplication = (App) context.getApplicationContext();
            contactRepository = new ContactRepository(mApplication);
        }
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            String toastText = String.format("Alarm Reboot");
            Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show();
            startRescheduleAlarmsService(context);
        } else {
            String toastText = String.format("Alarm Received");
            Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show();
            if (!intent.getBooleanExtra(RECURRING, false) || alarmIsToday(intent)) {
                startAlarmService(context, intent);
            }
        }
    }

    @Override
    public void onTimeout() {
        sendEmergencyMessageToAllContacts();
    }

    private boolean alarmIsToday(Intent intent) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int today = calendar.get(Calendar.DAY_OF_WEEK);

        switch(today) {
            case Calendar.MONDAY: return intent.getBooleanExtra(MONDAY, false);
            case Calendar.TUESDAY: return intent.getBooleanExtra(TUESDAY, false);
            case Calendar.WEDNESDAY: return intent.getBooleanExtra(WEDNESDAY, false);
            case Calendar.THURSDAY: return intent.getBooleanExtra(THURSDAY, false);
            case Calendar.FRIDAY: return intent.getBooleanExtra(FRIDAY, false);
            case Calendar.SATURDAY: return intent.getBooleanExtra(SATURDAY, false);
            case Calendar.SUNDAY: return intent.getBooleanExtra(SUNDAY, false);
            default: return false;
        }
    }

    private void startAlarmService(Context context, Intent intent) {
        initEmergencyTimeout(context);
        Intent intentService = new Intent(context, AlarmService.class);
        intentService.putExtra(TITLE, intent.getStringExtra(TITLE));
        callServiceWithIntent(context, intentService);
    }

    private void startRescheduleAlarmsService(Context context) {
        Intent intentService = new Intent(context, RescheduleAlarmsService.class);
        callServiceWithIntent(context, intentService);
    }

    private void initEmergencyTimeout(Context context) {
        messageToSend = new EmergencyTextValueHolder(context).getMessageText();
        EmergencyMessageSendingTimerService.addTimeoutListener(this);
        EmergencyMessageSendingTimerService.startTimer(new SavedTimeoutValueHolder(context).getTimeoutInMilisec());
    }

    private void callServiceWithIntent(Context context, Intent intentService) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intentService);
        } else {
            context.startService(intentService);
        }
    }

    private void sendEmergencyMessageToAllContacts() {
        SmsManager smsManager = SmsManager.getDefault();
        ArrayList<String> dividedParts = smsManager.divideMessage(messageToSend);
        for (Contact contact: contactRepository.getContacts()) {
            smsManager.sendMultipartTextMessage(contact.getPhoneNumber(), null, dividedParts, null, null);
            System.out.println("SMS sent to contact " + contact.getContactName());
        }
    }
}
