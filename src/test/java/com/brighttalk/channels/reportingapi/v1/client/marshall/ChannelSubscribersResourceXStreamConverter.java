/**
 * ****************************************************************************
 * Copyright BrightTALK Ltd, 2014.
 * All Rights Reserved.
 * $Id: $
 * ****************************************************************************
 */
package com.brighttalk.channels.reportingapi.v1.client.marshall;

import java.util.ArrayList;
import java.util.List;

import com.brighttalk.channels.reportingapi.v1.client.resource.ChannelSubscriberResource;
import com.brighttalk.channels.reportingapi.v1.client.resource.ChannelSubscribersResource;
import com.brighttalk.channels.reportingapi.v1.client.resource.Link;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 * {@link Converter XStream Converter} which supports unmarshalling representations of
 * {@link ChannelSubscribersResource} - a collection of {@link ChannelSubscriberResource}.
 * 
 * @see ChannelSubscriberResourceXStreamConverter
 */
public class ChannelSubscribersResourceXStreamConverter implements Converter {

  /** {@inheritDoc} */
  @Override
  @SuppressWarnings("rawtypes")
  public boolean canConvert(Class type) {
    return type.equals(ChannelSubscribersResource.class);
  }

  /** {@inheritDoc} */
  @Override
  public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
    throw new UnsupportedOperationException("Marshalling not currently supported.");
  }

  /** {@inheritDoc} */
  @Override
  public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
    List<ChannelSubscriberResource> subscribers = new ArrayList<>();
    List<Link> resourceLinks = new ArrayList<>();
    while (reader.hasMoreChildren()) {
      reader.moveDown();
      if ("channelSubscriber".equals(reader.getNodeName())) {
        ChannelSubscriberResource subscriber = (ChannelSubscriberResource) context.convertAnother(null, ChannelSubscriberResource.class);
        subscribers.add(subscriber);
      } else if ("link".equals(reader.getNodeName())) {
        Link resourceLink = (Link) context.convertAnother(null, Link.class);
        resourceLinks.add(resourceLink);
      }
      reader.moveUp();
    }
    if (subscribers.size() > 0) {
      return new ChannelSubscribersResource(subscribers, resourceLinks);
    }
    return null;
  }
}