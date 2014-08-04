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
import com.brighttalk.channels.reportingapi.v1.client.resource.WebcastRegistrationResource;
import com.brighttalk.channels.reportingapi.v1.client.resource.WebcastRegistrationsResource;
import com.brighttalk.channels.reportingapi.v1.client.resource.WebcastViewingResource;
import com.brighttalk.channels.reportingapi.v1.client.resource.WebcastViewingsResource;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 * {@link Converter XStream Converter} which supports unmarshalling representations of {@link WebcastViewingsResource} -
 * a collection of {@link WebcastViewingResource}.
 * 
 * @see WebcastViewingResourceXStreamConverter
 */
public class WebcastViewingsResourceXStreamConverter implements Converter {

  /** {@inheritDoc} */
  @Override
  @SuppressWarnings("rawtypes")
  public boolean canConvert(Class type) {
    return type.equals(WebcastViewingsResource.class);
  }

  /** {@inheritDoc} */
  @Override
  public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
    throw new UnsupportedOperationException("Marshalling not currently supported.");
  }

  /** {@inheritDoc} */
  @Override
  public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
    List<WebcastViewingResource> viewings = null;
    List<Link> links = null;
    while (reader.hasMoreChildren()) {
      reader.moveDown();
      if ("webcastViewing".equals(reader.getNodeName())) {
        if (viewings == null) {
          viewings = new ArrayList<>();
        }
        WebcastViewingResource viewing = (WebcastViewingResource) context.convertAnother(null,
            WebcastViewingResource.class);
        viewings.add(viewing);
      } else if ("link".equals(reader.getNodeName())) {
        if (links == null) {
          links = new ArrayList<>();
        }
        Link link = (Link) context.convertAnother(null, Link.class);
        links.add(link);
      }
      reader.moveUp();
    }
    return new WebcastViewingsResource(viewings, links);
  }
}