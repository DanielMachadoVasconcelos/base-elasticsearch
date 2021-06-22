package br.com.ead.personsearch.services;

import br.com.ead.personsearch.model.PersonEntity;
import br.com.ead.personsearch.repositories.PersonRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.ElasticsearchException;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class PersonService {

    CircuitBreakerFactory circuitBreakerFactory;
    PersonRepository repository;

    @Retryable(value = ElasticsearchException.class, maxAttempts = 2, backoff = @Backoff(delay = 100))
    public PersonEntity save(PersonEntity person) {
        var circuitBreaker = circuitBreakerFactory.create("person-service-save");
        return circuitBreaker.run(() -> repository.save(person));
    }

    @Recover
    void recover(ElasticsearchException exception, PersonEntity person){
        log.error("There was a error while saving the a person. " +
                "The service will send this request to a external topic for further analysis. Person: {}, Exception: {}",
                person, exception);
    }
}
