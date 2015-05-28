package com.example.e3.with.di.internal;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.namespace.IdentityNamespace;
import org.osgi.framework.wiring.BundleCapability;
import org.osgi.framework.wiring.FrameworkWiring;
import org.osgi.resource.Namespace;
import org.osgi.resource.Requirement;
import org.osgi.resource.Resource;

public final class Activator implements BundleActivator {
  
  private static volatile FrameworkWiring frameworkWiring;
  
  @Override
  public void start(BundleContext context) throws Exception {
    frameworkWiring = context.getBundle(Constants.SYSTEM_BUNDLE_ID).adapt(FrameworkWiring.class);
  }

  @Override
  public void stop(BundleContext context) throws Exception {
    frameworkWiring = null;
  }
  
  static <T> Class<T> loadClass(final String bundleName, String className){
    // there are different solutions for different OSGi versions
    // *) OSGi Version < r4v43 where PackageAdmin isn't deprecated: org.osgi.service.packageadmin.PackageAdmin.getBundles(String, String)
    // *) OSGi Version > r4v43 and < r6 or if you want to be OSGi version independent: use an BundleTracker implementation similar to org.eclipse.e4.ui.internal.workbench.BundleFinder
    // *) OSGi Version >= r6 where FrameworkWiring#findProviders(Requirement) exists: use this implementation
    
    FrameworkWiring wiring = frameworkWiring;
    if( wiring == null ){
      return null; // or throw exception what ever you prefer
    }
    
    // build FrameworkWiring search criteria to find a specific bundle
    Requirement requirement = new BundleNameRequirement(bundleName);
    Collection<BundleCapability> capabilities = wiring.findProviders(requirement);
    
    if( capabilities == null ){
      return null; // or throw exception what ever you prefer
    }
    
    // we didn't specify a version for simplicity so we take the bundle with the highest version
    Bundle bundle = null;
    for( BundleCapability capability : capabilities ){
      Bundle b = capability.getRevision().getBundle();
      if( bundle == null || b.getVersion().compareTo(bundle.getVersion()) > 0 ){
        bundle = b;
      }
    }
    
    if( bundle == null ){
      return null; // or throw exception what ever you prefer
    }
    
    try {
      // load the class via the bundle
      @SuppressWarnings("unchecked")
      Class<T> cls = (Class<T>)bundle.loadClass(className);
      return cls;
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
      return null; // or just forward the exception
    }
  }

  private static final class BundleNameRequirement implements Requirement{
//    private static final String REQUIREMNT_FILTER = "(&(" + IdentityNamespace.IDENTITY_NAMESPACE + "=%s)(" + IdentityNamespace.CAPABILITY_VERSION_ATTRIBUTE + "=%s))"; // would also take the bundle version into account
    private static final String REQUIREMNT_FILTER = "(" + IdentityNamespace.IDENTITY_NAMESPACE + "=%s)";
    
    private final Map<String, String> directives;
    
    public BundleNameRequirement(String bundleName){
      Objects.requireNonNull(bundleName, "No bundle symbolic name given!");
      directives = Collections.<String, String> singletonMap(Namespace.REQUIREMENT_FILTER_DIRECTIVE, String.format(REQUIREMNT_FILTER, bundleName));
    }
    
    @Override
    public Resource getResource() {
      return null;  // requirement is synthesized hence null
    }
    
    @Override
    public String getNamespace() {
      return IdentityNamespace.IDENTITY_NAMESPACE;
    }
    
    @Override
    public Map<String, String> getDirectives() {
      return directives;
    }
    
    @Override
    public Map<String, Object> getAttributes() {
      return Collections.<String, Object>emptyMap();
    }
  }
}
