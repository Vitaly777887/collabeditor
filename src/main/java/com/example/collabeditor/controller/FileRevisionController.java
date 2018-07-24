package com.example.collabeditor.controller;

import com.example.collabeditor.model.FileRevision;
import com.example.collabeditor.service.FileRevisionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FileRevisionController {

    @Autowired
    private FileRevisionService fileRevisionService;

    @RequestMapping("/saveFileRevision")
    public String saveFileRevision(@RequestParam("filename") String filename, @RequestParam("revision") int revision,
                                   @RequestParam("name") String name) {
        fileRevisionService.save(new FileRevision(name, revision, filename));
        return name;
    }

    @RequestMapping("/listFileRevisions")
    public String[] listFileRevisions(@RequestParam("filename") String filename) {
        return fileRevisionService.getFileRevisionNames(filename);
    }

    @RequestMapping("/chooseFileRevisions")
    public String chooseFileRevisions(@RequestParam("name") String name) {
        FileRevision fileRevision = fileRevisionService.findByName(name);
        return fileRevisionService.getFileForRevision(fileRevision.getFilename(), fileRevision.getRevision());
    }
}
