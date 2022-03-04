package com.example.springapplicationforfileoperation.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ResponseForGetById {

    private String status;
    private Response data;
}
