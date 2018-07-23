package com.example.collabeditor.controller;

import com.example.collabeditor.model.FileObject;
import com.example.collabeditor.service.FileService;
import com.example.collabeditor.service.TemService;
import com.example.collabeditor.service.OTService;
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
    private OTService otService;

    @Autowired
    private FileService fileService;

    @Autowired
    private TemService temService;

    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public String uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        fileService.save(fileService.createFileObject(file));
        return file.getOriginalFilename();
    }

    @RequestMapping(value = "/newFile", method = RequestMethod.POST)
    public String newFile(@RequestParam("filename") String filename) {
        fileService.save(new FileObject(filename));
        return filename;
    }

    @RequestMapping(value = "/listFiles", method = RequestMethod.GET)
    public String[] listFiles() {
        return fileService.getFileNames();
    }

    @RequestMapping(value = "/chooseFile", method = RequestMethod.POST)
    public String[] chooseFile(@RequestParam("filename") String filename) {
        return new String[]{otService.applyAllRevision(filename, fileService.findByFilename(filename).getFile())
                , "" + temService.findMaxRevisionByFilename(filename)};
    }
}