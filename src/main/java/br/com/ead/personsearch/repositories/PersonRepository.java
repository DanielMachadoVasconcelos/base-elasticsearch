package br.com.ead.personsearch.repositories;

import br.com.ead.personsearch.model.PersonEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface PersonRepository extends ElasticsearchRepository<PersonEntity, String> {
}
