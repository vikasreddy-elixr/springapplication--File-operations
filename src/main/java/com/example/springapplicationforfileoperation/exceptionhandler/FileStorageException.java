package com.example.springapplicationforfileoperation.exceptionhandler;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FileStorageException extends RuntimeException {
    private String message;
}
