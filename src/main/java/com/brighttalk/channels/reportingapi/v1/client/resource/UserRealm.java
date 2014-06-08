/**
 * ****************************************************************************
 * Copyright BrightTALK Ltd, 2014.
 * All Rights Reserved.
 * $Id:$
 * ****************************************************************************
 */
package com.brighttalk.channels.reportingapi.v1.client.resource;

import javax.xml.bind.annotation.XmlAttribute;

import com.google.common.base.Objects;

/**
 * A grouping of users of the BrightTALK Channels platform that are registered, authenticated and managed externally.
 */
public class UserRealm {
  @XmlAttribute
  private final int id;

  public UserRealm(int id) {
    this.id = id;
  }

  public final int getId() {
    return this.id;
  }      
  
  @Override
  public String toString() {
    /* @formatter:off */    
    return Objects.toStringHelper(this).omitNullValues()
      .add("id", this.id)
      .toString();
    /* @formatter:on */    
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + this.id;
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    UserRealm other = (UserRealm) obj;
    if (this.id != other.id) {
      return false;
    }
    return true;
  }  
}