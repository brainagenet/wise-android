/**
 * 
 */
package net.wisestone.installer.service;

import android.app.IntentService;
import android.content.Intent;

/**
 * @author exia2
 *
 */
public class ApplicationPackageScanService extends IntentService {

	/**
	 * @param name
	 */
	public ApplicationPackageScanService(String name) {
		super(name);
	}

	/* (non-Javadoc)
	 * @see android.app.IntentService#onHandleIntent(android.content.Intent)
	 */
	@Override
	protected void onHandleIntent(Intent intent) {
	}


}
