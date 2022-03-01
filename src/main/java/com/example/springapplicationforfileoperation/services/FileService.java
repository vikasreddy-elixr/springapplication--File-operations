package com.example.springapplicationforfileoperation.services;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;



public interface FileService {

    ResponseEntity<?> fileUpload(MultipartFile multipartFile, String userName);

    ResponseEntity<?> getFileById(String id);

    ResponseEntity<?> getFilesByUserName(String userName);

}
