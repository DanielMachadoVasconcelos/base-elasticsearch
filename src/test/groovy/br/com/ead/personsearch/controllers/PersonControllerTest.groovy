package br.com.ead.personsearch.controllers

import br.com.ead.personsearch.model.PersonEntity
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

import java.time.LocalDate

import static br.com.ead.personsearch.controllers.PersonController.PERSON_PATH
import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE
import static org.hamcrest.CoreMatchers.is
import static org.hamcrest.Matchers.hasSize
import static org.hamcrest.core.IsEqual.equalTo
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = 'username', roles = 'USER', password = 'password')
class PersonControllerTest extends Specification {

    @Autowired
    private MockMvc mvc

    @Autowired
    private ObjectMapper objectMapper

    @Test
    def 'Should save a person when receiving a POST request on /person endpoint'() {

        given: 'A random Jhon Doe person'
        def expectedId = UUID.randomUUID()
        def expectedName = 'Jhon Doe'
        def expectedDateOfBirth = LocalDate.now().minusYears(21L)
        def person = new PersonEntity(expectedId, expectedName, expectedDateOfBirth)

        when: 'Saving the person'
        def response = mvc.perform(post(PERSON_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding('utf-8')
                .content(objectMapper.writeValueAsString(person)))

        then: 'The response has the correct response'
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath('$.name').value(expectedName))
                .andExpect(jsonPath('$.uuid', equalTo(expectedId.toString())))
                .andExpect(jsonPath('$.dateOfBirth', is(expectedDateOfBirth.format(ISO_LOCAL_DATE))))
    }

    @Test
    def 'Should not save a person when receiving a invalid POST request on /person endpoint'() {

        given: 'A invalid person'
        def expectedDateOfBirth = LocalDate.now().plusYears(21L)
        def aInvalidPerson = new PersonEntity(null, '', expectedDateOfBirth)

        when: 'Trying to save the person'
        def response = mvc.perform(post(PERSON_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding('utf-8')
                .content(objectMapper.writeValueAsString(aInvalidPerson)))

        then: 'The response is invalid'
        response.andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath('$.errors', hasSize(3)))
                .andExpect(jsonPath("\$.errors[?(@.field == 'uuid')]").exists())
                .andExpect(jsonPath("\$.errors[?(@.field == 'name')]").exists())
                .andExpect(jsonPath("\$.errors[?(@.field == 'dateOfBirth')]").exists())
    }
}
