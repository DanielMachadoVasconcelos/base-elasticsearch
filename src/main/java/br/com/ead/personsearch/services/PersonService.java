package br.com.ead.personsearch.services;

import br.com.ead.personsearch.model.PersonEntity;
import br.com.ead.personsearch.repositories.PersonRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.CircuitBreaker;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class PersonService {

    PersonRepository repository;

    @CircuitBreaker(label = "person-service-save")
    public PersonEntity save(PersonEntity person) {
        return repository.save(person);
    }

    public Optional<PersonEntity> findById(String uuid) {
        return repository.findById(uuid);
    }
}
