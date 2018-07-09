package com.example.collabeditor.controller;

import com.example.collabeditor.service.TextEditorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;


@RestController
public class FileController {

    String directory = "collabeditor\\loadFiles";

    @Autowired
    TextEditorService service;

    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public String uploadFile(@RequestParam("file") MultipartFile file) {

        String name = null;

        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                bytes = new String(bytes, "Windows-1251").getBytes("UTF-8");


                name = file.getOriginalFilename();

                File dir = new File(directory);

                if (!dir.exists()) {
                    dir.mkdirs();
                }

                File uploadedFile = new File(dir.getAbsolutePath() + File.separator + name);

                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(uploadedFile));
                stream.write(bytes);
                stream.flush();
                stream.close();

                return "You successfully uploaded file=" + name;

            } catch (Exception e) {
                return "You failed to upload " + name + " => " + e.getMessage();
            }
        } else {
            return "You failed to upload " + name + " because the file was empty.";
        }
    }

    @RequestMapping(value = "/newFile", method = RequestMethod.POST)
    public String newFile(@RequestParam("filename") String filename) {
        File dir = new File(directory);

        if (!dir.exists()) {
            dir.mkdirs();
        }
        try {
            new File(dir.getAbsolutePath() + File.separator + filename + ".txt").createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return filename;
    }

    @RequestMapping(value = "/listFiles", method = RequestMethod.GET)
    public String[] listFiles() {
        String[] list = new File(directory).list();
        return Arrays.stream(list).map(s -> s.replace(".txt", "")).toArray(String[]::new);
    }

    @RequestMapping(value = "/chooseFile", method = RequestMethod.POST)
    public String[] chooseFile(@RequestParam("filename") String filename) throws IOException {

        String[] res = {TextEditorService.apply(filename, new String(Files.readAllBytes(Paths.get(directory + File.separator + filename + ".txt"))))
                , "" + service.getRevision(filename)};
        return res;

    }

    @RequestMapping(value = "/saveFile", method = RequestMethod.POST)
    public String saveFile(@RequestParam("filename") String filename) throws IOException {
        Path path = Paths.get(directory + File.separator + filename + ".txt");
        String resFile = service.saveFile(filename, new String(Files.readAllBytes(path)));
        if (!resFile.equals("")) {
            Files.write(path, resFile.getBytes());
        }
        return filename;
    }
}
