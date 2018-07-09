package com.example.collabeditor.service;

import com.example.collabeditor.model.TextEditorMessage;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class TextEditorService {
    private static Map<String, LinkedList<TextEditorMessage>> editorMessageHashMap;
    private static Map<String, AtomicInteger> revisions;
    private static Map<String, AtomicInteger> fileStorageRevisions;

    private static Map<String, String> users;

    static {
        editorMessageHashMap = new ConcurrentHashMap<>();
        revisions = new ConcurrentHashMap<>();
        fileStorageRevisions = new ConcurrentHashMap<>();
        users = new ConcurrentHashMap<>();
    }

    public void put(String user, String filename) {
        users.put(user, filename);
    }

    public String get(String user) {
        return users.get(user);
    }

    synchronized static public boolean addMessage(String filename, TextEditorMessage tem) {
        if (editorMessageHashMap.get(filename) == null) {
            editorMessageHashMap.put(filename, new LinkedList<>());
            revisions.put(filename, new AtomicInteger(0));
            fileStorageRevisions.put(filename, new AtomicInteger(0));
        }
        revisions.get(filename).incrementAndGet();
        return editorMessageHashMap.get(filename).add(tem);

    }

    public TextEditorMessage inc(TextEditorMessage serverChange, TextEditorMessage localChange) {

        if ("INSERT".equals(localChange.type)) {
            if (serverChange.getTo() >= localChange.getTo()) {
                int delta = serverChange.getData().length();
                localChange.setFrom(localChange.getFrom() + delta);
                localChange.setTo(localChange.getTo() + delta);
            }

        } else if ("DELETE".equals(localChange.type)) {
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

        if (revisions.containsKey(filename)) {
            int revidionLast = getRevision(filename);
            LinkedList<TextEditorMessage> textEditorMessages = new LinkedList<>();
            for (int i = revision + 1; i <= revidionLast; i++) {
                textEditorMessages.add(editorMessageHashMap.get(filename).get(i - 1));
            }
            return textEditorMessages.toArray(new TextEditorMessage[0]);
        }
        return new TextEditorMessage[0];
    }

    public static String apply(String filename, String file) {
        if (editorMessageHashMap.containsKey(filename)) {
            for (TextEditorMessage tem : editorMessageHashMap.get(filename)) {
                file = apply(file, tem);
            }
        }
        return file;

    }

    private static String apply(String file, TextEditorMessage tem) {
        if ("INSERT".equals(tem.type)) {
            return file.substring(0, tem.from) + tem.data
                    + ((file.length() > 2) ? file.substring(tem.from) : "");
        } else if ("DELETE".equals(tem.type)) {
            return file.substring(0, tem.to)
                    + ((file.length() > 2) ? file.substring(tem.from) : "");
        }
        return file;

    }

    public static int getRevision(String filename) {
        if (revisions.containsKey(filename)) {
            return revisions.get(filename).get() - fileStorageRevisions.get(filename).get();
        }
        return 0;
    }

    public String saveFile(String filename, String file) {
        if (!fileStorageRevisions.containsKey(filename)) {
            fileStorageRevisions.put(filename, new AtomicInteger());
        }
        int countSaveRevisions = getRevision(filename);
        fileStorageRevisions.get(filename).addAndGet(countSaveRevisions);
        LinkedList<TextEditorMessage> getSaveTem = new LinkedList<>();
        for (int i = 0; i < countSaveRevisions; i++) {

            getSaveTem.addLast(editorMessageHashMap.get(filename).removeFirst());
        }
        for (TextEditorMessage tem : getSaveTem) {
            file = apply(file, tem);
        }

        return file;
    }
}
