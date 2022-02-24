package com.example.springapplicationforfileoperation.controller;

import com.example.springapplicationforfileoperation.contants.Constants;
import com.example.springapplicationforfileoperation.model.FileInfo;
import com.example.springapplicationforfileoperation.responses.ErrorResponse;
import com.example.springapplicationforfileoperation.services.FileServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import java.util.Objects;
import java.util.UUID;

@Validated
@RestController
public class FileController {

    private final FileServiceImpl fileServiceImpl;

    public FileController(FileServiceImpl fileServiceImpl) {
        this.fileServiceImpl = fileServiceImpl;
    }

    @PostMapping(value = "/upload")
    public ResponseEntity<?> FileUpload(@RequestParam("file") MultipartFile multipartfile, @RequestParam("username") @NotEmpty String userName) {
        if (Objects.equals(multipartfile.getContentType(), "text/plain")) {
            return fileServiceImpl.fileUpload(multipartfile, userName);
        }
        return new ResponseEntity<>(new ErrorResponse(Constants.FAILURE, Constants.ERROR_FILE_FORMAT_NOT_SUPPORTED), HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/file/{id}")
    public ResponseEntity<?> getFileById(@PathVariable String id) {
        UUID uuid = UUID.fromString(id);
        return fileServiceImpl.getFileById(uuid);
    }

    @GetMapping("/file/user/{userName}")
    public ResponseEntity<?> getFilesByUserName(@PathVariable(name = "userName") String userName) {
        return fileServiceImpl.getFilesByUserName(userName);
    }

}
