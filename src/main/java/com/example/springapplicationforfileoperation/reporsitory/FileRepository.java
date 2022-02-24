package com.example.springapplicationforfileoperation.reporsitory;

import com.example.springapplicationforfileoperation.model.FileInfo;
import org.springframework.data.mongodb.repository.MongoRepository;

import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface FileRepository extends MongoRepository<FileInfo, String> {

    List<FileInfo> findByUserName(String userName);
}
