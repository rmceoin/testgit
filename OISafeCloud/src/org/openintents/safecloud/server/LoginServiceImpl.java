package org.openintents.safecloud.server;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Logger;

import org.openintents.safecloud.client.service.LoginService;
import org.openintents.safecloud.shared.SafeLogin;
import org.openintents.safecloud.shared.SafeLoginStatus;
import org.openintents.safecloud.shared.SafeLoginStatus.LoginStatus;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class LoginServiceImpl extends RemoteServiceServlet implements
		LoginService {

	static {
		ObjectifyService.register(SafeLogin.class);
	}

	public SafeLoginStatus loginServer(String input) throws IllegalArgumentException, IOException {
		
		final Logger log = Logger.getLogger(LoginServiceImpl.class.getName());
		log.info("loginServer");
		
//		log.info("objectify services started");

//		log.info("begin objectify");
		Objectify ofy = ObjectifyService.begin();
//		log.info("objectify has begun");
		
        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();

		String nickname="";
		if (user!=null) {
			nickname=user.getNickname();
		} else {
			// user is not logged in
			log.info("user not logged in");
			SafeLoginStatus status=new SafeLoginStatus(LoginStatus.NOT_LOGGED_IN);
			status.setLoginURL(userService.createLoginURL("/"));
			return status;
//			return "<a href=\"" + userService.createLoginURL("/") + "\">Login</a>";
		}
		log.info("user logged in as: "+nickname);
		
		SafeLogin login = ofy.query(SafeLogin.class).filter("name", nickname).get();
		if ((login==null) || (login.getId()==null)) {
			log.info("no user found");
			SafeLogin newLogin = new SafeLogin(nickname);
			ofy.put(newLogin);
			assert newLogin.getId() != null;

			log.info("created login id="+newLogin.getId());
			login = newLogin;
			login.setTimesLoggedIn(1);
		} else {
			log.info("found login id="+login.getId());
			login.setTimesLoggedIn(login.getTimesLoggedIn()+1);
		}
		String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";

		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
		String currentTime=sdf.format(cal.getTime());
		login.setLastLogin(currentTime);
		log.info("updated lastLogin="+currentTime);

		String ip = getThreadLocalRequest().getRemoteAddr();
		login.setLastIpAddress(ip);
		login.setEmail(user.getEmail());
		login.setUserId(user.getUserId());
		login.setAuthDomain(user.getAuthDomain());
		
		ofy.put(login);

		SafeLoginStatus status=new SafeLoginStatus(LoginStatus.LOGGED_IN);
		status.setLoginStatus(LoginStatus.LOGGED_IN);
		status.setSafeLogin(login);
		status.setLogoutURL(userService.createLogoutURL("/"));
		status.setLoginURL(userService.createLoginURL("/"));
//		return "<a href=\"" + userService.createLogoutURL("/") + "\">Logout</a>";
		return status;
	}

}
