package com.example.springapplicationforfileoperation.services;

import com.example.springapplicationforfileoperation.contants.Constants;
import com.example.springapplicationforfileoperation.exceptionhandler.InternalServerErrorException;
import com.example.springapplicationforfileoperation.exceptionhandler.NotFoundException;
import com.example.springapplicationforfileoperation.model.FileInfo;
import com.example.springapplicationforfileoperation.model.FileInfoDTO;
import com.example.springapplicationforfileoperation.reporsitory.FileRepository;
import com.example.springapplicationforfileoperation.responses.Response;
import com.example.springapplicationforfileoperation.responses.ResponseForGetById;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class FileServiceImpl implements FileService {
    final FileRepository fileRepository;

    public FileServiceImpl(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @Value(Constants.FILE_PATH)
    String filePath;

    @Override
    public ResponseEntity<Response> fileUpload(MultipartFile multipartFile, String userName) {

        FileInfo fileInfo = new FileInfo();
        fileInfo.setFileName(multipartFile.getOriginalFilename());
        fileInfo.setUserName(userName);
        fileRepository.save(fileInfo);
        try {
            Files.copy(multipartFile.getInputStream(), Paths.get(filePath.concat(String.valueOf(fileInfo.getId()))),
                    StandardCopyOption.REPLACE_EXISTING);

        } catch (IOException e) {
            throw new InternalServerErrorException(Constants.ERROR_UNEXPECTED_TYPE);
        }
        return new ResponseEntity<>(Response.builder().status(Constants.SUCCESS).id(fileInfo.getId()).build(), HttpStatus.OK);

    }

    @Override
    public ResponseEntity<ResponseForGetById> getFileById(String id) {
        UUID uuid = UUID.fromString(id);
        FileInfo getFile = fileRepository.findById(UUID.fromString(String.valueOf(uuid))).orElseThrow(() -> new NotFoundException(Constants.ERROR_NOT_FOUND));
        String fileName = getFile.getFileName();
        File file = new File(filePath.concat(String.valueOf(uuid)));

        if (file.exists()) {
            try {
                String data = new String(Files.readAllBytes(Paths.get(filePath.concat(String.valueOf(uuid)))));
                return new ResponseEntity<>(ResponseForGetById.builder().status(Constants.SUCCESS).data(Response.builder()
                        .userName(getFile.getUserName()).uploadTime(getFile.getLocalDateTime())
                        .fileName(fileName).content(data).build()).build(), HttpStatus.OK);
            } catch (IOException e) {
                throw new InternalServerErrorException(Constants.ERROR_UNEXPECTED_TYPE);
            }
        } else {
            throw new NotFoundException(Constants.ERROR_NOT_FOUND);

        }
    }

    @Override
    public ResponseEntity<Response> getFilesByUserName(String userName) {
        List<FileInfo> fileInfoList = fileRepository.findByUserName(userName);
        if (fileInfoList.isEmpty()) {
            throw new NotFoundException(Constants.ERROR_NO_DATA_FOUND);
        }

        return new ResponseEntity<>(Response.builder().status(Constants.SUCCESS).userName(userName)
                .files((fileInfoList.stream().map(this::convertDataIntoDTOForFileExists)
                        .collect(Collectors.toList()))).build(), HttpStatus.OK);
    }


    private FileInfoDTO convertDataIntoDTOForFileExists(FileInfo fileInfo) {
        File file = new File(filePath.concat(String.valueOf(fileInfo.getId())));
        if (file.exists()) {

            return FileInfoDTO.builder().id(fileInfo.getId())
                    .fileName(fileInfo.getFileName())
                    .localDateTime(fileInfo.getLocalDateTime()).fileExistence(Constants.TRUE)
                    .build();
        } else {
            return FileInfoDTO.builder().id(fileInfo.getId())
                    .fileName(fileInfo.getFileName())
                    .localDateTime(fileInfo.getLocalDateTime()).fileExistence(Constants.FALSE)
                    .build();
        }
    }
}
