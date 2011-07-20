/**
 * 
 */
package net.wisestone.installer.service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import net.wisestone.android.util.Lists;
import net.wisestone.installer.provider.ApplicationPackage;
import net.wisestone.installer.provider.ApplicationPackage.Packages;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.RemoteException;
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

    private File mExternalCacheDir;

    private ContentResolver mResolver;

    /**
     * @param context
     * @param resolver
     */
    public ApplicationPackageScanner(Context context, ContentResolver resolver) {
        mContext = context;
        mPackageManager = mContext.getPackageManager();
        mExternalCacheDir = mContext.getExternalCacheDir();
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
        if (LOGV) {
            Log.d(TAG, "internalScan(dir=" + dir.getAbsolutePath() + ")");
        }
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

                ContentProviderOperation o = parse(f);
                if (o != null)
                    batch.add(o);
            }
        }
        
        try {
            mResolver.applyBatch(ApplicationPackage.CONTENT_AUTHORITY, batch);
        } catch (RemoteException e) {
            throw new RuntimeException("Problem applying batch operation", e);
        } catch (OperationApplicationException e) {
            throw new RuntimeException("Problem applying batch operation", e);
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
        String packageName = pi.packageName;

        // get package version
        int versionCode = pi.versionCode;
        String versionName = pi.versionName;

        // generate package id
        String packageId = packageName + "_" + versionCode;

        // get package resources
        AssetManager assetManager = new AssetManager();
        assetManager.addAssetPath(archiveFilePath);
        Resources pkgRes = new Resources(assetManager, mContext.getResources().getDisplayMetrics(),
                mContext.getResources().getConfiguration());

        // get package label
        CharSequence label = null;
        if (pi.applicationInfo.labelRes != 0) {
            try {

                label = pkgRes.getText(pi.applicationInfo.labelRes);
            } catch (Resources.NotFoundException e) {
                // ignore. do nothing.
            }
        }

        if (label == null) {
            label = (pi.applicationInfo.nonLocalizedLabel != null) ? pi.applicationInfo.nonLocalizedLabel
                    : pi.applicationInfo.packageName;
        }
        String packageLabel = label.toString();

        // get package icon resource
        File packageIcon = new File(mExternalCacheDir, packageId);
        if (packageIcon.exists() == false) {
            OutputStream out = null;
            try {
                out = new BufferedOutputStream(new FileOutputStream(packageIcon));
                Bitmap iconBitmap = BitmapFactory.decodeResource(pkgRes, pi.applicationInfo.icon);
                iconBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            } catch (IOException e) {
            } finally {
                try {
                    if (out != null) {
                        out.close();
                        out = null;
                    }
                } catch (IOException e) {
                }
            }
        }

        if (LOGV) {
            Log.d(TAG, "  - package id: " + packageId);
            Log.d(TAG, "  - package name: " + packageName);
            Log.d(TAG, "  - package label: " + packageLabel);
            Log.d(TAG, "  - package version code: " + versionCode);
            Log.d(TAG, "  - package version name: " + versionName);
            Log.d(TAG, "  - package icon path: " + packageIcon.getAbsolutePath());
        }

        Uri packageUri = Packages.buildPackageUri(packageId);

        ContentProviderOperation.Builder builder = null;
        if (isRowExisting(packageUri, PackagesQuery.PROJECTION, mResolver)) {
            builder = ContentProviderOperation.newUpdate(packageUri);
            
        } else {
            builder = ContentProviderOperation.newInsert(Packages.CONTENT_URI);
            builder.withValue(Packages.PACKAGE_ID, packageId);
            builder.withValue(Packages.PACKAGE_NAME, packageName);
            builder.withValue(Packages.PACKAGE_LABEL, packageLabel);
            builder.withValue(Packages.PACKAGE_VERSIONCODE, versionCode);
            builder.withValue(Packages.PACKAGE_VERSIONNAME, versionName);
            builder.withValue(Packages.PACKAGE_FILE, archiveFilePath);
            builder.withValue(Packages.PACKAGE_ICON, packageIcon.getAbsolutePath());
        }
        return builder.build();
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

    private interface PackagesQuery
    {
        String[] PROJECTION = { Packages.PACKAGE_ID, Packages.PACKAGE_NAME };

        int PACKAGE_ID = 0;
        int PACKAGE_NAME = 1;
    }

}
