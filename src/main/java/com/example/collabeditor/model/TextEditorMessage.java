package com.example.collabeditor.model;

import javax.persistence.*;

@Entity
@Table(name = "tems", uniqueConstraints = {@UniqueConstraint(columnNames = {"revision", "file_object_id"}, name = "tem_unique_revision_file_object_id_idx")})
public class TextEditorMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "revision", nullable = false)
    private Integer revision;

    @Enumerated(EnumType.STRING)
    @Column(name = "message_type", nullable = false)
    private MessageType type;

    @Column(name = "data", nullable = false)
    private String data;

    @Column(name = "fromq", nullable = false)
    public Integer from;

    @Column(name = "to", nullable = false)
    public Integer to;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "file_object_id", nullable = false)
    // @OnDelete(action = OnDeleteAction.CASCADE)
    private FileObject fileObject;

    public enum MessageType {
        DELETE,
        INSERT
    }

    public FileObject getFileObject() {
        return fileObject;
    }

    public void setFileObject(FileObject fileObject) {
        this.fileObject = fileObject;
    }

    public String getFilename() {
        return fileObject.getFilename();
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
