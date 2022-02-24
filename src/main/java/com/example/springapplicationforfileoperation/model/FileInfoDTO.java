package com.example.springapplicationforfileoperation.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class FileInfoDTO {

    private UUID id;
    private String fileName;
    private LocalDateTime localDateTime;
}
