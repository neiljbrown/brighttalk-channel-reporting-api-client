/**
 * ****************************************************************************
 * Copyright BrightTALK Ltd, 2014.
 * All Rights Reserved.
 * $Id:$
 * ****************************************************************************
 */
package com.brighttalk.channels.reportingapi.v1.client;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimaps;

/**
 * Builds a map representation of the request parameters supported by the Get Channel Subscribers API from supplied
 * values. Also defines the set of named request parameters supported by the API.
 */
public class GetChannelSubscribersRequestParamsBuilder {

  /* CHECKSTYLE:OFF */
  enum ParamName {
    SUBSCRIBED("subscribed"), SUBSCRIBED_SINCE("subscribedSince"), UNSUBSCRIBED_SINCE("unsubscribedSince");

    private String name;

    ParamName(String name) {
      this.name = name;
    }

    public String getName() {
      return this.name;
    }
  }

  /* CHECKSTYLE:ON */

  // Multimap API uses flattened collection of key/value pairs, with multiple entries for multiple values with same key
  // Multimap.asMap() is used to convert this to a Map<String, Collection<String>> representation.
  private ListMultimap<String, String> params = ArrayListMultimap.create();

  private ApiDateTimeFormatter dateFormat = new ApiDateTimeFormatter();

  /**
   * Builds the request parameters using the data from the supplied parameters.
   * 
   * @param subscribed Optionally filters the results according to the Subscriber’s current subscription status.
   * {@code true} to to include only those that are subscribed. {@code false} to include only those that are
   * unsubscribed. If not supplied all Subscribers are returned, irrespective of their current subscription status.
   * @param subscribedSince Optionally filters the results to include only those Subscribers who subscribed after
   * (exclusive) the specified UTC date / time. (May, depending on the ‘subscribed’ param, include Subscribers who have
   * since unsubscribed).
   * @param unsubscribedSince Optionally filters the results to include only those Subscribers who <em>un</em>subscribed
   * after (exclusive) the specified UTC date / time. Implies setting the 'subscribed' param to “false”.
   * @param pageCriteria Optional {@link PageCriteria page criteria}.
   */
  public GetChannelSubscribersRequestParamsBuilder(Boolean subscribed, Date subscribedSince, Date unsubscribedSince,
      PageCriteria pageCriteria) {
    if (subscribed != null) {
      this.params.put(ParamName.SUBSCRIBED.getName(), subscribed.toString());
    }
    if (subscribedSince != null) {
      this.params.put(ParamName.SUBSCRIBED_SINCE.getName(), this.dateFormat.formatAsDateTime(subscribedSince));
    }
    if (unsubscribedSince != null) {
      this.params.put(ParamName.UNSUBSCRIBED_SINCE.getName(), this.dateFormat.formatAsDateTime(unsubscribedSince));
    }
    if (pageCriteria != null) {
      Map<String, List<String>> pagingParams = new PagingRequestParamsBuilder(pageCriteria).asMap();
      for (String paramName : pagingParams.keySet()) {
        this.params.putAll(paramName, pagingParams.get(paramName));
      }
    }
  }

  /**
   * @return A {@code Map<String, List<String>>} representation of thee request parameter names and their values.
   */
  public Map<String, List<String>> asMap() {
    // Multimap's asMap() methods convert
    return Multimaps.asMap(this.params);
  }
}