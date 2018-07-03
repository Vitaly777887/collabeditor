package com.example.collabeditor.controller;

import com.example.collabeditor.model.TextEditorMessage;
import com.example.collabeditor.service.TextEditorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OtController {
    @Autowired
    private TextEditorService service;

    @RequestMapping(value = "/ot", method = RequestMethod.POST)
    public TextEditorMessage[] ot(@RequestParam("type") String type, @RequestParam("data") String data
            , @RequestParam("from") Integer from, @RequestParam("to") Integer to
            , @RequestParam("filename") String filename, @RequestParam("revision") Integer revision) {

        TextEditorMessage[] newTEM = service.getNewTEM(filename, revision);
        if ("CHECK".equals(type)) {
            return newTEM;
        }
        TextEditorMessage tem = new TextEditorMessage(type, data, from, to, revision);
        service.addMessage(filename, tem);
        for (TextEditorMessage tem2 : newTEM) {
            service.inc(tem2, tem);
        }
        return newTEM;
    }
}
