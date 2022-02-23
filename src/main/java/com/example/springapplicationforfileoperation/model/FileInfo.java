package com.example.springapplicationforfileoperation.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import javax.persistence.GeneratedValue;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Document(value = "fileInfo")
public class FileInfo {

    @MongoId
    @GeneratedValue
    private UUID id= UUID.randomUUID();
    @NotEmpty
    private String userName;
    @NotEmpty
    private String fileName;
    private LocalDateTime localDateTime = LocalDateTime.now();
}
