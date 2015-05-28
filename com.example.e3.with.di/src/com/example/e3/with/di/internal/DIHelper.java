package com.example.e3.with.di.internal;

import java.net.URI;

/**
 * Utility class.
 */
public final class DIHelper {
  private DIHelper(){
    throw new IllegalArgumentException("Instantiation is not allowed!");
  }
  
  private static final String BUNLECLASS_SCHEMA = "bundleclass";
  private static final String BUNLECLASS_SCHEMA_PRERIX = BUNLECLASS_SCHEMA + "://";  
  
  /**
   * Checks if the given string represents a {@code "bundleclass://"} URI.
   * @param uri the string to check
   * @return true if the given string represents a {@code "bundleclass://"} URI; false otherwise
   */
  public static boolean isBundleClassURI(String uri){
    return uri != null && uri.startsWith(BUNLECLASS_SCHEMA_PRERIX);
  }

  /**
   * Loads the class represented by the given {@code "bundleclass://"} URI.
   * @param bundleClassURI an URI which points to the class to load
   * @return the loaded class or {@code null} if it couldn't be loaded
   * @throws IllegalArgumentException if the given URI is not a {@code "bundleclass://"} URI.
   */
  public static <T> Class<T> loadClass(URI bundleClassURI){
    if(!bundleClassURI.getScheme().equals(BUNLECLASS_SCHEMA)){
      throw new IllegalArgumentException("Not supported URI schema! (uri: " + bundleClassURI + ')');
    }
    
    String symbolicName = bundleClassURI.getHost();
    String className = bundleClassURI.getPath();
    
    /* HINT for bundle version
         -) org.osgi.framework.Version#valueOf(String) for parsing a version
         -) org.osgi.framework.VersionRange#valueOf(String) for parsing a version range
     */
    // String bundleVersion = bundleClassURI.getQuery(); // e.g.: v=[3.0.0,4.0.0) or v=3.2.0
    
    if(className.charAt(0) == '/' ){
      // remove leading slash
      className = className.substring(1);
    }
    
    return loadClass(symbolicName, className);
  }
  
  /**
   * Tries to load a class from the named bundle. 
   * @param bundleName the symbolic name of the bundle which should be asked to load the class 
   * @param className the name of the class to load
   * @return the loaded class or {@code null} if it couldn't be loaded
   */
  public static <T> Class<T> loadClass(String bundleName, String className){
    return Activator.loadClass(bundleName, className);
  }
}
