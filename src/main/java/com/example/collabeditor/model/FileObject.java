package com.example.collabeditor.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "files", uniqueConstraints = {@UniqueConstraint(columnNames = "filename", name = "file_unique_filename_idx")})

public class FileObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "filename", nullable = false)
    private String filename;

    @Lob()
    @Column(name = "file")
    private String file;

    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "fileObject")
    private List<TextEditorMessage> tems;

    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "fileObject")
    private Set<FileRevision> fileRevisions;

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

    public Set<FileRevision> getFileRevisions() {
        return fileRevisions;
    }

    public void setFileRevisions(Set<FileRevision> fileRevisions) {
        this.fileRevisions = fileRevisions;
    }

    public List<TextEditorMessage> getTems() {
        return tems;
    }

    public void setTems(List<TextEditorMessage> tems) {
        this.tems = tems;
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