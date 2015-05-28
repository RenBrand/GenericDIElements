package com.example.e3.with.di;

import java.net.URI;
import java.net.URISyntaxException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.core.runtime.IExecutableExtensionFactory;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.e4.tools.compat.parts.DIEditorPart;
import org.eclipse.e4.tools.compat.parts.DIHandler;
import org.eclipse.e4.tools.compat.parts.DIViewPart;

import com.example.e3.with.di.internal.DIHelper;

/**
 * A configurable ExtensionRegistry factory which provides a generic way to define a DIViewPart, a DIHandler or a DIEditorPart.
 * 
 * This factory allows to create Views/Editors/Handlers by only referencing the POJO-classes in the extension. To define the
 * POJO class the following formats are allowed:
 * <ul>
 * 	<li><b>full qualified class name:</b> tries to load the class from the bundle which provides the extension</li>
 * 	<li><b><code>bundleclass://&lt;bundle-symbolic-name&gt/&lt;full-qualified-class-name&gt;</code>-URI:</b>
 * 		tries to load the class from the bundle which was mentioned in the URI host part</li>
 * </ul>
 *
 */
public class DIFactoryForE3Elements implements IExecutableExtension, IExecutableExtensionFactory {
  
  /* The extension points we want to support. */
  static enum Type {
    EDITOR("org.eclipse.ui.editors"),
    VIEW("org.eclipse.ui.views"),
    HANDLER("org.eclipse.ui.handlers");
    
    private final String extensionPointID;
    
    private Type(String extensionPointID){
      this.extensionPointID = extensionPointID;
    }
    
    public static Type find(IConfigurationElement configElement){
      String extenstionPointID = configElement.getDeclaringExtension().getExtensionPointUniqueIdentifier();
      for( Type t : Type.values() ){
        if( t.extensionPointID.equals( extenstionPointID ) ){
          return t;
        }
      }
      
      return null;
    }
  }
  
  private Type type;
  private Class<?> pojoClass;

  @Override
  public void setInitializationData(IConfigurationElement config, String propertyName, Object data) throws CoreException {
    if( !( data instanceof String ) ){
      throw new CoreException(new Status(IStatus.ERROR, config.getContributor().getName(), "No class reference speciefied in " + config.getAttribute("") + "!"));
    }
    
    String classRef = (String) data;
    
    // is the data represented as "bundleclass://<bundle-symbolicname>/<class-name>"? 
    if( DIHelper.isBundleClassURI(classRef) ){
      try {
        URI contributionURI = new URI((String)data);
        pojoClass = DIHelper.loadClass(contributionURI);
      } catch (URISyntaxException e) {
        throw new CoreException(new Status(IStatus.ERROR, config.getContributor().getName(), e.getMessage(), e));
      }
    } else {
      // load the class form the bundle which provides/defines the extension
      pojoClass = DIHelper.loadClass(config.getContributor().getName(), classRef);
    }
    
    type = Type.find(config);
    if( type == null ){
      throw new CoreException(new Status(IStatus.ERROR, config.getContributor().getName(), "Facoty only knows views, editors and handlers!"));
    }
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Override
  public Object create() throws CoreException {
    switch( type ) {
      case EDITOR:
        return new DIEditorPart(pojoClass) {};
        
      case HANDLER:
        return new DIHandler<>(pojoClass);
        
      case VIEW:
        return new DIViewPart(pojoClass) {};
    }
    
    throw new CoreException(new Status(IStatus.ERROR, "com.example.e3.with.di", "Unknown Type " + type));
  }
  
  
}
