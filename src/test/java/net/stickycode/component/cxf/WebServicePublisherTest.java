package net.stickycode.component.cxf;

import javax.inject.Inject;
import javax.jws.WebService;

import net.stickycode.component.cxf.v1.DummyWebService;
import net.stickycode.component.cxf.v1.WebServiceContract;
import net.stickycode.mockwire.MockwireContainment;
import net.stickycode.mockwire.Uncontrolled;
import net.stickycode.mockwire.UnderTest;
import net.stickycode.mockwire.junit4.MockwireRunner;

import org.apache.cxf.Bus;
import org.apache.cxf.BusFactory;
import org.apache.cxf.bus.CXFBusImpl;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(MockwireRunner.class)
//@MockwireContainment//({"/META-INF/cxf/cxf.xml", "/META-INF/cxf/cxf-servlet.xml"})
public class WebServicePublisherTest {

  @WebService
  public interface WS {
  }

  public class A
      implements WS {
  }

  @Uncontrolled
  WebServiceExposureRepository repository;
  
  @UnderTest
  WebServicePublisher publisher;
  
  @UnderTest
  public Bus createBus() {
    return BusFactory.getDefaultBus();
  }

  @Test(expected = WebServiceShouldExistInVersionedPackageException.class)
  public void enforceVersionedPackages() {
    repository.add(new A(), WS.class);
    publisher.exposeWebservices();
  }

  @Test
  public void check() {
    repository.add(new DummyWebService(), WebServiceContract.class);
    publisher.exposeWebservices();
  }
}
