package com.example.collabeditor.model;


public class TextEditorMessage {
    public String type;
    public String data;
    public Integer from;
    public Integer to;
    public Integer revision;


    public enum MessageType {
        DELETE,
        INSERT,
        CHECK
    }

    public TextEditorMessage() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
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

    public TextEditorMessage(String type, String data, Integer from, Integer to, Integer revision) {

        this.type = type;
        this.data = data;
        this.from = from;
        this.to = to;
        this.revision = revision;
    }
}
