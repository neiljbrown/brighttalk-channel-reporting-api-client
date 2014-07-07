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

import com.brighttalk.channels.reportingapi.v1.client.resource.Question;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 * {@link Converter XStream Converter} which supports unmarshalling representations of a {@link Question}.
 */
public class QuestionXStreamConverter implements Converter {

  /** {@inheritDoc} */
  @Override
  @SuppressWarnings("rawtypes")  
  public boolean canConvert(Class type) {
    return type.equals(Question.class);
  }

  /** {@inheritDoc} */
  @Override
  public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
    throw new UnsupportedOperationException("Marshalling not currently supported.");    
  }

  /** {@inheritDoc} */
  @Override
  public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {    
    int id = Integer.parseInt(reader.getAttribute("id"));
    String reportingId = reader.getAttribute("reportingId");
    String type = reader.getAttribute("type"); 
    boolean required = reader.getAttribute("required") != null ? Boolean.parseBoolean(reader.getAttribute("required")) : false;
    boolean deleted = reader.getAttribute("deleted") != null ? Boolean.parseBoolean(reader.getAttribute("deleted")) : false;    
    String text = null;
    List<String> options = null;
    List<String> answers = null;
    while (reader.hasMoreChildren()) {
      reader.moveDown();
      String nodeName = reader.getNodeName();
      if (nodeName.equals("text")) {
        text = reader.getValue();
      } else if (nodeName.equals("options")) {
        if (options == null) {
          options = new ArrayList<>();
        }
        while (reader.hasMoreChildren()) {
          reader.moveDown();
          options.add(reader.getValue());
          reader.moveUp();          
        }
      } else if (nodeName.equals("answers")) {
        if (answers == null) {
          answers = new ArrayList<>();
        }
        while (reader.hasMoreChildren()) {
          reader.moveDown();
          answers.add(reader.getValue());
          reader.moveUp();          
        }        
      }
      reader.moveUp();
    }
    Question q = new Question(id, reportingId, type, text, options);
    q.setRequired(required);
    q.setDeleted(deleted);
    if (answers != null) {
      q.setAnswers(answers);
    }
    return q;
  }
}