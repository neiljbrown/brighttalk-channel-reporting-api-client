/**
 * ****************************************************************************
 * Copyright BrightTALK Ltd, 2014.
 * All Rights Reserved.
 * $Id: $
 * ****************************************************************************
 */
package com.brighttalk.channels.reportingapi.v1.client.marshall;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.brighttalk.channels.reportingapi.v1.client.ApiDateTimeFormatter;
import com.brighttalk.channels.reportingapi.v1.client.resource.ChannelSubscriberResource;
import com.brighttalk.channels.reportingapi.v1.client.resource.Embed;
import com.brighttalk.channels.reportingapi.v1.client.resource.Link;
import com.brighttalk.channels.reportingapi.v1.client.resource.User;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 * {@link Converter XStream Converter} which supports unmarshalling representations of a
 * {@link ChannelSubscriberResource}.
 */
public class ChannelSubscriberResourceXStreamConverter implements Converter {

  /** {@inheritDoc} */
  @Override
  @SuppressWarnings("rawtypes")
  public boolean canConvert(Class type) {
    return type.equals(ChannelSubscriberResource.class);
  }

  /** {@inheritDoc} */
  @Override
  public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
    throw new UnsupportedOperationException("Marshalling not currently supported.");
  }

  /** {@inheritDoc} */
  @Override
  public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
    int id = Integer.valueOf(reader.getAttribute("id"));
    String lastSubscribed = null;
    String unsubscribed = null;
    Embed embed = null;
    User user = null;
    List<Link> resourceLinks = new ArrayList<>();
    while (reader.hasMoreChildren()) {
      reader.moveDown();
      String nodeName = reader.getNodeName();
      if ("lastSubscribed".equals(nodeName)) {
        lastSubscribed = reader.getValue();
      } else if ("unsubscribed".equals(nodeName)) {
        unsubscribed = reader.getValue();
      } else if ("embed".equals(nodeName)) {
        embed = (Embed) context.convertAnother(null, Embed.class);        
      } else if ("user".equals(nodeName)) {
        user = (User) context.convertAnother(null, User.class);
      } else if ("link".equals(nodeName)) {
        Link resourceLink = (Link) context.convertAnother(null, Link.class);
        resourceLinks.add(resourceLink);
      }
      reader.moveUp();
    }
    ApiDateTimeFormatter dateTimeFormatter = new ApiDateTimeFormatter();
    Date lastSubscribedDate = lastSubscribed != null ? dateTimeFormatter.parse(lastSubscribed) : null;
    Date unsubscribedDate = unsubscribed != null ? dateTimeFormatter.parse(unsubscribed) : null;
    return new ChannelSubscriberResource(id, lastSubscribedDate, unsubscribedDate, embed, user, resourceLinks);
  }
}
