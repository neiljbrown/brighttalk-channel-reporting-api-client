/**
 * ****************************************************************************
 * Copyright BrightTALK Ltd, 2014.
 * All Rights Reserved.
 * $Id:$
 * ****************************************************************************
 */
package com.brighttalk.channels.reportingapi.v1.client.resource;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.common.base.Objects;

/**
 * An error returned by the API in response to a failed API request.
 */
@XmlRootElement(name = "error")
@XmlAccessorType(XmlAccessType.FIELD)
public class ApiError {
  private String code;
  private String message;
  
  // Private, as only exists only to keep JAXB implementation happy.
  private ApiError() {
  }  

  public ApiError(String code, String message) {
    this.code = code;
    this.message = message;
  }

  public final String getCode() {
    return this.code;
  }

  public final String getMessage() {
    return this.message;
  }   
  
  @Override
  public String toString() {
    /* @formatter:off */    
    return Objects.toStringHelper(this).omitNullValues()
      .add("code", this.code)
      .add("message", this.message)
      .toString();
    /* @formatter:on */    
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((this.code == null) ? 0 : this.code.hashCode());
    result = prime * result + ((this.message == null) ? 0 : this.message.hashCode());
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
    ApiError other = (ApiError) obj;
    if (this.code == null) {
      if (other.code != null) {
        return false;
      }
    } else if (!this.code.equals(other.code)) {
      return false;
    }
    if (this.message == null) {
      if (other.message != null) {
        return false;
      }
    } else if (!this.message.equals(other.message)) {
      return false;
    }
    return true;
  }   
}