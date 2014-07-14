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

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimaps;

/**
 * Builds a map representation of the request parameters supported by the {@link ApiClient#getSurveyResponses Get Survey
 * Responses API} from supplied values. Also defines the set of named request parameters supported by the API.
 */
public class GetSurveyResponsesRequestParamsBuilder {

  /* CHECKSTYLE:OFF */
  enum ParamName {
    SINCE("since");

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
  // Multimap.asMap() is subsequently used to convert this to a Map<String, Collection<String>> representation.
  // Use LinkedListMultimap to get reliable (insert) order for keys as well as values
  private LinkedListMultimap<String, String> params = LinkedListMultimap.create();

  private ApiDateTimeFormatter dateFormat = new ApiDateTimeFormatter();

  /**
   * Builds the request parameters using the data from the supplied parameters.
   * 
   * @param since Optionally filters the results to include only those survey response (created or) updated
   * after (exclusive) the specified UTC date / time.
   * @param pageCriteria Optional {@link PageCriteria page criteria}.
   */
  public GetSurveyResponsesRequestParamsBuilder(Date since, PageCriteria pageCriteria) {
    if (since != null) {
      this.params.put(ParamName.SINCE.getName(), this.dateFormat.formatAsDateTime(since));
    }
    if (pageCriteria != null) {
      Map<String, List<String>> pagingParams = new PagingRequestParamsBuilder(pageCriteria).asMap();
      for (String paramName : pagingParams.keySet()) {
        this.params.putAll(paramName, pagingParams.get(paramName));
      }
    }
  }

  /**
   * @return A {@code Map<String, List<String>>} representation of the request parameter names and their values.
   */
  public Map<String, List<String>> asMap() {
    // Multimap's asMap() methods convert
    return Multimaps.asMap(this.params);
  }
}