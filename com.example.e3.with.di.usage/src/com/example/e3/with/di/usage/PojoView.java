package com.example.e3.with.di.usage;

import javax.annotation.PostConstruct;

import org.eclipse.e4.ui.di.Focus;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class PojoView {
  private Label l;
  
  @PostConstruct
  public void initUI(Composite parent){
    System.out.println(parent.getLayout());
    l = new Label(parent, SWT.NONE);
    l.setText("and injected!");
  }
  
  @Focus
  public void setFocus(){
    if( l != null && !l.isDisposed()){
      l.setFocus();
    }
  } 
}
