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

import com.brighttalk.channels.reportingapi.v1.client.resource.ChannelResource;
import com.brighttalk.channels.reportingapi.v1.client.resource.ChannelsResource;
import com.brighttalk.channels.reportingapi.v1.client.resource.Link;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 * {@link Converter XStream Converter} which supports unmarshalling representations of {@link ChannelsResource} - a 
 * collection of {@link ChannelResource}.
 *
 * @see ChannelResourceXStreamConverter    
 */
public class ChannelsResourceXStreamConverter implements Converter {

  /** {@inheritDoc} */
  @Override
  @SuppressWarnings("rawtypes")  
  public boolean canConvert(Class type) {
    return type.equals(ChannelsResource.class);
  }

  /** {@inheritDoc} */
  @Override
  public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
    throw new UnsupportedOperationException("Marshalling not currently supported.");
  }

  /** {@inheritDoc} */
  @Override
  public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {    
    List<ChannelResource> channels = new ArrayList<>();
    List<Link> resourceLinks = new ArrayList<>();
    while (reader.hasMoreChildren()) {
      reader.moveDown();
      if ("channel".equals(reader.getNodeName())) {
        ChannelResource channel = (ChannelResource) context.convertAnother(null, ChannelResource.class);
        channels.add(channel);
      } else if ("link".equals(reader.getNodeName())) {
        Link resourceLink = (Link) context.convertAnother(null, Link.class);
        resourceLinks.add(resourceLink);
      }
      reader.moveUp();
    }
    if (channels.size() > 0) {
      return new ChannelsResource(channels, resourceLinks);
    }
    return null;
  }
}