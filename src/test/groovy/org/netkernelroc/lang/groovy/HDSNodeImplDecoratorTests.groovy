package org.netkernelroc.lang.groovy

import org.junit.Test;

class HDSNodeImplDecoratorTests {

  HDSNodeImplDecoratorTests() {
    HDSNodeImplDecorator.decorate()
  }
  
  // Borrowed from http://groovy.codehaus.org/GPath
  @Test
  void gpathSuccess() {

    def hds = new HDSBuilder().characters {
      props { props "dd" }
      character(id: 1, name: "Wallace") { likes "cheese" }
      character(id: 2, name: "Gromit") { likes "sleep" }
    }


    assert hds != null
    assert hds.children().size() == 3 //, "Children ${hds.children()}"

    def characters = hds.character
    println "hds:" + hds.children().size()
    println "characters:" + hds.character.size()
    for (c in characters) {
      println c['@name']
    }

    assert characters.size() == 2

    assert hds.character.likes.size() == 2 //, "Likes ${hds.character.likes}"

    // lets find Gromit
    def gromit = hds.character.find { it['@id'] == '2' }
    assert gromit != null //, "Should have found Gromit!"
    assert gromit['@name'] == "Gromit"

    // lets find what Wallace likes in 1 query
    def answer = hds.character.find { it['@id'] == '1' }.likes.text()
    assert answer == "cheese"



  }

}
