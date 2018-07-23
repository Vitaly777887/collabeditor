package com.example.collabeditor.repository;

import com.example.collabeditor.model.TextEditorMessage;
import org.springframework.data.repository.CrudRepository;


public interface TemRepository extends CrudRepository<TextEditorMessage, Integer> {
}
