package com.example.collabeditor.service;

import com.example.collabeditor.model.TextEditorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.StreamSupport;

@Service
public class TextEditorService {

    @Autowired
    private TemService temService;

    public TextEditorMessage inc(TextEditorMessage serverChange, TextEditorMessage localChange) {

        if (localChange.type == TextEditorMessage.MessageType.INSERT) {
            if (serverChange.getTo() >= localChange.getTo()) {
                int delta = serverChange.getData().length();
                localChange.setFrom(localChange.getFrom() + delta);
                localChange.setTo(localChange.getTo() + delta);
            }

        } else if (localChange.type == TextEditorMessage.MessageType.DELETE) {
            int delta = serverChange.getData() == null ? 1 : serverChange.getData().length();
            if (serverChange.getTo() >= localChange.getTo()) {
                localChange.setFrom(localChange.getFrom() - delta);
                localChange.setTo(localChange.getTo() - delta);
            }
        }
        serverChange.setRevision(serverChange.getRevision() + 1);
        return serverChange;
    }

    public TextEditorMessage[] getNewTEM(String filename, Integer revision) {
        return StreamSupport.stream(temService.findByFilenameOrderByRevision(filename).spliterator(), false)
                .filter(tem -> tem.getRevision() > revision).toArray(TextEditorMessage[]::new);
    }

    public String apply(String filename, String file) {
        for (TextEditorMessage tem : temService.findByFilenameOrderByRevision(filename)) {
            file = apply(file, tem);
        }
        return file;

    }

    public String apply(String file, TextEditorMessage tem) {
        if (tem.type == TextEditorMessage.MessageType.INSERT) {
            return file.substring(0, tem.from) + tem.data
                    + ((file.length() > 2) ? file.substring(tem.from) : "");
        } else if (tem.type == TextEditorMessage.MessageType.DELETE) {
            return file.substring(0, tem.to)
                    + ((file.length() > 2) ? file.substring(tem.from) : "");
        }
        return file;
    }
}
