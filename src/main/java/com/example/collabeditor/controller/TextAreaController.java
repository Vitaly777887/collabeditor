package com.example.collabeditor.controller;

import com.example.collabeditor.model.TextEditorMessage;
import com.example.collabeditor.service.TextEditorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class TextAreaController {

    @Autowired
    private TextEditorService service;

    @MessageMapping("/textArea.sendChange")
    @SendTo("/topic/public")
    public TextEditorMessage sendChanges(@Payload TextEditorMessage tem) {
        TextEditorMessage[] newTEM = service.getNewTEM(tem.getFilename(), tem.getRevision());

        for (TextEditorMessage tem2 : newTEM) {
            service.inc(tem2, tem);
        }
        tem.setRevision(tem.getRevision() + 1 + newTEM.length);
        service.addMessage(tem.getFilename(), tem);
        return tem;
    }
}
