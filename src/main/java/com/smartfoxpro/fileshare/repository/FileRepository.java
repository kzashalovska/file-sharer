package com.smartfoxpro.fileshare.repository;

import com.smartfoxpro.fileshare.entity.FileEntity;
import com.smartfoxpro.fileshare.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<FileEntity, Long> {
    FileEntity findFileEntityById(long id);

    List<FileEntity> findFileEntitiesByUser(User user);
}
