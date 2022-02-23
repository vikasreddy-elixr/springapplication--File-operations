package com.example.springapplicationforfileoperation.services;

import com.example.springapplicationforfileoperation.contants.Constants;
import com.example.springapplicationforfileoperation.exceptionhandler.NotFoundException;
import com.example.springapplicationforfileoperation.model.FileInfo;
import com.example.springapplicationforfileoperation.model.FileInfoDTO;
import com.example.springapplicationforfileoperation.reporsitory.GlobalRepository;
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
import java.util.stream.Collectors;

@Service
public class FileServiceImpl implements FileService {
    GlobalRepository globalRepository;

    public FileServiceImpl(GlobalRepository globalRepository) {
        this.globalRepository = globalRepository;
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
    public ResponseEntity<?> fileUpload(MultipartFile multipartFile, FileInfo fileInfo, String userName) {

        try {
            fileInfo.setFileName(multipartFile.getOriginalFilename());
            fileInfo.setUserName(userName);
            globalRepository.save(fileInfo);
            Files.copy(multipartFile.getInputStream(), Paths.get(filePath + multipartFile.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);

        } catch (IOException e) {
            return new ResponseEntity<>(new ErrorResponse(Constants.FAILURE, Constants.ERROR_UNEXPECTED_TYPE), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(new SuccessResponse(Constants.SUCCESS, fileInfo.getId()), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getFileById(String id) {
        if ((!id.matches("[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}"))){
            return new ResponseEntity<>(new ErrorResponse(Constants.FAILURE,Constants.ERROR_UUID_FORMAT),HttpStatus.BAD_REQUEST);
        }
            FileInfo getFile = globalRepository.findById(id).orElseThrow(() -> new NotFoundException(Constants.ERROR_NOT_FOUND));
        String fileName = getFile.getFileName();
        File file = new File(filePath + fileName);

        if (file.exists()) {
            try {
                String data = new String(Files.readAllBytes(Paths.get(filePath + fileName)));
                return new ResponseEntity<>(new SuccessResponse(Constants.SUCCESS, getFile.getUserName(), getFile.getLocalDateTime(), fileName, data), HttpStatus.OK);
            } catch (IOException e) {
                return new ResponseEntity<>(new ErrorResponse(Constants.FAILURE, e.getMessage()), HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>(new ErrorResponse(Constants.FAILURE, Constants.ERROR_NOT_FOUND), HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<?> getFilesByUserName(String userName) {
        if(globalRepository.findByUserName(userName).isEmpty()) {
            return new ResponseEntity<>(new ErrorResponse(Constants.FAILURE,Constants.ERROR_NO_DATA_FOUND),HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(new SuccessResponse(Constants.SUCCESS,userName,globalRepository
                .findByUserName(userName)
                .stream()
                .map(this::convertDataIntoDTO)
                .collect(Collectors.toList())),HttpStatus.OK);

    }

    private FileInfoDTO convertDataIntoDTO(FileInfo fileInfo) {
        FileInfoDTO requiredFileDetailsDto = new FileInfoDTO();
        requiredFileDetailsDto.setId(fileInfo.getId());
        requiredFileDetailsDto.setFileName(fileInfo.getFileName());
        requiredFileDetailsDto.setLocalDateTime(fileInfo.getLocalDateTime());
        return requiredFileDetailsDto;
    }

}
