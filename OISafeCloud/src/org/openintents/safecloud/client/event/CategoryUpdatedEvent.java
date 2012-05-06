package org.openintents.safecloud.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class CategoryUpdatedEvent extends GwtEvent<CategoryUpdatedEventHandler> {
  public static Type<CategoryUpdatedEventHandler> TYPE = new Type<CategoryUpdatedEventHandler>();
  
  @Override
  public Type<CategoryUpdatedEventHandler> getAssociatedType() {
    return TYPE;
  }

  @Override
  protected void dispatch(CategoryUpdatedEventHandler handler) {
    handler.onCategoryUpdated(this);
  }
}
