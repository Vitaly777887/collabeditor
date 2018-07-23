package com.example.collabeditor.service;

import com.example.collabeditor.model.TextEditorMessage;
import com.example.collabeditor.repository.FileRepository;
import com.example.collabeditor.repository.TemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class TemService {

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private TemRepository temRepository;
    @Autowired
    private FileService fileService;

    @Autowired
    private OTService otService;

    public List<TextEditorMessage> findByFilenameOrderByRevision(String filename) {
        List<TextEditorMessage> tems = fileService.getTems(filename);
        tems.sort(Comparator.comparing(TextEditorMessage::getRevision));
        return tems;
    }

    public int findMaxRevisionByFilename(String filename) {
        return findByFilenameOrderByRevision(filename).size();
    }

    public TextEditorMessage applyNewRevisionsAndSave(TextEditorMessage tem, String filename) {
        TextEditorMessage[] newTEM = otService.getNewTEM(tem.getFilename(), tem.getRevision());

        for (TextEditorMessage tem2 : newTEM) {
            otService.inc(tem2, tem);
        }
        tem.setRevision(tem.getRevision() + 1 + newTEM.length);
        tem.setFileObject(fileRepository.findByFilename(tem.getFilename()));
        return temRepository.save(tem);
    }
}
