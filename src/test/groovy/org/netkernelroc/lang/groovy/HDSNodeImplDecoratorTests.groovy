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
    
    def records = new HDSBuilder().records {
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

    def allRecords = records.car
    assert 3 == allRecords.size()
    def allNodes = records.depthFirst().collect{ it }
    assert 22 == allNodes.size()
    def firstRecord = records.car[0]
    assert 'car' == firstRecord.name()
    
    def a = firstRecord.'@make'
    
    assert 'Holden' == firstRecord.@make.text()
    assert 'Australia' == firstRecord.country.text()
    def carsWith_e_InMake = records.car.findAll{ it.@make.text().contains('e') }
    assert carsWith_e_InMake.size() == 2
    // alternative way to find cars with 'e' in make
    assert 2 == records.car.findAll{ it.@make =~ '.*e.*' }.size()
    // makes of cars that have an 's' followed by an 'a' in the country
    assert ['Holden', 'Peel'] == records.car.findAll{ it.country =~ '.*s.*a.*' }.@make.collect{ it.text() }
    def expectedRecordTypes = ['speed', 'size', 'price']
    assert expectedRecordTypes == records.depthFirst().grep{ it.@type != '' }.'@type'*.text()
    assert expectedRecordTypes == records.'**'.grep{ it.@type != '' }.'@type'*.text()
    def countryOne = records.car[1].country
    assert 'Peel' == countryOne.parent().@make.text()
    assert 'Peel' == countryOne.'..'.@make.text()
    // names of cars with records sorted by year
    def sortedNames = records.car.list().sort{ it.@year.toInteger() }.'@name'*.text()
    assert ['Royale', 'P50', 'HSV Maloo'] == sortedNames
    assert ['Australia', 'Isle of Man'] == records.'**'.grep{ it.@type =~ 's.*' }*.parent().country*.text()
    assert 'co-re-co-re-co-re' == records.car.children().collect{ it.name()[0..1] }.join('-')
    assert 'co-re-co-re-co-re' == records.car.'*'.collect{ it.name()[0..1] }.join('-')
    
  }
}
