package com.example.collabeditor.service;

import com.example.collabeditor.model.FileObject;
import com.example.collabeditor.model.TextEditorMessage;
import com.example.collabeditor.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

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

    public List<FileObject> findAll() {
        return fileRepository.findAll();
    }

    public FileObject createFileObject(MultipartFile file) throws IOException {
        return new FileObject(file.getOriginalFilename(), new String(file.getBytes(), "Windows-1251"));
    }

    public String[] getFileNames() {
        return findAll().stream()
                .map(s -> s.getFilename().replace(".txt", ""))
                .toArray(String[]::new);
    }

    public List<TextEditorMessage> getTems(String filename) {
        return findByFilename(filename).getTems();
    }
}
