/**
 * ****************************************************************************
 * Copyright BrightTALK Ltd, 2014.
 * All Rights Reserved.
 * $Id: $
 * ****************************************************************************
 */
package com.brighttalk.channels.reportingapi.v1.client;

import java.util.Date;

import com.brighttalk.channels.reportingapi.v1.client.resource.ChannelSubscribersResource;
import com.brighttalk.channels.reportingapi.v1.client.resource.ChannelsResource;
import com.brighttalk.channels.reportingapi.v1.client.resource.SubscribersWebcastActivityResource;
import com.brighttalk.channels.reportingapi.v1.client.resource.SurveyResource;
import com.brighttalk.channels.reportingapi.v1.client.resource.SurveyResponseResource;
import com.brighttalk.channels.reportingapi.v1.client.resource.SurveyResponsesResource;
import com.brighttalk.channels.reportingapi.v1.client.resource.SurveysResource;

/**
 * A client for accessing the BrightTALK channel owner reporting API (version 1.x).
 * <p>
 * TODO @see <a href="...">BrightTALK channel owner reporting API technical specification</a>
 */
public interface ApiClient {

  /**
   * Retrieves the channels owned by the current API user.
   * 
   * @param pageCriteria Optional {@link PageCriteria page criteria}.
   * @return A {@link ChannelsResource} containing a collection of channels owned by the current user.
   * @throws ApiClientException If an error occurs on making the API call.
   */
  ChannelsResource getMyChannels(PageCriteria pageCriteria) throws ApiClientException;

  /**
   * Retrieves the channels owned by an identified BrightTALK user.
   * <p>
   * The API credentials used must be authorised to access the identified user's channels.
   * 
   * @param userId The ID of the BrightTALK user.
   * @param pageCriteria Optional {@link PageCriteria page criteria}.
   * @return A {@link ChannelsResource} containing a collection of channels owned by the identified user.
   * @throws ApiClientException If an error occurs on making the API call.
   */
  ChannelsResource getUserChannels(int userId, PageCriteria pageCriteria) throws ApiClientException;

  /**
   * Retrieves the list of current and/or past subscribers to a channel owned by an API user.
   * 
   * @param channelId The ID of the channel.
   * @param subscribed Optionally filters the results according to the subscriber’s current subscription status.
   * {@code true} to to include only those that are subscribed. {@code false} to include only those that are
   * unsubscribed. If not supplied all subscribers are returned, irrespective of their current subscription status.
   * @param subscribedSince Optionally filters the results to include only those subscribers who subscribed after
   * (exclusive) the specified UTC date / time. Supports incremental reporting of new and updated susbcribers. (May,
   * depending on the ‘subscribed’ param, include Subscribers who have since unsubscribed).
   * @param unsubscribedSince Optionally filters the results to include only those subscribers who <em>un</em>subscribed
   * after (exclusive) the specified UTC date / time. Implies setting the {@code subscribed} param to “false”.
   * @param pageCriteria Optional {@link PageCriteria page criteria}.
   * @return A {@link ChannelSubscribersResource} containing a collection of zero or more subscribers to the channel.
   * @throws ApiClientException If an error occurs on making the API call.
   */
  ChannelSubscribersResource getChannelSubscribers(int channelId, Boolean subscribed, Date subscribedSince,
      Date unsubscribedSince, PageCriteria pageCriteria) throws ApiClientException;

  /**
   * Retrieves a summary (aggregation) of per subscriber activity for webcasts in one of the channel owner's channels.
   * 
   * @param channelId The ID of the channel.
   * @param since Optionally filters the results to include only those Subscriber Webcast Activity (created or) updated
   * after (exclusive) the specified UTC date / time.
   * @param expandChannelSurveyResponse Optionally expands every child Subscriber Webcast Activity to include the
   * subscriber’s response to the channel’s survey, if there is one. Defaults to {@code false}.
   * @param pageCriteria Optional {@link PageCriteria page criteria}.
   * @return A {@link SubscribersWebcastActivityResource} containing a collection of zero or more activities.
   * @throws ApiClientException If an error occurs on making the API call.
   */
  SubscribersWebcastActivityResource getSubscribersWebcastActivityForChannel(int channelId, Date since,
      Boolean expandChannelSurveyResponse, PageCriteria pageCriteria) throws ApiClientException;

  /**
   * Retrieves a summary (aggregation) of per subscriber activity for a specific webcast in one of the channel owner's
   * channels.
   * 
   * @param channelId The ID of the channel.
   * @param webcastId The ID of the webcast.
   * @param since Optionally filters the results to include only those Subscriber Webcast Activity (created or) updated
   * after (exclusive) the specified UTC date / time.
   * @param expandChannelSurveyResponse Optionally expands every child Subscriber Webcast Activity to include the
   * subscriber’s response to the channel’s survey, if there is one. Defaults to {@code false}.
   * @param pageCriteria Optional {@link PageCriteria page criteria}.
   * @return A {@link SubscribersWebcastActivityResource} containing a collection of zero or more activities.
   * @throws ApiClientException If an error occurs on making the API call.
   */
  SubscribersWebcastActivityResource getSubscribersWebcastActivityForWebcast(int channelId, int webcastId, Date since,
      Boolean expandChannelSurveyResponse, PageCriteria pageCriteria) throws ApiClientException;

  /**
   * Retrieves the survey (definitions) for a channel owned by the current user.
   * <p>
   * Supports a Channel owner obtaining a link for subsequently retrieving the responses to a channel survey.
   * 
   * @param channelId The ID of the channel.
   * @return A {@link SurveysResource} containing a collection of zero or more {@link SurveyResource surveys}.
   * @throws ApiClientException If an error occurs on making the API call.
   */
  SurveysResource getSurveysForChannel(int channelId) throws ApiClientException;

  /**
   * Retrieves an identified survey (definition) owned by the current user.
   * 
   * @param surveyId The ID of the survey.
   * @return The {@link SurveyResource}.
   * @throws ApiClientException If an error occurs on making the API call.
   */
  SurveyResource getSurvey(int surveyId) throws ApiClientException;

  
  /**
   * Retrieves all of the responses to an identified survey owned by the current user.
   * 
   * @param surveyId The ID of the survey.
   * @param since Optionally filters the results to include only those survey responses (created or) updated
   * after (exclusive) the specified UTC date / time.
   * @param pageCriteria Optional {@link PageCriteria page criteria}.
   * @return A {@link SurveyResponsesResource} containing a collection of zero or more {@link SurveyResponseResource}.
   * @throws ApiClientException If an error occurs on making the API call. 
   */
  SurveyResponsesResource getSurveyResponses(int surveyId, Date since, PageCriteria pageCriteria)
      throws ApiClientException;
}