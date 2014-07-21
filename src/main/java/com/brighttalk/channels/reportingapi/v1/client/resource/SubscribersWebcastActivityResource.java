/**
 * ****************************************************************************
 * Copyright BrightTALK Ltd, 2014.
 * All Rights Reserved.
 * $Id:$
 * ****************************************************************************
 */
package com.brighttalk.channels.reportingapi.v1.client.resource;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.common.base.Objects;

/**
 * A collection of {@link SubscriberWebcastActivity subscriber webcast activity}.
 */
@XmlRootElement(name = "subscribersWebcastActivity")
@XmlAccessorType(XmlAccessType.FIELD)
public class SubscribersWebcastActivityResource {

  /** URI template string for the relative URL of the Subscribers Webcast Activity for an identified channel. */
  public static final String FOR_CHANNEL_RELATIVE_URI_TEMPLATE = "/v1/channel/{channelId}/subscribers_webcast_activity";

  /** URI template string for the relative URL of the Subscribers Webcast Activity for an identified webcast. */
  public static final String FOR_WEBCAST_RELATIVE_URI_TEMPLATE = "/v1/channel/{channelId}/webcast/{webcastId}/subscribers_webcast_activity";

  @XmlElementRef
  private List<SubscriberWebcastActivityResource> subscriberWebcastActivities = new ArrayList<>();
  
  @XmlElement(name = "link")
  private List<Link> links = new ArrayList<>();

  // Private, as only exists only to keep JAXB implementation happy.
  private SubscribersWebcastActivityResource() {
  }

  public SubscribersWebcastActivityResource(List<SubscriberWebcastActivityResource> subscriberWebcastActivities,
      List<Link> links) {
    this.subscriberWebcastActivities = subscriberWebcastActivities;
    this.links = links;
  }

  /**
   * @return the subscriberWebcastActivities
   */
  public final List<SubscriberWebcastActivityResource> getSubscriberWebcastActivities() {
    return this.subscriberWebcastActivities != null ? this.subscriberWebcastActivities
        : new ArrayList<SubscriberWebcastActivityResource>();
  }

  /**
   * @return the links
   */
  public final List<Link> getLinks() {
    return this.links != null ? this.links : new ArrayList<Link>();
  }

  @Override
  public String toString() {
    /* @formatter:off */    
    return Objects.toStringHelper(this).omitNullValues()
      .add("subscriberWebcastActivities", this.subscriberWebcastActivities)
      .add("links", this.links)
      .toString();
    /* @formatter:on */
  }
}