package org.openintents.safecloud.shared;

import java.io.Serializable;
import com.googlecode.objectify.annotation.Entity;

@SuppressWarnings("serial")
@Entity
public class JobCategory implements Serializable  {

	public enum Type { LIST, NEW };
	public enum Status { OK, ERROR };
	
	public Type type;
	public Status status;
	public SafeCategory[] safeCategory;
	
	public JobCategory() {}
	
	public JobCategory(Type type)
	{
		this.type=type;
	}
}
