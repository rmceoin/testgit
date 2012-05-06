package org.openintents.safecloud.client.presenter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openintents.safecloud.client.event.CategoryAddEvent;
import org.openintents.safecloud.client.event.CategoryDeletedEvent;
import org.openintents.safecloud.client.event.CategoryDeletedEventHandler;
import org.openintents.safecloud.client.event.CategoryListChangedEvent;
import org.openintents.safecloud.client.event.CategoryUpdatedEvent;
import org.openintents.safecloud.client.event.CategoryUpdatedEventHandler;
import org.openintents.safecloud.client.event.ShowCategoryPopupEvent;
import org.openintents.safecloud.client.helper.ClickPoint;
import org.openintents.safecloud.client.helper.RPCCall;
import org.openintents.safecloud.client.service.CategoryServiceAsync;
import org.openintents.safecloud.shared.SafeCategory;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

public class CategoryListPresenter implements Presenter {

	private List<SafeCategory> categories;
	private List<Integer> selectedRows;

	public interface Display {
		HasClickHandlers getAddButton();
		
		HasClickHandlers getList();
		
		void setData(List<String> friendNames);
		
		int getClickedRow(ClickEvent event);

	    ClickPoint getClickedPoint(ClickEvent event);

		List<Integer> getSelectedRows();
		
		Widget asWidget();
	}

	private final CategoryServiceAsync rpcService;
	private final SimpleEventBus eventBus;
	private final Display display;

	public CategoryListPresenter(CategoryServiceAsync rpcService,
		SimpleEventBus eventBus, Display view) {
		this.rpcService = rpcService;
		this.eventBus = eventBus;
		this.display = view;
		bind();
	}

	public void bind() {
		// Bind view
		display.getAddButton().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				eventBus.fireEvent(new CategoryAddEvent());
			}
		});

		if (display.getList() != null)
			display.getList().addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					Logger logger = Logger.getLogger("");

					int propClicked = display
						.getClickedRow(event);
					logger.log(Level.INFO, "Category list clicked");
					if (propClicked >= 0) {
						logger.log(Level.INFO, "Category list property button clicked");
						ClickPoint point = display.getClickedPoint(event);

						SafeCategory category = categories
							.get(propClicked);
					    logger.log(Level.INFO, "fire event: "+category.getName());

						eventBus.fireEvent(new ShowCategoryPopupEvent(category,
								point));

					} else {
						GWT.log("Friend list check box clicked");
						selectedRows = display.getSelectedRows();
						fireCategoryListChangeEvent();
					}
				}
			});
	    // Listen to events
	    eventBus.addHandler(CategoryUpdatedEvent.TYPE,
	        new CategoryUpdatedEventHandler() {
	          public void onCategoryUpdated(CategoryUpdatedEvent event) {
	            fetchCategories();
	          }

	        });

	    eventBus.addHandler(CategoryDeletedEvent.TYPE,
	        new CategoryDeletedEventHandler() {
	          @Override
	          public void onCategoryDeleted(CategoryDeletedEvent event) {
	            fetchCategories();
	          }
	        });
	}
	
	@Override
	public void go(HasWidgets container) {
		container.clear();
	    container.add(display.asWidget());
	    fetchCategories();
	}

	public void sortCategories() {
		if (categories==null) return;
		for (int i = 0; i < categories.size(); ++i) {
			for (int j = 0; j < categories.size() - 1; ++j) {
				if (categories.get(j).getName()
						.compareToIgnoreCase(
								categories.get(j + 1).getName()) >= 0) {
					SafeCategory tmp = categories.get(j);
					categories.set(j, categories.get(j + 1));
					categories.set(j + 1, tmp);
				}
			}
		}
	}

	private void fetchCategories() {

		    new RPCCall<ArrayList<SafeCategory>>() {
		      @Override
		      protected void callService(
		          AsyncCallback<ArrayList<SafeCategory>> cb) {
		        rpcService.getCategories(cb);
		      }

		      @Override
		      public void onSuccess(ArrayList<SafeCategory> result) {
		        categories = result;
		        sortCategories();
		        display.setData(toStringList(categories));
		        fireCategoryListChangeEvent();
		      }

		      @Override
		      public void onFailure(Throwable caught) {
		        Window.alert("Error fetching categories: "
		            + caught.getMessage());
		      }
		    }.retry(3);

	}

	private List<String> toStringList(List<SafeCategory> categories) {
		    List<String> list = new ArrayList<String>();
		    if (categories!=null) {
			    for (SafeCategory f : categories) {
			      list.add(f.getName());
			    }
		    }
		    return list;
	}

	private void fireCategoryListChangeEvent() {
		    List<SafeCategory> selection = new ArrayList<SafeCategory>();
		    Integer row;
		    if (selectedRows != null) {
		      for (Iterator<Integer> i = selectedRows.iterator(); i.hasNext();) {
		        row = (Integer) i.next();
		        GWT.log("selected: " + row.toString());
		        selection.add(categories.get(row));
		      }
		    }
		    if (selection.isEmpty())
		      eventBus.fireEvent(new CategoryListChangedEvent(categories));
		    else
		      eventBus.fireEvent(new CategoryListChangedEvent(selection));
	}
}
