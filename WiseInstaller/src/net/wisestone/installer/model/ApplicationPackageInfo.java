/*
 * (#) net.wisestone.installer.model.ApplicationPackageInfo.java
 * Created on 2011. 7. 18.
 */
package net.wisestone.installer.model;

import java.io.File;

import android.net.Uri;

/**
 * 
 * 
 * @author ms29.seo@gmail.com
 * @version 0.1
 */
public class ApplicationPackageInfo
{

    /**
     * package name
     */
    public String packageName;

    /**
     * application name
     */
    public String name;

    /**
     * version code
     */
    public int versionCode;

    /**
     * version number
     */
    public String versionName;

    /**
     * package status
     */
    public int status = -1;

    /**
     * package file
     */
    public File file;

    /**
     * default constructor
     */
    public ApplicationPackageInfo() {
    }

    /**
     * get file uri
     * 
     * @return file uri
     */
    public Uri getFileUri() {
        return Uri.fromFile(file);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
        StringBuilder buf = new StringBuilder("{ ");
        buf.append("packageName: \"" + packageName + "\"");
        buf.append(", name: \"" + name + "\"");
        buf.append(", versionCode: \"" + versionCode + "\"");
        buf.append(", versionName: \"" + versionName + "\"");
        buf.append(", status: \"" + status + "\"");
        buf.append(", file: \"" + file.getAbsolutePath() + "\"");
        buf.append(" }");
        return buf.toString();
    }

}
