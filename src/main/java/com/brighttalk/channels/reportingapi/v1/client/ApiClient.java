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
}