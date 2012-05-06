package org.openintents.safecloud.shared;

import java.io.Serializable;

import javax.persistence.Id;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;

@SuppressWarnings("serial")
@Entity
public class SafeCategory implements Serializable {
	@Id Long id;
	private String name;
	private String userId;
	Key<SafeLogin> owner;
	
	public SafeCategory() {}

	public SafeCategory(String name)
	{
		this.name = name;
	}
	
	public Long getId()
	{
		return this.id;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}

	public String getUserId()
	{
		return this.userId;
	}
	
	public void setUserId(String userId)
	{
		this.userId = userId;
	}
}
