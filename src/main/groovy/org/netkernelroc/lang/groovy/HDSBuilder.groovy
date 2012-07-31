package org.netkernelroc.lang.groovy

import java.lang.reflect.Field;
import java.util.Map;

import org.netkernel.layer0.representation.IHDSNode
import org.netkernel.layer0.representation.impl.HDSNodeImpl;

class HDSBuilder extends BuilderSupport {

  @Override
  protected Object createNode(Object name) {
    return new HDSNodeImpl(name, null)
  }

  @Override
  protected Object createNode(Object name, Object value) {
    return new HDSNodeImpl(name, value, null)
  }

  @Override
  protected Object createNode(Object name, Map attributes) {
    return createNode(name, attributes, null)
  }

  @Override
  protected Object createNode(Object name, Map attributes, Object value) {
    def n = new HDSNodeImpl(name, null)
    attributes.each { k, v ->
      setParent(n, new HDSNodeImpl("@${k}", v, null))
    }
    return n;
  }

  @Override
  protected void setParent(Object parent, Object child) {
    // Need to play a little reflection game here to set the parent
    Field f = HDSNodeImpl.class.getDeclaredField("mParent")
    f.setAccessible(true)
    f.set(child, parent)

    if(parent.children) {
      // Need to copy current children array to new one
      IHDSNode[] newChildren = new IHDSNode[parent.children.size() + 1]
      (0..(parent.children.size()-1)).each { index ->
        newChildren[index] = parent.children[index]
      }
      newChildren[parent.children.size()] = child
      parent.children = newChildren
    } else {
      parent.children = [child]as IHDSNode[]
    }
  }
}
