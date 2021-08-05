package br.com.ead.personsearch.consumers;

import br.com.ead.personsearch.model.PersonEntity;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class PersonConsumer {

    @KafkaListener(topics = "users", groupId = "group_id")
    public void consume(PersonEntity entity){
        log.info(String.format("#### -> Consumed entity -> %s", entity));
    }
}
