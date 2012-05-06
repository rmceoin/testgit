package org.openintents.safecloud.client.service;

import java.util.ArrayList;

import org.openintents.safecloud.shared.JobCategory;
import org.openintents.safecloud.shared.SafeCategory;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>CategoryService</code>.
 */
public interface CategoryServiceAsync {
	void categoryServer(JobCategory job, AsyncCallback<JobCategory> callback);

	void getCategories(AsyncCallback<ArrayList<SafeCategory>> callback);
	
	void deleteCategory(Long id, AsyncCallback<Boolean> callback);

	void getCategory(Long id, AsyncCallback<SafeCategory> callback);

	void updateCategory(SafeCategory category, AsyncCallback<SafeCategory> callback);

}
