/*
 * (#) net.wisestone.installer.provider.ApplicationPackage.java
 * Created on 2011. 7. 19. 
 */
package net.wisestone.installer.provider;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * 
 *
 * @author ms29.seo@hotmail.com
 * @version 0.1
 */
public final class ApplicationPackage
{

    interface PackagesColumns
    {
        /** Unique string identifying this package (equal package name). */
        String PACKAGE_ID = "package_id";
        String PACKAGE_NAME = "package_name";
        String PACKAGE_LABEL = "package_label";
        String PACKAGE_VERSIONCODE = "package_vercd";
        String PACKAGE_VERSIONNAME = "package_vernm";
        String PACKAGE_FILE = "package_file";
        String PACKAGE_ICON = "package_icon";
    }

    private static final String PATH_PACKAGES = "packages";

    public static final String CONTENT_AUTHORITY = "net.wisestone.android.apps.wiseinstaller";

    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static class Packages implements PackagesColumns, BaseColumns
    {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_PACKAGES).build();

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.wiseinstaller.package";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.wiseinstaller.package";

        public static final String PACKAGES_COUNT = "packages_count";

        public static final String DEFAULT_SORT = PackagesColumns.PACKAGE_LABEL + " ASC";
        
        public static Uri buildPackageUri(String packageId) {
            return CONTENT_URI.buildUpon().appendPath(packageId).build();
        }
        
        public static String getPackageId(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }

    private ApplicationPackage() {
    }

}
