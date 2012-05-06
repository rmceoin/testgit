package org.openintents.safecloud.server.service;

import java.util.List;

import org.openintents.safecloud.shared.SafeLogin;

import com.google.appengine.api.datastore.EntityNotFoundException;

public class SafeLoginDao extends ObjectifyDao<SafeLogin>
{
  public SafeLogin save(SafeLogin SafeLogin)
  {
    put(SafeLogin);
    return SafeLogin;
  }
  
  public SafeLogin fetch(Long id) throws EntityNotFoundException {
    return get(id);
  }
  
  public List<SafeLogin> fetchRange(Integer start, Integer length) {
    return listAll(start, length); 
  }
  
  public Integer getCount() {
    return countAll();
  }
}