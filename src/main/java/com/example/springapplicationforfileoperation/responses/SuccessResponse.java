package com.example.springapplicationforfileoperation.responses;

import com.example.springapplicationforfileoperation.model.FileInfoDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class SuccessResponse {
    private String status;
    private UUID id;
    private String message;
    private String userName;
    private LocalDateTime localDateTime;
    private String fileName;
    private String content;
    private List<FileInfoDTO> files;

    public SuccessResponse(String status, UUID id) {
        this.status = status;
        this.id = id;
    }

    public SuccessResponse(String status, String userName, LocalDateTime localDateTime, String fileName, String content) {
        this.status = status;
        this.userName = userName;
        this.localDateTime = localDateTime;
        this.fileName = fileName;
        this.content = content;
    }

    public SuccessResponse(String status, String userName, List<FileInfoDTO> fileInfoDto) {
        this.status = status;
        this.userName = userName;
        this.files = fileInfoDto;

    }

}
