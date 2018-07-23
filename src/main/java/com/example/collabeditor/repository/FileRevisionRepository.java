package com.example.collabeditor.repository;

import com.example.collabeditor.model.FileRevision;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRevisionRepository extends JpaRepository<FileRevision, Integer> {
    List<FileRevision> findByFilename(String filename);

    FileRevision findByName(String name);
}