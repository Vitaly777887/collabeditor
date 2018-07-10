package com.example.collabeditor.repository;

import com.example.collabeditor.model.FileObject;
import org.springframework.data.repository.CrudRepository;

public interface FileRepository extends CrudRepository<FileObject, Integer> {
    FileObject findByFilename(String filename);
}
