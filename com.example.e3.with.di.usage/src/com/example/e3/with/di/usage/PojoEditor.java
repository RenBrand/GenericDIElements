package com.example.e3.with.di.usage;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.ui.di.Focus;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;

public class PojoEditor {
  
  private Text input;
  
  @Inject
  public void setEditorInput(IEditorInput editorInput){
    System.out.println("got editor input injected: " + editorInput);
  }
  
  @PostConstruct
  public void initUI(Composite parent){
    Composite p = new Composite(parent, SWT.NONE);
    p.setLayout(new RowLayout());
    Label l = new Label(p, SWT.NONE);
    l.setText("Some input: ");
    input = new Text(p, SWT.NONE);
  }
  
  @Focus
  public void setFocus(){
    if( input != null && !input.isDisposed() ){
      input.setFocus();
    }
  }
}
