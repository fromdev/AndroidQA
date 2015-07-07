/**
 * 
 */
package com.fromdev.android.parser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.fromdev.android.configuration.Global;

import android.content.Context;
import android.util.Log;

/**
 * @author kamran
 *
 */
public class Decompress {
	private InputStream _zipFile;
	private Context mContext;
	private String outputLocatoin;
	// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================

		// ===========================================================
		// Constructors
		// ===========================================================

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		// ===========================================================
		// Methods for/from SuperClass/Interfaces
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================

	public Decompress(InputStream zipFile, Context mContext,
			String outputLocation) {
		_zipFile = zipFile;
		this.mContext = mContext;

		this.outputLocatoin = outputLocation;

		// _dirChecker("");
	}

	public void unzip() {
		ZipInputStream zin  = null;
		
		try {
			// FileInputStream fin = new FileInputStream(_zipFile);
			zin = new ZipInputStream(_zipFile);
			ZipEntry ze = null;
			while ((ze = zin.getNextEntry()) != null) {
				Log.v("Decompress", "Unzipping " + ze.getName());

				FileOutputStream fout = null;
				try {
				if (!ze.isDirectory()
						&& ze.getName().equalsIgnoreCase("qaconfig.json")) {
					dirchecker(outputLocatoin);

					fout = new FileOutputStream(
							mContext.getFilesDir() + "/" + outputLocatoin + "/"

							+ ze.getName());
					for (int c = zin.read(); c != -1; c = zin.read()) {
						fout.write(c);
					}

					zin.closeEntry();
					
				} else {

				}
				} finally {
					if(fout!=null) {
						try {
							fout.close();
						}catch(Exception e) {
							Global.getInstance().setLastException(e);
							e.printStackTrace();
						}
					}
				}

			}
			
		} catch (Exception e) {
			Global.getInstance().setLastException(e);
			Log.e("Decompress", "unzip", e);
		} finally {
			try {
				if(zin!=null) {
					zin.close();
				}
			} catch (Exception e) {
				Global.getInstance().setLastException(e);
				e.printStackTrace();
			}
		}
	}

	private void dirchecker(String dir) {

		File folder = new File(mContext.getFilesDir() + "/" + dir);
		boolean success = true;
		if (!folder.exists()) {
			success = folder.mkdir();
		}
		if (success) {
			// Do something on success
		} else {
			// Do something else on failure
		}
	}
	// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
}
