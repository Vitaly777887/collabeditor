package com.example.collabeditor.model;

import javax.persistence.*;

@Entity
public class TextEditorMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;

    public Integer revision;

    public MessageType type;

    public String filename;

    public String data;

    @Column(name = "FROMQ")
    public Integer from;

    public Integer to;

    public enum MessageType {
        DELETE,
        INSERT,
        CHECK
    }

    public String getFilename() {
        return getFilename2();
    }

    public String getFilename2() {
        return filename;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Integer getFrom() {
        return from;
    }

    public void setFrom(Integer from) {
        this.from = from;
    }

    public Integer getTo() {
        return to;
    }

    public void setTo(Integer to) {
        this.to = to;
    }

    public Integer getRevision() {
        return revision;
    }

    public void setRevision(Integer revision) {
        this.revision = revision;
    }

    public TextEditorMessage() {
    }

    public TextEditorMessage(MessageType type, String data, Integer from, Integer to, Integer revision) {

        this.type = type;
        this.data = data;
        this.from = from;
        this.to = to;
        this.revision = revision;
    }
}
