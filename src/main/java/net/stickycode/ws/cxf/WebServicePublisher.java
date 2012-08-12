package net.stickycode.ws.cxf;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PreDestroy;
import javax.inject.Inject;

import net.stickycode.stereotype.StickyComponent;
import net.stickycode.stereotype.configured.PostConfigured;

import org.apache.cxf.Bus;
import org.apache.cxf.binding.soap.SoapBindingConstants;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.jaxws.JaxWsServerFactoryBean;
import org.apache.cxf.jaxws.support.JaxWsServiceFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@StickyComponent
public class WebServicePublisher {

  private Logger log = LoggerFactory.getLogger(WebServicePublisher.class);

  @Inject
  private Bus busProvider;

  @Inject
  private WebServiceExposureRepository exposures;
  
  @Inject
  private WebServiceNamingStrategy nameStrategy;

  private List<EndpointImpl> webServices = new ArrayList<EndpointImpl>();

  @PostConfigured
  public void exposeWebservices() {
    log.info("exposing web services");
    for (WebServiceExposure e : exposures) {
      log.info("publish {}", e.bean());
      publish(e.bean(), e.name(), e.contract());
    }
  }

  @PreDestroy
  public void shutdown() {
    for (EndpointImpl i : webServices) {
      i.stop();
    }
  }

  private void publish(Object bean, String beanName, Class<?> i) {
    log.info("Publishing {} as web service {}", beanName, i.getSimpleName());
    String address = nameStrategy.deriveAddress(bean, i);
    JaxWsServerFactoryBean factory = createServerFactory(i);
    factory.setServiceClass(i);

    EndpointImpl endpointImpl = new EndpointImpl(getBus(), bean, factory);
    endpointImpl.publish(address);
    webServices.add(endpointImpl);
  }

  private Bus getBus() {
    return busProvider;
  };

  protected JaxWsServerFactoryBean createServerFactory(Class<?> i) {
    JaxWsServerFactoryBean serverFactory = new JaxWsServerFactoryBean();
    serverFactory.setBus(getBus());
    serverFactory.setServiceFactory(createServiceFactory(i));
    serverFactory.setBindingId(SoapBindingConstants.SOAP11_BINDING_ID);
    return serverFactory;
  }

  protected JaxWsServiceFactoryBean createServiceFactory(Class<?> i) {
    JaxWsServiceFactoryBean s = new JaxWsServiceFactoryBean();
    s.setBus(getBus());
    URL classpathWsdl = i.getResource(i.getSimpleName() + ".wsdl");
    if (classpathWsdl != null)
      s.setWsdlURL(classpathWsdl);

    return s;
  }

}
