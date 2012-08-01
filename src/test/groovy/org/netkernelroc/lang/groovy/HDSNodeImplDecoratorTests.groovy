package org.netkernelroc.lang.groovy

import org.junit.Ignore
import org.junit.Test

class HDSNodeImplDecoratorTests {

  HDSNodeImplDecoratorTests() {
    HDSNodeImplDecorator.decorate()
  }

  // Borrowed from http://groovy.codehaus.org/GPath
  @Test
  @Ignore
  void gpathSuccess() {

    def node = new HDSBuilder().characters {
      props { props "dd" }
      character(id: '1', name: "Wallace") { likes "cheese" }
      character(id: '2', name: "Gromit") { likes "sleep" }
    }

    assert node != null
    assert node.children().size() == 3 //, "Children ${node.children()}"

    def characters = node.character
    println "node:" + node.children().size()
    println "characters:" + node.character.size()
    for (c in characters) {
      println c['@name']
    }

    assert characters.size() == 2

    assert node.character.likes.size() == 2 //, "Likes ${node.character.likes}"

    // lets find Gromit
    def gromit = node.character.find { it['@id'] == '2' }
    assert gromit != null //, "Should have found Gromit!"
    assert gromit['@name'] == "Gromit"

    // lets find what Wallace likes in 1 query
    def answer = node.character.find { it['@id'] == '1' }.likes.text()
    assert answer == "cheese"
  }


  //http://groovy.codehaus.org/Reading+XML+using+Groovy's+XmlSlurper
  @Test
  void longerGPathSuccess() {
    
    def hds = new HDSBuilder().records {
      car(name: 'HSV Maloo', make: 'Holden', year: '2006') {
        country "Australia"
        record(type: 'speed') 'Production Pickup Truck with speed of 271kph'
      }
      car(name: 'P50', make: 'Peel', year: '1962') {
        country 'Isle of Man'
        record(type: 'size') 'Smallest Street-Legal Car at 99cm wide and 59 kg in weight'
      }
      car(name: 'Royale', make: 'Bugatti', year: '1931') {
        country 'France'
        record(type: 'price') 'Most Valuable Car at $15 million'
      }
    }

    println hds
    
  }
}
