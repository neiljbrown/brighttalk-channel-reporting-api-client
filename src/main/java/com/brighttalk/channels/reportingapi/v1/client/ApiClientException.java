/**
 * ****************************************************************************
 * Copyright BrightTALK Ltd, 2011.
 * All Rights Reserved.
 * $Id: MarketoClientException.java 32042 2011-06-23 18:08:09Z nbrown $
 * ****************************************************************************
 */
package com.brighttalk.channels.reportingapi.v1.client;

/**
 * Base class for non-checked (runtime) exception thrown by the {@link ApiClient}.
 */
public class ApiClientException extends RuntimeException {
  
  /**
   * @param message The internal, technical diagnostic message providing supplementary information about the context in 
   * which the exception occurred.  
   */
  public ApiClientException(final String message) {
    super(message);
  }
  
  /** 
   * @param cause The causal exception.
   */
  public ApiClientException(final Throwable cause) {
    super(cause);
  }  

  /** 
   * @param message The internal, technical diagnostic message providing supplementary information about the context in 
   * which the exception occurred.  
   * @param cause The causal exception.
   */
  public ApiClientException(final String message, final Throwable cause) {
    super(message, cause);
  }   
}