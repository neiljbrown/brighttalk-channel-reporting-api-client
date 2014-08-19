/*
 * Copyright 2014-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/**
 * Package containing the classes which make up the BrightTALK reporting API resource model - the RESTful 
 * resource-oriented view of the BrightTALK domain.
 */
// Use a custom JAXB XmlAdapter for unmarshalling integer strings to overcome a bug in the one supplied in the JAXB RI
@XmlJavaTypeAdapter(type = int.class, value = IntegerXmlAdapter.class)
package com.neiljbrown.brighttalk.channels.reportingapi.v1.client.resource;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.neiljbrown.brighttalk.channels.reportingapi.v1.client.jaxb.IntegerXmlAdapter;
