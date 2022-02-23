package com.example.springapplicationforfileoperation.services;

import com.example.springapplicationforfileoperation.model.FileInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;


public interface FileService {

   ResponseEntity<?> fileUpload(MultipartFile multipartFile, FileInfo fileInfo, String userName);
   ResponseEntity<?> getFileById(String id);
   ResponseEntity<?> getFilesByUserName(String userName);

}
