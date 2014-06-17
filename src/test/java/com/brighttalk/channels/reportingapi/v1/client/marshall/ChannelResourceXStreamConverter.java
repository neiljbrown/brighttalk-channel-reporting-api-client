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
import com.brighttalk.channels.reportingapi.v1.client.resource.ChannelResource;
import com.brighttalk.channels.reportingapi.v1.client.resource.Link;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 * {@link Converter XStream Converter} which supports unmarshalling representations of {@link ChannelResource}.
 */
public class ChannelResourceXStreamConverter implements Converter {

  /** {@inheritDoc} */
  @Override
  @SuppressWarnings("rawtypes")  
  public boolean canConvert(Class type) {
    return type.equals(ChannelResource.class);
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
    String name = null;
    String strapline = null;
    String description = null;
    String url = null;
    String organisation = null;
    String keywords = null;
    String type = null;
    String created = null;
    String lastUpdated = null;
    List<Link> resourceLinks = new ArrayList<>();
    while (reader.hasMoreChildren()) {
      reader.moveDown();
      String nodeName = reader.getNodeName();
      if ("name".equals(nodeName)) {
        name = reader.getValue();
      } else if ("strapline".equals(nodeName)) {
        strapline = reader.getValue();
      } else if ("description".equals(nodeName)) {
        description = reader.getValue();
      } else if ("url".equals(nodeName)) {
        url = reader.getValue();
      } else if ("organisation".equals(nodeName)) {
        organisation = reader.getValue();
      } else if ("keywords".equals(nodeName)) {
        keywords = reader.getValue();
      } else if ("type".equals(nodeName)) {
        type = reader.getValue();
      } else if ("created".equals(nodeName)) {
        created = reader.getValue();
      } else if ("lastUpdated".equals(nodeName)) {
        lastUpdated = reader.getValue();
      } else if ("link".equals(nodeName)) {
        // Passing null to UnmarshallingContext.convertAnother() as ChannelResource is immutable so don't have a 
        // current object. XStream permits this.
        Link resourceLink = (Link) context.convertAnother(null, Link.class);
        resourceLinks.add(resourceLink);
      }
      reader.moveUp();
    }
    ApiDateTimeFormatter dateTimeFormatter = new ApiDateTimeFormatter();
    Date createdDate = created != null ? dateTimeFormatter.parse(created) : null;
    Date lastUpdatedDate = lastUpdated != null ? dateTimeFormatter.parse(lastUpdated) : null;
    return new ChannelResource(id, name, strapline, description, url, organisation, keywords, type, createdDate,
        lastUpdatedDate, resourceLinks);
  }
}
