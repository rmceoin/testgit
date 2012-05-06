package org.openintents.safecloud.client.service;

import java.io.IOException;

import org.openintents.safecloud.shared.SafeLoginStatus;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("login")
public interface LoginService extends RemoteService {
	SafeLoginStatus loginServer(String name) throws IllegalArgumentException, IOException;
}
