package com.example.collabeditor.model;

import javax.persistence.*;

@Entity
public class FileObject {

    @Id
    @Column(name = "fileId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String filename;

    @Lob()
    private String file;

    public Integer getId() {
        return id;
    }

    public FileObject() {
    }

    public FileObject(String filename) {
        this.filename = filename;
        this.file = "";
    }

    public FileObject(String filename, String file) {
        this.filename = filename;
        this.file = file;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }
}
