package com.example.springapplicationforfileoperation.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class FileInfoDTO {

    private UUID id;
    private String fileName;
    private LocalDateTime localDateTime;
    private boolean isFilePresent;
}
