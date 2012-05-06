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
package org.openintents.safecloud.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.openintents.safecloud.client.event.CategoryAddEvent;
import org.openintents.safecloud.client.event.CategoryAddEventHandler;
import org.openintents.safecloud.client.event.CategoryEditEvent;
import org.openintents.safecloud.client.event.CategoryEditEventHandler;
import org.openintents.safecloud.client.event.ShowCategoryPopupEvent;
import org.openintents.safecloud.client.event.ShowCategoryPopupEventHandler;
import org.openintents.safecloud.client.presenter.CategoryEditPresenter;
import org.openintents.safecloud.client.presenter.CategoryPopupPresenter;
import org.openintents.safecloud.client.presenter.Presenter;
import org.openintents.safecloud.client.service.CategoryServiceAsync;
import org.openintents.safecloud.client.view.CategoryEditView;
import org.openintents.safecloud.client.view.CategoryPopupView;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.History;


public class AppController implements ValueChangeHandler<String> {
	private final SimpleEventBus eventBus;
	private final CategoryServiceAsync categoryService;
	private Long currentCategoryId;

	public AppController(CategoryServiceAsync rpcService, SimpleEventBus eventBus) {
		this.eventBus = eventBus;
		this.categoryService = rpcService;
		bind();
	}

	private void bind() {
	    
		History.addValueChangeHandler(this);

    eventBus.addHandler(CategoryAddEvent.TYPE, new CategoryAddEventHandler() {
      public void onAddCategory(CategoryAddEvent event) {
        doAddNewCategory();
      }
    });

	eventBus.addHandler(ShowCategoryPopupEvent.TYPE, new ShowCategoryPopupEventHandler() {
		public void onShowCategoryPopup(ShowCategoryPopupEvent event) {
			CategoryPopupPresenter categoryPopupPresenter = new CategoryPopupPresenter(
					categoryService, eventBus, new CategoryPopupView(event.getCategory()
							.getName(), event.getClickPoint()), event.getCategory());
			categoryPopupPresenter.go();
		}
	});
    eventBus.addHandler(CategoryEditEvent.TYPE, new CategoryEditEventHandler() {
      public void onEditCategory(CategoryEditEvent event) {
        doEditCategory(event.getId());
      }
    });
    /*

    eventBus.addHandler(CategoryEditCancelledEvent.TYPE, new CategoryEditCancelledEventHandler() {
      public void onEditCategoryCancelled(CategoryEditCancelledEvent event) {
        doEditCategoryCancelled();
      }
    });

    eventBus.addHandler(CategoryUpdatedEvent.TYPE, new CategoryUpdatedEventHandler() {
      public void onCategoryUpdated(CategoryUpdatedEvent event) {
        doCategoryUpdated();
      }
    });
  */  
  }

  private void doAddNewCategory() {
    History.newItem("add");
  }

  private void doEditCategory(Long id) {
    currentCategoryId = id;
    History.newItem("edit");
  }

  private void doEditCategoryCancelled() {
    History.newItem("list");
  }

  private void doCategoryUpdated() {
    History.newItem("list");
  }


  public void go() {

    if ("".equals(History.getToken())) {
      History.newItem("list");
    } else {
      History.fireCurrentHistoryState();
    }
  }

  public void onValueChange(ValueChangeEvent<String> event) {
    String token = event.getValue();

    if (token != null) {
      Presenter presenter = null;

      if (token.equals("list")) {
//        presenter = new MessageListPresenter(messagesService, eventBus, new MessageListView());
//        presenter.go(ConnectrApp.get().getMainPanel());
        return;

      } else if (token.equals("add")) {
        presenter = new CategoryEditPresenter(categoryService, eventBus, new CategoryEditView());
        presenter.go(OISafeCloud.get().getMainPanel());
        return;

      } 

      else if (token.equals("edit")) {
        presenter = new CategoryEditPresenter(categoryService, eventBus, new CategoryEditView(), currentCategoryId);
        presenter.go(OISafeCloud.get().getMainPanel());

        return;
      }
    }

  }
}
