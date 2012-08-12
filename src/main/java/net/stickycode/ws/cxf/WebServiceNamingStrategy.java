package net.stickycode.ws.cxf;

import net.stickycode.stereotype.plugin.StickyStrategy;

@StickyStrategy
public class WebServiceNamingStrategy {

  public String deriveAddress(Object bean, Class<?> i) {
    return "/" + i.getSimpleName() + getLeaf(i.getPackage());
  }
  
  String getLeaf(Package p) {
    String name = p.getName();
    String version = name.substring(name.lastIndexOf('.') + 1);
    if (version.matches("^v[\\.0-9]+$"))
      return "/" + version;
  
    throw new WebServiceShouldExistInVersionedPackageException(p);
  }

}
