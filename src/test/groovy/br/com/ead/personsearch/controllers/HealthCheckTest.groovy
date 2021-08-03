package br.com.ead.personsearch.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@SpringBootTest
@AutoConfigureMockMvc
class HealthCheckTest extends Specification {

    @Autowired
    private MockMvc mvc

    def 'Should respond status code OK and body as UP when requesting the health endpoint'() {

        when:
        def response = mvc.perform(get('/actuator/health')
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding('utf-8'))

        then: 'The response has the correct response'
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath('$.status').value('UP'))

    }

    def 'Should respond circuit break statistics when requesting the metrics endpoint'() {

        when:
        def response = mvc.perform(get('/actuator/metrics')
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding('utf-8'))

        then: 'The response has the correct response'
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
    }
}
