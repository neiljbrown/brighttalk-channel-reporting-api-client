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
import com.brighttalk.channels.reportingapi.v1.client.resource.Question;
import com.brighttalk.channels.reportingapi.v1.client.resource.SurveyResource;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 * {@link Converter XStream Converter} which supports unmarshalling representations of a {@link SurveyResource}.
 */
public class SurveyResourceXStreamConverter implements Converter {

  /** {@inheritDoc} */
  @Override
  @SuppressWarnings("rawtypes")
  public boolean canConvert(Class type) {
    return type.equals(SurveyResource.class);
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
    boolean active = false;
    List<Question> questions = null;   
    List<Link> links = null;
    while (reader.hasMoreChildren()) {
      reader.moveDown();
      String nodeName = reader.getNodeName();
      if ("active".equals(nodeName)) {
        active = Boolean.parseBoolean(reader.getValue());
      } else if ("questions".equals(nodeName)) {
        if (questions == null) {
          questions = new ArrayList<>();
        }
        while (reader.hasMoreChildren()) {
          reader.moveDown();
          Question question = (Question) context.convertAnother(null, Question.class);
          questions.add(question);
          reader.moveUp();          
        }
      } else if ("link".equals(nodeName)) {
        if (links == null) {
          links = new ArrayList<>();
        }
        Link link = (Link) context.convertAnother(null, Link.class);
        links.add(link);
      }
      reader.moveUp();
    }
    return new SurveyResource(id, active, questions, links);   
  }
}
