/** 
 * Copyright 2010 Daniel Guermeur and Amy Unruh
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 *   See http://connectrapp.appspot.com/ for a demo, and links to more information 
 *   about this app and the book that it accompanies.
 */
package org.openintents.safecloud.client.presenter;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.openintents.safecloud.client.event.CategoryDeletedEvent;
import org.openintents.safecloud.client.event.CategoryEditEvent;
import org.openintents.safecloud.client.helper.ClickPoint;
import org.openintents.safecloud.client.helper.RPCCall;
import org.openintents.safecloud.client.service.CategoryServiceAsync;
import org.openintents.safecloud.client.view.CategoryPopupView;
import org.openintents.safecloud.shared.SafeCategory;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

public class CategoryPopupPresenter implements Presenter {

  public interface Display {
    HasClickHandlers getEditButton();
    HasClickHandlers getDeleteButton();
    HasText getCategoryNameLabel();
    void hide();
    void setName(String displayName);
    void setNameAndShow(String displayName, ClickPoint location);
    Widget asWidget();
  }

  private SafeCategory category;
  CategoryPopupView popup;

  private final CategoryServiceAsync categoryService;
  private final SimpleEventBus eventBus;
  private Display display;

  public CategoryPopupPresenter(CategoryServiceAsync categoryService, SimpleEventBus eventBus,
		  Display display, SafeCategory category) {
    this.categoryService = categoryService;
    this.eventBus = eventBus;
    this.display = display;
    this.category = category;

    bind();
  }

  public void bind() {
    this.display.getEditButton().addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        display.hide();
		Logger logger = Logger.getLogger("");
		logger.log(Level.INFO, "fire CategoryEditEvent");

        eventBus.fireEvent(new CategoryEditEvent(category.getId()));
      }
    });

    this.display.getDeleteButton().addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
         display.hide();
        if (Window.confirm("Are you sure?")) {
          deleteCategory(category.getId());
        }
      }
    });

  }

  private void deleteCategory(final Long id) {
    
    new RPCCall<Boolean>() {
      @Override
      protected void callService(AsyncCallback<Boolean> cb) {
          categoryService.deleteCategory(id, cb);
        }

      @Override
      public void onSuccess(Boolean isDeleted) {
    	  if (isDeleted==true) {
    		  eventBus.fireEvent(new CategoryDeletedEvent());
    	  }
      }

      @Override
      public void onFailure(Throwable caught) {
        Window.alert("An error occurred: " + caught.toString());
      }

    }.retry(3);

  }

  public void go() {
  }

  @Override
  public void go(HasWidgets container) {
    // Auto-generated method stub
    
  }

}
