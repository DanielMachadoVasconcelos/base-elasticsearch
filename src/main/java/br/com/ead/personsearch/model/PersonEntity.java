package br.com.ead.personsearch.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.elasticsearch.index.VersionType;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@Document(indexName = "persons", versionType = VersionType.INTERNAL)
public class PersonEntity {

    @Id
    String uuid;

    @Field(name = "name", type = FieldType.Keyword)
    String name;

    @Field(name = "date_of_birth", type = FieldType.Date)
    LocalDate dateOfBirth;

}
