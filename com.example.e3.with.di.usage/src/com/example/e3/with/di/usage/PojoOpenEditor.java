package com.example.e3.with.di.usage;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;

public class PojoOpenEditor {
  
  private static final class NullEditorInput implements IEditorInput {
    private NullEditorInput(){}

    @Override
    public Object getAdapter(@SuppressWarnings("rawtypes") Class adapter) {
      return null;
    }

    @Override
    public boolean exists() {
      return false;
    }

    @Override
    public ImageDescriptor getImageDescriptor() {
      return null;
    }

    @Override
    public String getName() {
      return "NullInput";
    }

    @Override
    public IPersistableElement getPersistable() {
      return null;
    }

    @Override
    public String getToolTipText() {
      return "No Editor Input";
    }
    
  }
  
  @Execute
  public void open(IWorkbenchPage page){
    try {
      page.openEditor(new NullEditorInput(), "com.example.e3.with.di.usage.pojoeditor");
    } catch (PartInitException e) {
      e.printStackTrace();
    }
  }
}
