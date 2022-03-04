package com.example.springapplicationforfileoperation.services;

import com.example.springapplicationforfileoperation.contants.Constants;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
        fileInfo.setId(UUID.fromString("d845ace0-c19f-4fad-98ac-1b06c8ba3c3e"));
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
        String id = "493388f5-8c68-4984-beac-962dd4ba35f6";
        when(fileRepository.findById(UUID.fromString(id))).thenReturn(Optional.of(fileInfo));
        ResponseEntity<ResponseForGetById> fileFound = fileService.getFileById(id);
        assertNotNull(fileFound);
        assertEquals(HttpStatus.OK, fileFound.getStatusCode());
        assertEquals(Constants.SUCCESS, Objects.requireNonNull(fileFound.getBody()).getStatus());
        assertEquals(fileInfo.getUserName(), fileFound.getBody().getData().getUserName());
        assertEquals(fileInfo.getFileName(), fileFound.getBody().getData().getFileName());
    }

    @Test
    public void getFileByUsername() {
        String userName = "userName";
        when(fileRepository.findByUserName(userName)).thenReturn(fileInfoList);
        ResponseEntity<Response> fIleFound = fileService.getFilesByUserName(userName);
        assertNotNull(fIleFound);
        assertEquals(HttpStatus.OK, fIleFound.getStatusCode());
        assertEquals(Constants.SUCCESS, Objects.requireNonNull(fIleFound.getBody()).getStatus());
        assertEquals(fileInfo.getUserName(), fIleFound.getBody().getUserName());
        assertEquals(fileInfoList.get(0).getFileName(), fIleFound.getBody().getFiles().get(0).getFileName());
    }
}
