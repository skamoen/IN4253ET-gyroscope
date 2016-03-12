package nl.tudelft.superevilhackinglab;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by H2c on 12-Mar-16.
 */
public class BCReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            StaticVariables.isScreenOn = false;
            Log.d("cekscreen","screen off");
        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            StaticVariables.isScreenOn = true;
            Log.d("cekscreen","screen on");
        }
    }
}