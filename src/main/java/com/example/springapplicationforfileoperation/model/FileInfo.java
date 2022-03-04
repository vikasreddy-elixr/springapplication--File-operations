package com.example.springapplicationforfileoperation.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import javax.persistence.GeneratedValue;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Document(value = "fileInfo")
public class FileInfo {

    @MongoId(FieldType.STRING)
    @GeneratedValue
    private UUID id = UUID.randomUUID();
    @NotEmpty
    private String userName;
    @NotEmpty
    private String fileName;
    private  LocalDateTime localDateTime = LocalDateTime.now();
}
