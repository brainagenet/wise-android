/*
 * (#) net.wisestone.installer.receiver.OnMediaMountedReceiver.java
 * Created on 2011. 7. 18.
 */
package net.wisestone.installer.receiver;

import net.wisestone.installer.service.ApplicationPackageScanService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * 
 * 
 * @author ms29.seo@gmail.com
 * @version 0.1
 */
public class OnMediaMountedReceiver extends BroadcastReceiver
{

    private static final String TAG = "OnMediaMountedReceiver";

    private static final boolean LOGV = true;

    /* (non-Javadoc)
     * @see android.content.BroadcastReceiver#onReceive(android.content.Context, android.content.Intent)
     */
    public void onReceive(Context context, Intent intent) {
        if (LOGV) {
            Log.d(TAG,
                    "onReceive(action=" + intent.getAction() + ", data=" + intent.getDataString()
                            + ")");
        }
        
        final Intent i = new Intent(context, ApplicationPackageScanService.class);
        context.startService(i);
    }

}
