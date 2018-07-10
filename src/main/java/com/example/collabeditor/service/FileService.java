package com.example.collabeditor.service;

import com.example.collabeditor.model.FileObject;
import com.example.collabeditor.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class FileService {
    @Autowired
    private FileRepository fileRepository;

    public void save(FileObject fileObject) {
        fileRepository.save(fileObject);
    }

    public FileObject findByFilename(String filename) {
        return fileRepository.findByFilename(filename);
    }

    public List<FileObject> findAll(){
      return  StreamSupport.stream(fileRepository.findAll().spliterator(), false).collect(Collectors.toList());
    }
}
