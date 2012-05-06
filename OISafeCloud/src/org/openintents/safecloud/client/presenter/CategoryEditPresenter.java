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

import java.util.Set;

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
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

public class CategoryEditPresenter implements Presenter {
  public interface Display {
    HasClickHandlers getAddUrlButton();
    HasClickHandlers getCancelButton();
    String getDeletedUrl(ClickEvent event);
    HasValue<String> getEmailAddress();
    HasValue<String> getCategoryName();
    HasValue<String> getLastName();
    HasClickHandlers getList();
    HasClickHandlers getSaveButton();
    HasValue<String> getUrl();
    void setData(Set<String> urls);
    Widget asWidget();
  }


  private SafeCategory category;
  private final CategoryServiceAsync rpcService;
  private final SimpleEventBus eventBus;
  private final Display display;

  public CategoryEditPresenter(CategoryServiceAsync rpcService, SimpleEventBus eventBus, Display display) {
    this.rpcService = rpcService;
    this.eventBus = eventBus;
    this.display = display;
    this.category = new SafeCategory();
    bind();
  }

  public CategoryEditPresenter(final CategoryServiceAsync rpcService, SimpleEventBus eventBus,
		  Display display, final Long id) {
    this(rpcService, eventBus, display);

    if (id == null)
      return;
    
    new RPCCall<SafeCategory>() {
      @Override
      protected void callService(AsyncCallback<SafeCategory> cb) {
        rpcService.getCategory(id, cb);
      }

      @Override
      public void onSuccess(SafeCategory result) {
        category = result;
        CategoryEditPresenter.this.display.getCategoryName().setValue(category.getName());
//        CategoryEditPresenter.this.display.getLastName().setValue(category.getLastName());
//        CategoryEditPresenter.this.display.getEmailAddress().setValue(category.getEmailAddress());
//        CategoryEditPresenter.this.display.setData(category.getUrls());
      }

      @Override
      public void onFailure(Throwable caught) {
        Window.alert("Error retrieving category...");
      }
    }.retry(3);

  }

  public void bind() {
    this.display.getSaveButton().addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        doSave();
      }
    });

    this.display.getAddUrlButton().addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        addURL();
      }

    });

    this.display.getCancelButton().addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        GWT.log("CategoryEditPresenter: Firing CategoryEditCancelledEvent");
//        eventBus.fireEvent(new CategoryEditCancelledEvent());
      }
    });
    
    if (display.getList() != null)
      display.getList().addClickHandler(new ClickHandler() {
        public void onClick(ClickEvent event) {
          String url = display.getDeletedUrl(event);

          if (url != null) {
            deleteUrl(url);
          }
        }

        private void deleteUrl(String url) {
//          category.getUrls().remove(url);
//          display.setData(category.getUrls());
        }
      });

  }

  public void go(final HasWidgets container) {
    container.clear();
    container.add(display.asWidget());
  }

  private void addURL() {
    String url = display.getUrl().getValue().trim();
    if (url.equals(""))
      return;
    
//    category.addUrl(url);
//    display.setData(category.getUrls());
  }
  
  private void doSave() {
    category.setName(display.getCategoryName().getValue().trim());
//    category.setLastName(display.getLastName().getValue().trim());
//    category.setEmailAddress(display.getEmailAddress().getValue().trim());

    new RPCCall<SafeCategory>() {
      @Override
      protected void callService(AsyncCallback<SafeCategory> cb) {
        rpcService.updateCategory(category, cb);
      }

      @Override
      public void onSuccess(SafeCategory result) {
        GWT.log("CategoryEditPresenter: Firing CategoryUpdateEvent");
//        eventBus.fireEvent(new CategoryUpdatedEvent(result));
      }

      @Override
      public void onFailure(Throwable caught) {
        Window.alert("Error retrieving category...");
      }
    }.retry(3);
  }

}
