package com.example.e3.with.di.usage;

import org.eclipse.e4.core.di.annotations.Execute;

public class PojoHandler {

  @Execute
  public void execute(){
    System.out.println("yes: executed!");
  }
}
