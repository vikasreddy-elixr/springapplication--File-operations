package com.example.springapplicationforfileoperation.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
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
    private LocalDateTime localDateTime = LocalDateTime.now();

    public FileInfo(String id ,String userName, String fileName) {
        this.userName = userName;
        this.fileName = fileName;
        this.id = UUID.fromString(id);
    }
}
