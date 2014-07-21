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

import com.brighttalk.channels.reportingapi.v1.client.resource.Link;
import com.brighttalk.channels.reportingapi.v1.client.resource.SubscriberWebcastActivityResource;
import com.brighttalk.channels.reportingapi.v1.client.resource.SubscribersWebcastActivityResource;
import com.brighttalk.channels.reportingapi.v1.client.resource.SurveyResponseResource;
import com.brighttalk.channels.reportingapi.v1.client.resource.SurveyResponsesResource;
import com.brighttalk.channels.reportingapi.v1.client.resource.WebcastResource;
import com.brighttalk.channels.reportingapi.v1.client.resource.WebcastsResource;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 * {@link Converter XStream Converter} which supports unmarshalling representations of {@link WebcastsResource} - a
 * collection of {@link WebcastResource}.
 * 
 * @see WebcastResourceXStreamConverter
 */
public class WebcastsResourceXStreamConverter implements Converter {

  /** {@inheritDoc} */
  @Override
  @SuppressWarnings("rawtypes")
  public boolean canConvert(Class type) {
    return type.equals(WebcastsResource.class);
  }

  /** {@inheritDoc} */
  @Override
  public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
    throw new UnsupportedOperationException("Marshalling not currently supported.");
  }

  /** {@inheritDoc} */
  @Override
  public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
    List<WebcastResource> webcasts = null;
    List<Link> links = null;
    while (reader.hasMoreChildren()) {
      reader.moveDown();
      if ("webcast".equals(reader.getNodeName())) {
        if (webcasts == null) {
          webcasts = new ArrayList<>();
        }
        WebcastResource webcast = (WebcastResource) context.convertAnother(null, WebcastResource.class);
        webcasts.add(webcast);
      } else if ("link".equals(reader.getNodeName())) {
        if (links == null) {
          links = new ArrayList<>();
        }
        Link link = (Link) context.convertAnother(null, Link.class);
        links.add(link);
      }
      reader.moveUp();
    }
    return new WebcastsResource(webcasts, links);
  }
}