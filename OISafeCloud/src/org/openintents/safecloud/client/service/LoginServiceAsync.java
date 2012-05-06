package org.openintents.safecloud.client.service;

import org.openintents.safecloud.shared.SafeLoginStatus;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface LoginServiceAsync {
	void loginServer(String input, AsyncCallback<SafeLoginStatus> callback)
			throws IllegalArgumentException;
}
