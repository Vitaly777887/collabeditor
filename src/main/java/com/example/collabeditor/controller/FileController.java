package com.example.collabeditor.controller;

import com.example.collabeditor.model.FileObject;
import com.example.collabeditor.service.FileService;
import com.example.collabeditor.service.TemService;
import com.example.collabeditor.service.TextEditorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
public class FileController {

    @Autowired
    private TextEditorService service;

    @Autowired
    private FileService fileService;

    @Autowired
    private TemService temService;

    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public String uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        fileService.save(new FileObject(file.getOriginalFilename(), new String(file.getBytes(), "Windows-1251")));
        return file.getOriginalFilename();
    }

    @RequestMapping(value = "/newFile", method = RequestMethod.POST)
    public String newFile(@RequestParam("filename") String filename) {
        fileService.save(new FileObject(filename));
        return filename;
    }

    @RequestMapping(value = "/listFiles", method = RequestMethod.GET)
    public String[] listFiles() {
        return fileService.findAll().stream()
                .map(s -> s.getFilename().replace(".txt", ""))
                .toArray(String[]::new);
    }

    @RequestMapping(value = "/chooseFile", method = RequestMethod.POST)
    public String[] chooseFile(@RequestParam("filename") String filename) throws IOException {
        String[] res = {service.apply(filename, fileService.findByFilename(filename).getFile())
                , "" + temService.findMaxRevisionByFilename(filename)};
        return res;
    }
}