package org.netkernelroc.lang.groovy

import org.netkernel.layer0.representation.impl.HDSNodeImpl

class HDSNodeImplDecorator {

  static void decorate() {
    
    MetaClass metaClass = new ExpandoMetaClass(HDSNodeImpl.class);
    
    metaClass.children() {
      delegate.children
    }
    
    metaClass.getProperty = { propertyName ->
      println propertyName
    }
    
    
    GroovySystem.metaClassRegistry.setMetaClass(metaClass)
    
  }

}
