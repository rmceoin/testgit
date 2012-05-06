package org.openintents.safecloud.shared;

import java.io.Serializable;

import javax.persistence.Id;

import com.googlecode.objectify.annotation.Entity;

@SuppressWarnings("serial")
@Entity
public class SafeLoginStatus implements Serializable {
	@Id Long id;
	public enum LoginStatus { UNKNOWN, NOT_LOGGED_IN, LOGGED_IN };
	private LoginStatus loginStatus;
	private SafeLogin safeLogin;
	private String loginURL;
	private String logoutURL;
	
	@SuppressWarnings("unused")
	private SafeLoginStatus()
	{
	}

	public SafeLoginStatus(LoginStatus status)
	{
		this.loginStatus=status;
	}

	public Long getId()
	{
		return this.id;
	}

	public LoginStatus getLoginStatus()
	{
		return this.loginStatus;
	}
	
	public void setLoginStatus(LoginStatus loginStatus)
	{
		this.loginStatus = loginStatus;
	}

	public SafeLogin getSafeLogin()
	{
		return this.safeLogin;
	}
	
	public void setSafeLogin(SafeLogin safeLogin)
	{
		this.safeLogin = safeLogin;
	}

	public String getLoginURL()
	{
		return this.loginURL;
	}
	
	public void setLoginURL(String loginURL)
	{
		this.loginURL = loginURL;
	}

	public String getLogoutURL()
	{
		return this.logoutURL;
	}
	
	public void setLogoutURL(String logoutURL)
	{
		this.logoutURL = logoutURL;
	}

}
