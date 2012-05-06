package org.openintents.safecloud.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class CategoryAddEvent extends GwtEvent<CategoryAddEventHandler> {
  public static Type<CategoryAddEventHandler> TYPE = new Type<CategoryAddEventHandler>();
  
  @Override
  public Type<CategoryAddEventHandler> getAssociatedType() {
    return TYPE;
  }

  @Override
  protected void dispatch(CategoryAddEventHandler handler) {
    handler.onAddCategory(this);
  }
}
