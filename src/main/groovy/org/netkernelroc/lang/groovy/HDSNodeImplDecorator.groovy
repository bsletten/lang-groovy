package org.netkernelroc.lang.groovy

import org.netkernel.layer0.representation.impl.HDSNodeImpl
import org.netkernel.layer0.util.HDSXPath

class HDSNodeImplDecorator {

  static void decorate() {
    
    ExpandoMetaClass metaClass = new ExpandoMetaClass(HDSNodeImpl.class);
    
    metaClass.children = {
      delegate.children
    }
    
    metaClass.children = { 
      delegate.getChildren()
    }
    
    metaClass.depthFirst = {
      def xpathResult = []
      HDSXPath.eval(xpathResult, delegate, "//*")
      return xpathResult
    }
    
    metaClass.name = {
      delegate.getName()
    }
    
    metaClass.text = {
      delegate.getValue()
    }
    
    metaClass.propertyMissing = { String name ->
      def result
      
      def xpathResult = []
      HDSXPath.eval(xpathResult, delegate, name)
      
      if(xpathResult.size() == 1) {
        if(name.startsWith("@")) {
          result = xpathResult[0].getValue()
        } else {
          result = xpathResult[0]
        }
      } else {
        result = xpathResult
      }
      
      return result
      
    }
    
    metaClass.initialize()
    
    GroovySystem.metaClassRegistry.setMetaClass(HDSNodeImpl.class, metaClass)
  }

}
