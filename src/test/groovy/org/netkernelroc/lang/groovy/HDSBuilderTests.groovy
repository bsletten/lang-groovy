package org.netkernelroc.lang.groovy;

import org.junit.Test;

class HDSBuilderTests {

  @Test
  void simpleHdsTreeSuccess() {
    def builder = new HDSBuilder()

    def hds = builder.config {
      http {
        port 8080
        host "localhost"
        protocol "http"
      }
    }

    assert hds.getFirstValue("//http/port") == 8080
  }

  @Test
  void hdsBuilderWithAttributes() {

    def builder = new HDSBuilder()

    def hds = builder.modules {
      module(id: "lang-groovy") {
        spaces {
          space(id: "urn:org:netkernelroc:lang:groovy")
          space(id: "urn:org:netkernelroc:lang:groovy:doc")
        }
      }
    }
    
    assert hds.getFirstValue("//space/@id")[0] == "urn:org:netkernelroc:lang:groovy"
  }
}
