package com.example.springapplicationforfileoperation.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class ErrorResponse {

    private String success;
    private String message;
}