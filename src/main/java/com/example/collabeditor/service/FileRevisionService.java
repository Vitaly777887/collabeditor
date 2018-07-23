package com.example.collabeditor.service;

import com.example.collabeditor.model.FileObject;
import com.example.collabeditor.model.FileRevision;
import com.example.collabeditor.repository.FileRepository;
import com.example.collabeditor.repository.FileRevisionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileRevisionService {

    @Autowired
    FileRevisionRepository fileRevisionRepository;

    @Autowired
    private OTService otService;

    @Autowired
    private TemService temService;

    @Autowired
    private FileService fileService;

    public void save(FileRevision fileRevision) {
        fileRevisionRepository.save(fileRevision);
    }

    public List<FileRevision> findByFilename(String filename) {
        return fileRevisionRepository.findByFilename(filename);
    }

    public FileRevision findByName(String name) {
        return fileRevisionRepository.findByName(name);
    }

    public String[] getFileRevisionNames(String filename) {
        return findByFilename(filename).stream()
                .map(s -> s.getName())
                .toArray(String[]::new);
    }

    public String getFileForRevision(String filename, int revision) {
        FileObject byFilename = fileService.findByFilename(filename);
        String file = byFilename.getFile();
        return otService.apply(file, temService.findByFilenameAndRevisionIsLessThanOrderByRevisionAsc(filename, revision + 1));
    }
}