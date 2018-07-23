package com.example.collabeditor.repository;

import com.example.collabeditor.model.FileObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<FileObject, Integer> {
    FileObject findByFilename(String filename);
}
