package org.openintents.safecloud.shared;

import java.io.Serializable;

import javax.persistence.Id;

import com.googlecode.objectify.annotation.Entity;

@SuppressWarnings("serial")
@Entity
public class SafeLogin implements Serializable {
	@Id Long id;
	private String userId;
	private String name;
	private String lastLogin;
	private String lastIpAddress;
	private String email;
	private String authDomain;
	private int timesLoggedIn;
	
	@SuppressWarnings("unused")
	private SafeLogin() {}

	public SafeLogin(String name)
	{
		this.name = name;
	}
	
	public Long getId()
	{
		return this.id;
	}

	public String getUserId()
	{
		return this.userId;
	}
	
	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	public String getName()
	{
		return this.name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}

	public String getLastLogin()
	{
		return this.lastLogin;
	}
	
	public void setLastLogin(String lastLogin)
	{
		this.lastLogin = lastLogin;
	}

	public String getLastIpAddress()
	{
		return this.lastIpAddress;
	}
	
	public void setLastIpAddress(String lastIpAddress)
	{
		this.lastIpAddress = lastIpAddress;
	}

	public String getEmail()
	{
		return this.email;
	}
	
	public void setEmail(String email)
	{
		this.email = email;
	}

	public String getAuthDomain()
	{
		return this.authDomain;
	}
	
	public void setAuthDomain(String authDomain)
	{
		this.authDomain = authDomain;
	}

	public int getTimesLoggedIn()
	{
		return this.timesLoggedIn;
	}
	
	public void setTimesLoggedIn(int timesLoggedIn)
	{
		this.timesLoggedIn = timesLoggedIn;
	}
}
