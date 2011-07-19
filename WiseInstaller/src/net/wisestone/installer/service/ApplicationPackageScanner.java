/**
 * 
 */
package net.wisestone.installer.service;

import java.io.File;
import java.util.ArrayList;

import net.wisestone.android.util.Lists;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

/**
 * @author exia2
 *
 */
public class ApplicationPackageScanner
{

    private static final String TAG = "ApplicationPackageScanner";

    private static final boolean LOGV = true;

    private Context mContext;

    private PackageManager mPackageManager;

    private ContentResolver mResolver;

    /**
     * @param context
     * @param resolver
     */
    public ApplicationPackageScanner(Context context, ContentResolver resolver) {
        mContext = context;
        mPackageManager = mContext.getPackageManager();
        mResolver = resolver;
    }

    public void scan(File dir) {
        if (LOGV) {
            Log.d(TAG, "scan(dir=" + dir.getAbsolutePath() + ")");
        }
        final ArrayList<ContentProviderOperation> batch = Lists.newArrayList();
        internalScan(dir, batch);

    }

    private void internalScan(File dir, ArrayList<ContentProviderOperation> batch) {
        assertExists(dir);
        assertDirectory(dir);

        File[] fileList = dir.listFiles();
        for (int i = 0, l = fileList.length; i < l; i++) {
            File f = fileList[i];
            if (f.isDirectory()) {
                internalScan(f, batch);
            } else {
                String filename = f.getName().toLowerCase();
                if (filename.endsWith(".apk") == false) {
                    continue;
                }

                if (LOGV) {
                    Log.d(TAG, "  - find android package file -->> " + f.getName());
                }
                batch.add(parse(f));
            }
        }
    }

    private ContentProviderOperation parse(File f) {
        String archiveFilePath = f.getAbsolutePath();
        if (LOGV) {
            Log.d(TAG, "parse(file=" + archiveFilePath + ")");
        }

        PackageInfo pi = mPackageManager.getPackageArchiveInfo(archiveFilePath,
                PackageManager.GET_UNINSTALLED_PACKAGES);

        // get package name
        String packageId = pi.packageName;

        // get package version
        int versionCode = pi.versionCode;
        String verionName = pi.versionName;

        // get package label
        CharSequence label = null;
        if (pi.applicationInfo.labelRes != 0) {
            try {
                AssetManager assetManager = new AssetManager();
                assetManager.addAssetPath(archiveFilePath);
                Resources pkgRes = new Resources(assetManager, mContext.getResources()
                        .getDisplayMetrics(), mContext.getResources().getConfiguration());
                label = pkgRes.getText(pi.applicationInfo.labelRes);
            } catch (Resources.NotFoundException e) {
                // ignore. do nothing.
            }
        }

        if (label == null) {
            label = (pi.applicationInfo.nonLocalizedLabel != null) ? pi.applicationInfo.nonLocalizedLabel
                    : pi.applicationInfo.packageName;
        }
        String packageName = label.toString();
        
        // get package icon resource
        

        ContentProviderOperation.Builder builder;
        return null;
    }

    private void assertExists(File dir) {
        if (dir.exists() == false)
            throw new IllegalArgumentException("The dir must exist.");
    }

    private void assertDirectory(File dir) {
        if (dir.isFile())
            throw new IllegalArgumentException("The dir must be directory.");
    }

    protected boolean isRowExisting(Uri uri, String[] projection, ContentResolver resolver) {
        final Cursor cursor = resolver.query(uri, projection, null, null, null);
        try {
            if (!cursor.moveToFirst())
                return false;
        } finally {
            cursor.close();
        }
        return true;
    }

}
