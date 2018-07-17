package com.example.collabeditor.repository;

import com.example.collabeditor.model.TextEditorMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TemRepository extends JpaRepository<TextEditorMessage, Integer> {
    List<TextEditorMessage> findByFilename(String filename);
}
