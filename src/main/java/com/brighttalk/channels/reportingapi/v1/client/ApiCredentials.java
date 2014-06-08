/**
 * ****************************************************************************
 * Copyright BrightTALK Ltd, 2014.
 * All Rights Reserved.
 * $Id:$
 * ****************************************************************************
 */
package com.brighttalk.channels.reportingapi.v1.client;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

/**
 * A set of credentials used to authenticate an API client as an owner of one or more BrightTALK Channels.
 */
public class ApiCredentials {
  
  private final int key;
  private final String secret;
  
  /**
   * @param key The unique identifier for the BrightTALK API user.
   * @param secret A secret value used to authenticate the API user identified by the {@code key}.  
   */
  public ApiCredentials(int key, String secret) {
    Preconditions.checkArgument(key > 0, "API key must be a positive integer.");
    Preconditions.checkArgument(Strings.isNullOrEmpty(secret), "API secret must be a non-empty string.");
    this.key = key;
    this.secret = secret;
  }

  public final int getKey() {
    return this.key;
  }

  public final String getSecret() {
    return this.secret;
  }       
}