/**
 * 
 */
package net.wisestone.installer.provider;

import net.wisestone.installer.provider.ApplicationPackage.Packages;
import net.wisestone.installer.provider.ApplicationPackageDatabase.Tables;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

/**
 * @author exia2
 *
 */
public class ApplicationPackageProvider extends ContentProvider
{

    private static final String TAG = "ApplicationPackageProvider";

    private static final boolean LOGV = Log.isLoggable(TAG, Log.VERBOSE);

    private ApplicationPackageDatabase mOpenHelper;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private static final int PACKAGES = 100;
    private static final int PACKAGES_STARRED = 101;
    private static final int PACKAGES_NEW = 102;
    private static final int PACKAGES_UPDATED = 103;
    private static final int PACKAGES_UPDATED_STARRED = 104;
    private static final int PACKAGES_SEARCH = 105;
    private static final int PACKAGES_AT = 106;
    private static final int PACKAGES_PARALLEL = 107;
    private static final int PACKAGES_NEXT = 108;
    private static final int PACKAGES_ID = 109;
    private static final int PACKAGES_ID_SPEAKERS = 110;
    private static final int PACKAGES_ID_SPEAKERS_ID = 111;
    private static final int PACKAGES_ID_NOTES = 112;
    private static final int PACKAGES_ID_TAGS = 113;
    private static final int PACKAGES_ID_TAGS_ID = 114;

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = ApplicationPackage.CONTENT_AUTHORITY;

        matcher.addURI(authority, "packages", PACKAGES);
        matcher.addURI(authority, "packages/new", PACKAGES_NEW);
        matcher.addURI(authority, "packages/updated", PACKAGES_UPDATED);
        matcher.addURI(authority, "packages/updated/starred", PACKAGES_UPDATED_STARRED);
        matcher.addURI(authority, "packages/search/*", PACKAGES_SEARCH);
        matcher.addURI(authority, "packages/at/*", PACKAGES_AT);
        matcher.addURI(authority, "packages/parallel/*", PACKAGES_PARALLEL);
        matcher.addURI(authority, "packages/next/*", PACKAGES_NEXT);
        matcher.addURI(authority, "packages/*", PACKAGES_ID);
        matcher.addURI(authority, "packages/*/speakers", PACKAGES_ID_SPEAKERS);
        matcher.addURI(authority, "packages/*/speakers/*", PACKAGES_ID_SPEAKERS_ID);
        matcher.addURI(authority, "packages/*/notes", PACKAGES_ID_NOTES);
        matcher.addURI(authority, "packages/*/tags", PACKAGES_ID_TAGS);
        matcher.addURI(authority, "packages/*/tags/*", PACKAGES_ID_TAGS_ID);

        return matcher;
    }

    /* (non-Javadoc)
     * @see android.content.ContentProvider#onCreate()
     */
    @Override
    public boolean onCreate() {
        return false;
    }

    /* (non-Javadoc)
     * @see android.content.ContentProvider#query(android.net.Uri, java.lang.String[], java.lang.String, java.lang.String[], java.lang.String)
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
            String sortOrder) {
        return null;
    }

    /* (non-Javadoc)
     * @see android.content.ContentProvider#insert(android.net.Uri, android.content.ContentValues)
     */
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        if (LOGV) {
            Log.v(TAG, "insert(uri=" + uri + ", values=" + values.toString() + ")");
        }
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PACKAGES: {
                db.insertOrThrow(Tables.PACKAGES, null, values);
                return Packages.buildPackageUri(values.getAsString(Packages.PACKAGE_ID));
            }
            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
    }

    /* (non-Javadoc)
     * @see android.content.ContentProvider#delete(android.net.Uri, java.lang.String, java.lang.String[])
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    /* (non-Javadoc)
     * @see android.content.ContentProvider#update(android.net.Uri, android.content.ContentValues, java.lang.String, java.lang.String[])
     */
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (LOGV) Log.v(TAG, "update(uri=" + uri + ", values=" + values.toString() + ")");
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        return 0;
    }

    /* (non-Javadoc)
     * @see android.content.ContentProvider#getType(android.net.Uri)
     */
    @Override
    public String getType(Uri uri) {
        return null;
    }

}
