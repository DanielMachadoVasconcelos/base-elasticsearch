package br.com.ead.personsearch.controllers;

import br.com.ead.personsearch.model.PersonEntity;
import br.com.ead.personsearch.services.PersonService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/")
public class PersonController {

    public static final String PERSON_PATH = "/persons";

    PersonService service;

    @PostMapping(PERSON_PATH)
    @ResponseStatus(HttpStatus.CREATED)
    public PersonEntity save(@RequestBody @Valid PersonEntity person) {
        return service.save(person);
    }

    @GetMapping(PERSON_PATH + "/{uuid}")
    public Optional<PersonEntity> findById(@PathVariable("uuid") String uuid) {
        return service.findById(uuid);
    }
}
