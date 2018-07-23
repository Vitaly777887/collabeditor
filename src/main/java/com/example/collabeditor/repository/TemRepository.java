package com.example.collabeditor.repository;

import com.example.collabeditor.model.TextEditorMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TemRepository extends JpaRepository<TextEditorMessage, Integer> {
    List<TextEditorMessage> findByFilename(String filename);

    List<TextEditorMessage> findByFilenameAndRevisionIsLessThanOrderByRevision(String filename, int revision);
}
