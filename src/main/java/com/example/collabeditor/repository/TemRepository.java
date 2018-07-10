package com.example.collabeditor.repository;

import com.example.collabeditor.model.TextEditorMessage;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TemRepository extends CrudRepository<TextEditorMessage, Integer> {
    List<TextEditorMessage> findByFilename(String filename);
}
