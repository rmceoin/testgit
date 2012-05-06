package org.openintents.safecloud.client;

import org.openintents.safecloud.client.presenter.CategoryListPresenter;
import org.openintents.safecloud.client.service.CategoryService;
import org.openintents.safecloud.client.service.CategoryServiceAsync;
import org.openintents.safecloud.client.service.LoginService;
import org.openintents.safecloud.client.service.LoginServiceAsync;
import org.openintents.safecloud.client.view.CategoryListView;
import org.openintents.safecloud.shared.SafeLoginStatus;
import org.openintents.safecloud.shared.SafeLoginStatus.LoginStatus;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class OISafeCloud implements EntryPoint {

	@UiField VerticalPanel categoryListPanel;
	@UiField ScrollPanel mainPanel;
	@UiField HeaderPanel headerPanel;

	RootLayoutPanel root;
	
	private static OISafeCloud singleton;

	private final LoginServiceAsync loginService = GWT
		.create(LoginService.class);
	private SimpleEventBus eventBus = new SimpleEventBus();


	private static LoginStatus statusLogin = LoginStatus.NOT_LOGGED_IN;
	
	interface OISafeCloudUiBinder extends UiBinder<DockLayoutPanel, OISafeCloud> {}

	private static final OISafeCloudUiBinder binder = GWT.create(OISafeCloudUiBinder.class);

	private CategoryListPresenter categoryListPresenter;
	
	/**
	 * Gets the singleton application instance.
	 * @return OISafeCloud singleton instance
	 */
	public static OISafeCloud get() {
		return singleton;
	}
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {

		singleton = this;
		
		RootPanel appPanel = RootPanel.get("loading");
		appPanel.setVisible(false);

	    DockLayoutPanel outer = binder.createAndBindUi(this);

	    root = RootLayoutPanel.get();
	    root.clear();
	    root.add(outer);
	    
	    login();
	}
	
	private void login() {
		loginService.loginServer("",
				new AsyncCallback<SafeLoginStatus>() {
					public void onFailure(Throwable caught) {
						// Show the RPC error message to the user
/*						RootPanel rootPanel = RootPanel.get("loginContainer");
						final HTML loginLabel = new HTML();
						loginLabel.setHTML("Server error: "+caught.getLocalizedMessage());
						rootPanel.clear();
						rootPanel.add(loginLabel);
						*/
					}

					public void onSuccess(SafeLoginStatus result) {
						if (result==null) {
						} else {
							if (result.getLoginStatus().equals(LoginStatus.LOGGED_IN)) {
								String nickname=result.getSafeLogin().getName();
								headerPanel.loginLogout.setHTML("Logout");
								headerPanel.loginLogout.setHref(result.getLogoutURL());
								headerPanel.username.setInnerText(nickname);
								headerPanel.welcomeMsg.setInnerText("Welcome:");
							} else {
								headerPanel.loginLogout.setHTML("Login");
								headerPanel.loginLogout.setHref(result.getLoginURL());
							}
							statusLogin = result.getLoginStatus();
						}
						if (statusLogin==LoginStatus.LOGGED_IN) {
							goAfterLogin();
						}
					}
				});

	}

	private void goAfterLogin() {
		CategoryServiceAsync categoryService = GWT.create(CategoryService.class);

		categoryListPresenter = new CategoryListPresenter(categoryService, eventBus, new CategoryListView());
		categoryListPresenter.go(categoryListPanel);

		AppController appViewer = new AppController(categoryService, eventBus);
		appViewer.go();
	}
	
	public SimpleEventBus getEventBus() {
		return eventBus;
	}

	  /**
	   * @return the mainPanel
	   */
	  ScrollPanel getMainPanel() {
	    return mainPanel;
	  }

}
