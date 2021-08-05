package br.com.ead.personsearch.producers;

import br.com.ead.personsearch.model.PersonEntity;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@AllArgsConstructor
public class PersonProducer {

    private static final String TOPIC = "persons";

    KafkaTemplate<String, PersonEntity> kafkaTemplate;

    public void sendMessage(PersonEntity entity) {
        log.info("Sending data='{}' to topic='{}'", entity, TOPIC);
        this.kafkaTemplate.send(TOPIC, entity.getUuid(), entity);
    }
}
