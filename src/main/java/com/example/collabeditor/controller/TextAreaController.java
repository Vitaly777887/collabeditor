package com.example.collabeditor.controller;

import com.example.collabeditor.model.TextEditorMessage;
import com.example.collabeditor.service.TemService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.io.IOException;

@Controller
public class TextAreaController {

    @Autowired
    private TemService temService;

    @MessageMapping("/textArea.sendChange")
    @SendTo("/topic/public")
    public synchronized TextEditorMessage sendChanges(@Payload String filename, @Payload TextEditorMessage tem) throws IOException {
        //Fix filename
        ObjectMapper mapper = new ObjectMapper();
        StringF s = mapper.readValue(filename, StringF.class);
        return temService.applyNewRevisionsAndSave(tem, s.getFilename());
    }

    class StringF {
        public String filename;

        public String getFilename() {
            return filename;
        }

        public void setFilename(String filename) {
            this.filename = filename;
        }
    }
}