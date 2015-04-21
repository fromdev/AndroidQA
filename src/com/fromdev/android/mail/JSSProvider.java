package com.fromdev.android.mail;

import java.security.AccessController;
import java.security.Provider;

@SuppressWarnings("serial")

/**
 * @author kamran
 *
 */
public class JSSProvider extends Provider {

	public JSSProvider() {
		 super("HarmonyJSSE", 1.0, "Harmony JSSE Provider");
	        AccessController.doPrivileged(new java.security.PrivilegedAction<Void>() {
	            public Void run() {
	                put("SSLContext.TLS",
	                        "org.apache.harmony.xnet.provider.jsse.SSLContextImpl");
	                put("Alg.Alias.SSLContext.TLSv1", "TLS");
	                put("KeyManagerFactory.X509",
	                        "org.apache.harmony.xnet.provider.jsse.KeyManagerFactoryImpl");
	                put("TrustManagerFactory.X509",
	                        "org.apache.harmony.xnet.provider.jsse.TrustManagerFactoryImpl");
	                return null;
	            }
	        });
	}
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
