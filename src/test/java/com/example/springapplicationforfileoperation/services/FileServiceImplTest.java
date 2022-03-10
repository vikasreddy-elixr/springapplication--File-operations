package com.example.springapplicationforfileoperation.services;

import com.example.springapplicationforfileoperation.contants.Constants;
import com.example.springapplicationforfileoperation.exceptionhandler.NotFoundException;
import com.example.springapplicationforfileoperation.model.FileInfo;
import com.example.springapplicationforfileoperation.reporsitory.FileRepository;
import com.example.springapplicationforfileoperation.responses.Response;
import com.example.springapplicationforfileoperation.responses.ResponseForGetById;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@WebMvcTest(FileServiceImpl.class)
@RunWith(MockitoJUnitRunner.class)
class FileServiceImplTest {

    @Autowired
    FileServiceImpl fileService;
    @MockBean
    FileRepository fileRepository;

    private final FileInfo fileInfo = new FileInfo();
    private final List<FileInfo> fileInfoList = new ArrayList<>();


    @BeforeEach
    public void setUp() {
        fileInfo.setId(UUID.fromString("df38ab86-c3db-434d-a346-2471be5990d"));
        fileInfo.setUserName("userName");
        fileInfo.setFileName("project.txt");
        fileInfo.setLocalDateTime(LocalDateTime.now());
        fileInfoList.add(fileInfo);
        MockitoAnnotations.openMocks(this);
        MockMvcBuilders.standaloneSetup(fileService).build();
    }

    @Test
    void test_SaveFile() {

        String fileName = "project.txt";
        MockMultipartFile sampleFile = new MockMultipartFile(
                "file",
                fileName,
                "text/plain",
                "This is the file content".getBytes());
        when(fileRepository.save(any(FileInfo.class))).thenReturn(fileInfo);
        ResponseEntity<Response> savedData = fileService.fileUpload(sampleFile, "userName");
        assertThat(savedData).isNotNull();
        assertThat(Objects.requireNonNull(savedData.getBody()).getId().getClass().getName()).isSameAs(fileInfo.getId().getClass().getName());
    }

    @Test
    public void getFileById() {
        String id = "0663b08d-386f-41ce-8c86-b9935a9c9029";
        when(fileRepository.findById(UUID.fromString(id))).thenReturn(Optional.of(fileInfo));
        ResponseEntity<ResponseForGetById> fileFound = fileService.getFileById(id);
        assertNotNull(fileFound);
        assertEquals(HttpStatus.OK, fileFound.getStatusCode());
        assertEquals(Constants.SUCCESS, Objects.requireNonNull(fileFound.getBody()).getStatus());
        assertEquals(fileInfo.getUserName(), fileFound.getBody().getData().getUserName());
        assertEquals(fileInfo.getFileName(), fileFound.getBody().getData().getFileName());
    }

    @Test
    public void errorGet_FileById() {
        String id = "e87b8fc3-91b3-4a37-8d41-f7bf7a0b4730";
        when(fileRepository.findById(UUID.fromString(id))).thenThrow(new NotFoundException(Constants.ERROR_NOT_FOUND));
        try {
            fileService.getFileById(id);
            fail();
        } catch (NotFoundException exception) {
            assertTrue(true);
        }
    }

    @Test
    public void getFileByUsername() {
        String userName = "userName";
        when(fileRepository.findByUserName(userName)).thenReturn(fileInfoList);
        ResponseEntity<Response> fileFound = fileService.getFilesByUserName(userName);
        assertNotNull(fileFound);
        assertEquals(HttpStatus.OK, fileFound.getStatusCode());
        assertEquals(Constants.SUCCESS, Objects.requireNonNull(fileFound.getBody()).getStatus());
        assertEquals(fileInfo.getUserName(), fileFound.getBody().getUserName());
        assertEquals(fileInfoList.get(0).getFileName(), fileFound.getBody().getFiles().get(0).getFileName());
    }

    @Test
    public void errorGet_FileByUserName() {

        String userName = "userName";
        when(fileRepository.findByUserName(userName)).thenThrow(new NotFoundException(Constants.ERROR_NOT_FOUND));
        try {
            fileService.getFilesByUserName(userName);
            fail();
        } catch (NotFoundException exception) {
            assertTrue(true);
        }
    }
}
