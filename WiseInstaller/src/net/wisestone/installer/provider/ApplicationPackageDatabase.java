/*
 * (#) net.wisestone.installer.provider.ApplicationPackageDatabase.java
 * Created on 2011. 7. 18. 
 */
package net.wisestone.installer.provider;

import net.wisestone.installer.provider.ApplicationPackage.Packages;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * 
 *
 * @author ms29.seo@hotmail.com
 * @version 0.1
 */
public class ApplicationPackageDatabase extends SQLiteOpenHelper
{

    /**
     * Logging Tag String
     */
    private static final String TAG = "ApplicationPackageDatabase";

    /**
     * Database Name String
     */
    private static final String DATABASE_NAME = "apppkgs.db";

    // NOTE: carefully update onUpgrade() when bumping database versions to make
    // sure user data is saved.

    private static final int VER_LAUNCH = 1;

    /**
     * Database version
     */
    private static final int DATABASE_VERSION = VER_LAUNCH;

    interface Tables
    {
        String PACKAGES = "packages";
    }

    /**
     * @param context
     */
    public ApplicationPackageDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /* (non-Javadoc)
     * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
     */
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate()");

        db.execSQL("CREATE TABLE " + Tables.PACKAGES + " (" + Packages._ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT, " + Packages.PACKAGE_ID + " TEXT NOT NULL, "
                + Packages.PACKAGE_NAME + " TEXT NOT NULL, " + Packages.PACKAGE_VERSIONCODE
                + " INTEGER NOT NULL, " + Packages.PACKAGE_VERSIONNAME + " TEXT NOT NULL, "
                + Packages.PACKAGE_FILE + " TEXT NOT NULL, UNIQUE (" + Packages.PACKAGE_ID
                + ") ON CONFLICT REPLACE)");
    }

    /* (non-Javadoc)
     * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
     */
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "onUpgrade() from " + oldVersion + " to " + newVersion);
    }

}
