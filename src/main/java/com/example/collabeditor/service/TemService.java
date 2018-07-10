package com.example.collabeditor.service;

import com.example.collabeditor.model.TextEditorMessage;
import com.example.collabeditor.repository.FileRepository;
import com.example.collabeditor.repository.TemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class TemService {

    @Autowired
    private TemRepository temRepository;

    @Autowired
    private FileRepository fileRepository;

    public boolean save(TextEditorMessage tem) {
        return temRepository.save(tem) == tem;
    }

    public List<TextEditorMessage> findByFilenameOrderByRevision(String filename) {
        List<TextEditorMessage> tems = temRepository.findByFilename(filename);
        tems.sort(Comparator.comparing(TextEditorMessage::getRevision));
        return tems;
    }

    public int findMaxRevisionByFilename(String filename) {
        Optional<TextEditorMessage> max = findByFilenameOrderByRevision(filename).stream().max(Comparator.comparing(TextEditorMessage::getRevision));
        return max.isPresent() ? max.get().getRevision() : 0;
    }
}
