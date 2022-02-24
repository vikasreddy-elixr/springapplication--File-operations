package com.example.springapplicationforfileoperation.services;

import com.example.springapplicationforfileoperation.contants.Constants;
import com.example.springapplicationforfileoperation.exceptionhandler.NotFoundException;
import com.example.springapplicationforfileoperation.model.FileInfo;
import com.example.springapplicationforfileoperation.model.FileInfoDTO;
import com.example.springapplicationforfileoperation.reporsitory.FileRepository;
import com.example.springapplicationforfileoperation.responses.ErrorResponse;
import com.example.springapplicationforfileoperation.responses.SuccessResponse;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.UnexpectedTypeException;
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
    FileRepository fileRepository;

    public FileServiceImpl(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    final String filePath;

    {
        try {
            filePath = new ClassPathResource("/static/ ").getFile().getAbsolutePath();
        } catch (IOException e) {
            throw new UnexpectedTypeException();
        }
    }

    @Override
    public ResponseEntity<?> fileUpload(MultipartFile multipartFile, String userName) {

        FileInfo fileInfo;
        try {
            fileInfo = new FileInfo();
            fileInfo.setFileName(multipartFile.getOriginalFilename());
            fileInfo.setUserName(userName);
            fileRepository.save(fileInfo);
            Files.copy(multipartFile.getInputStream(), Paths.get(filePath + fileInfo.getId()), StandardCopyOption.REPLACE_EXISTING);

        } catch (IOException e) {
            return new ResponseEntity<>(new ErrorResponse(Constants.FAILURE, Constants.ERROR_UNEXPECTED_TYPE), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(SuccessResponse.builder().status(Constants.SUCCESS).id(fileInfo.getId()).build(), HttpStatus.OK);

    }

    @Override
    public ResponseEntity<?> getFileById(UUID uuid ) {
        FileInfo getFile = fileRepository.findById(String.valueOf(uuid)).orElseThrow(() -> new NotFoundException(Constants.ERROR_NOT_FOUND));
        String fileName = getFile.getFileName();
        File file = new File(filePath + uuid);

        if (file.exists()) {
            try {
                String data = new String(Files.readAllBytes(Paths.get(filePath + uuid)));
                return new ResponseEntity<>(new SuccessResponse(Constants.SUCCESS, SuccessResponse.builder().userName(getFile.getUserName()).uploadTime(getFile.getLocalDateTime()).fileName(fileName).content(data).build()), HttpStatus.OK);
            } catch (IOException e) {
                return new ResponseEntity<>(new ErrorResponse(Constants.FAILURE, e.getMessage()), HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<>(new ErrorResponse(Constants.FAILURE, Constants.ERROR_NOT_FOUND), HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<?> getFilesByUserName(String userName) {
        List<FileInfo> fileInfoList = fileRepository.findByUserName(userName);
        if (fileInfoList.isEmpty()) {
            return new ResponseEntity<>(new ErrorResponse(Constants.FAILURE, Constants.ERROR_NO_DATA_FOUND), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(SuccessResponse.builder().status(Constants.SUCCESS).userName(userName).files((fileInfoList.stream().map(this::convertDataIntoDTO).collect(Collectors.toList()))).build(), HttpStatus.OK);
    }

    private FileInfoDTO convertDataIntoDTO(FileInfo fileInfo) {
        FileInfoDTO requiredFileDetailsDto = new FileInfoDTO();
        requiredFileDetailsDto.setId(fileInfo.getId());
        requiredFileDetailsDto.setFileName(fileInfo.getFileName());
        requiredFileDetailsDto.setLocalDateTime(fileInfo.getLocalDateTime());
        return requiredFileDetailsDto;
    }

}
