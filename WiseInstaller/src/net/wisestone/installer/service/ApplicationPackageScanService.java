/**
 * 
 */
package net.wisestone.installer.service;

import java.io.File;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.Intent;
import android.util.Log;

/**
 * @author exia2
 *
 */
public class ApplicationPackageScanService extends IntentService
{

    private static final String TAG = "ApplicationPackageScanService";

    private static final boolean LOGV = true;

    private ContentResolver mResolver;
    
    private ApplicationPackageScanner mScanner;
    
    /**
     * @param name
     */
    public ApplicationPackageScanService() {
        super(TAG);
    }
    
    public void onCreate() {
        super.onCreate();
        
        mResolver = getContentResolver();
        mScanner = new ApplicationPackageScanner(this, mResolver);
    }

    /* (non-Javadoc)
     * @see android.app.IntentService#onHandleIntent(android.content.Intent)
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        if (LOGV) {
            Log.d(TAG, "onHandleIntent(action=" + intent.getAction() + ")");
        }

        File scanBaseDir = new File("/mnt/sdcard/apps");
        if (scanBaseDir.exists() == false) {
            if (LOGV) {
                Log.d(TAG, "  - make directories for " + scanBaseDir.getAbsolutePath());
            }
            scanBaseDir.mkdirs();
        }
        
        mScanner.scan(scanBaseDir);
    }

}
