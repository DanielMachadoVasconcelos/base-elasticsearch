package br.com.ead.personsearch.service

import br.com.ead.personsearch.model.PersonEntity
import br.com.ead.personsearch.services.PersonService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate
import spock.lang.Specification

import java.time.LocalDate

@SpringBootTest
class PersonServiceTest extends Specification {

    @Autowired
    private PersonService classUnderTest

    @Autowired
    private ElasticsearchRestTemplate template

    @Test
    def 'Should correctly save a person into database'() {

        given: 'A random Jhon Doe person'
        def expectedId = UUID.randomUUID().toString()
        def expectedName = 'Jhon Doe'
        def expectedDateOfBirth = LocalDate.now().minusYears(21L)
        def person = new PersonEntity(expectedId, expectedName, expectedDateOfBirth)

        when: 'Saving the person in the database'
        classUnderTest.save(person)

        then: 'The Jhon Does is available to search'
        def entity = template.get(expectedId, PersonEntity)

        and: 'All data points are as expected'
        verifyAll entity, {
            it.uuid == expectedId
            it.name == expectedName
            it.dateOfBirth == expectedDateOfBirth
        }
    }
}
