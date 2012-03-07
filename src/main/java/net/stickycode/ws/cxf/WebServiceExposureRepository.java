/**
 * Copyright (c) 2011 RedEngine Ltd, http://www.RedEngine.co.nz. All rights reserved.
 *
 * This program is licensed to you under the Apache License Version 2.0,
 * and you may not use this file except in compliance with the Apache License Version 2.0.
 * You may obtain a copy of the Apache License Version 2.0 at http://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the Apache License Version 2.0 is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Apache License Version 2.0 for the specific language governing permissions and limitations there under.
 */
package net.stickycode.ws.cxf;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.stickycode.stereotype.StickyFramework;
import net.stickycode.stereotype.component.StickyRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@StickyRepository
@StickyFramework
public class WebServiceExposureRepository
    implements Iterable<WebServiceExposure> {

  private Logger log = LoggerFactory.getLogger(WebServiceExposureRepository.class);

  private List<WebServiceExposure> exposures = new ArrayList<WebServiceExposure>();

  public void add(Object bean, Class<?> i) {
    log.info("found {}", bean);
    exposures.add(new WebServiceExposure(bean, i));
  }

  @Override
  public Iterator<net.stickycode.ws.cxf.WebServiceExposure> iterator() {
    return exposures.iterator();
  }
}
