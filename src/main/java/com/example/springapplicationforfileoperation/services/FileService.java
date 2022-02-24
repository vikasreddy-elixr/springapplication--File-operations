package com.example.springapplicationforfileoperation.services;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;


public interface FileService {

    ResponseEntity<?> fileUpload(MultipartFile multipartFile, String userName);

    ResponseEntity<?> getFileById(UUID uuid);

    ResponseEntity<?> getFilesByUserName(String userName);

}
