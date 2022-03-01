package com.example.springapplicationforfileoperation.responses;

import lombok.Data;

@Data
public class ResponseForGetById{

    private String status;
    private Response data;

    public ResponseForGetById(String status, Response build) {
        this.status = status;
        this.data = build;
    }
}
