package br.com.ead.base.elasticsearch.subsystemtests.spock

//import org.junit.jupiter.api.Test
import spock.lang.Specification

class PersonSearchTest extends Specification {

//    @Test
    void 'Should run this test' () {
        given:
        def result = Math.random().intValue()

        when:
        result / 10

        then:
        result == result / 10
    }

    void 'Should also run this test' () {
        given:
        def result = Math.random().intValue()

        when:
        result / 10

        then:
        result == 10
    }

    void 'Should maybe run this test' () {
        given:
        def result = Math.random().intValue()

        when:
        result / 10

        then:
        result == result
    }
}
