package br.com.ead.personsearch.services;

import br.com.ead.personsearch.model.PersonEntity;
import br.com.ead.personsearch.producers.PersonProducer;
import br.com.ead.personsearch.repositories.PersonRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.CircuitBreaker;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class PersonService {

    PersonRepository repository;
    PersonProducer producer;

    @Retryable(label = "person-service-save-retry")
    @CircuitBreaker(label = "person-service-save-circuit-breaker")
    public PersonEntity save(PersonEntity person) {

        var result = repository.save(person);
        producer.sendMessage(result);
        return result;
    }

    public Optional<PersonEntity> findById(String uuid) {
        return repository.findById(uuid);
    }
}
