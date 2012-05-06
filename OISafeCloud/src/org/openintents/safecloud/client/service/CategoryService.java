package org.openintents.safecloud.client.service;

import java.io.IOException;
import java.util.ArrayList;

import org.openintents.safecloud.shared.JobCategory;
import org.openintents.safecloud.shared.SafeCategory;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("category")
public interface CategoryService extends RemoteService {
	JobCategory categoryServer(JobCategory job) throws IllegalArgumentException, IOException;
	
	ArrayList<SafeCategory> getCategories();

	Boolean deleteCategory(Long id);

	SafeCategory getCategory(Long id);

	SafeCategory updateCategory(SafeCategory category);
}
