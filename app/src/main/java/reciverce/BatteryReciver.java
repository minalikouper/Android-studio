package reciverce;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.checkbatter.services.BatteryNotificationService;

public class BatteryReciver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent serviceIntent = new Intent(context, BatteryNotificationService.class);
        // Start service
        context.startService(serviceIntent);
        String action = serviceIntent.getAction();
        if (action.equals("NOW_BATTERY")) {
            int level = serviceIntent.getExtras().getInt("NOW_BATTERY");
            Log.i("battery", "data res. " + level);
        }

    }
}
