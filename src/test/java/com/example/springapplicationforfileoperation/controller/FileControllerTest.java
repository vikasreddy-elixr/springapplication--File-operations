package com.example.springapplicationforfileoperation.controller;

import com.example.springapplicationforfileoperation.contants.Constants;
import com.example.springapplicationforfileoperation.exceptionhandler.NotFoundException;
import com.example.springapplicationforfileoperation.model.FileInfo;
import com.example.springapplicationforfileoperation.responses.Response;
import com.example.springapplicationforfileoperation.services.FileService;
import net.minidev.json.JSONArray;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FileController.class)
@RunWith(MockitoJUnitRunner.class)
class FileControllerTest {

    @Autowired
    FileController fileController;
    @MockBean
    FileService fileService;

    private final FileInfo fileInfo = new FileInfo();

    String id = "497618e4-b378-45a5-b8dc-dfef81c9ac4e";

    @Autowired
    private MockMvc mockMvc;


    @Before
    public void init() {
        fileInfo.setId(UUID.fromString(id));
        fileInfo.setUserName("userName");
        fileInfo.setFileName("empty.txt");
        fileInfo.setLocalDateTime(LocalDateTime.now());
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void test_UploadFile() throws Exception {

        String fileName = "fileName.txt";
        MockMultipartFile sampleFile = new MockMultipartFile("file", fileName, "text/plain",
                "This is the file content".getBytes());
        mockMvc.perform(MockMvcRequestBuilders.multipart("/upload")
                        .file(sampleFile)
                        .characterEncoding("UTF-8")
                        .contentType(MediaType.TEXT_PLAIN)
                        .param("username", "userName"))
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    public void test_UploadFile_NoFileProvided() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.multipart("/upload"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void test_GetFileById() throws Exception {

        Mockito.when(fileService.getFileById(id))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

         mockMvc.perform(MockMvcRequestBuilders.get("/file/id")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void negativeTest_GetFileById() {

        Mockito.when(fileController.getFileById(id))
                .thenReturn(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        ResponseEntity<?> result = fileController.getFileById(id);
        assertEquals(HttpStatus.NOT_FOUND,result.getStatusCode());
    }


    @Test
    public void test_GetFileByUsername() throws Exception {
        String userName = "userName";
        when(fileService.getFilesByUserName(userName)).thenReturn(new ResponseEntity<>(HttpStatus.OK));
        JSONArray json = new JSONArray();
         mockMvc.perform(MockMvcRequestBuilders.get("/file/user/userName")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json.toString()))
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    public void negativeTest_GetFileByUsername()  {
        String userName = "userName";
        when(fileController.getFilesByUserName(userName))
                .thenReturn(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        ResponseEntity<?> result = fileService.getFilesByUserName(userName);
        assertEquals(HttpStatus.NOT_FOUND,result.getStatusCode());
    }
}
