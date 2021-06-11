package br.com.ead.personsearch.services;

import br.com.ead.personsearch.model.PersonEntity;
import br.com.ead.personsearch.repositories.PersonRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PersonService {

    PersonRepository repository;

    public PersonEntity save(PersonEntity person) {
        return repository.save(person);
    }
}
