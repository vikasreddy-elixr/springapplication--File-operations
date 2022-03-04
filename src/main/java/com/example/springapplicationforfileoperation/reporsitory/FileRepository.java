package com.example.springapplicationforfileoperation.reporsitory;

import com.example.springapplicationforfileoperation.model.FileInfo;
import org.springframework.data.mongodb.repository.MongoRepository;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


@Repository
public interface FileRepository extends MongoRepository<FileInfo, UUID> {

    List<FileInfo> findByUserName(String userName);
}
