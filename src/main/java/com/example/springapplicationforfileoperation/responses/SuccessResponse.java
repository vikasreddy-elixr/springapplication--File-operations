package com.example.springapplicationforfileoperation.responses;

import com.example.springapplicationforfileoperation.model.FileInfoDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class SuccessResponse {

    private String status;
    private UUID id;
    private String message;
    private String userName;
    private LocalDateTime uploadTime;
    private String fileName;
    private String content;
    private List<FileInfoDTO> files;
    private SuccessResponse file;

    public SuccessResponse(String status, SuccessResponse build) {
        this.status = status;
        this.file = build;
    }
}
