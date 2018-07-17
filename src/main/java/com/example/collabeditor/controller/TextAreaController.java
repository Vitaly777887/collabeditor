package com.example.collabeditor.controller;

import com.example.collabeditor.model.TextEditorMessage;
import com.example.collabeditor.service.TemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class TextAreaController {

    @Autowired
    private TemService temService;

    @MessageMapping("/textArea.sendChange")
    @SendTo("/topic/public")
    public synchronized TextEditorMessage sendChanges(@Payload TextEditorMessage tem) {
        return temService.applyNewRevisionsAndSave(tem);
    }
}