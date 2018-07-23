package com.example.collabeditor.model;

import javax.persistence.*;

@Entity
@Table(name = "revisions_of_file", uniqueConstraints = {@UniqueConstraint(columnNames = {"revision", "file_object_id"}, name = "file_revision_unique_revision_file_object_id_idx")})
public class FileRevision {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "revision")
    private int revision;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "file_object_id", nullable = false)
    // @OnDelete(action = OnDeleteAction.CASCADE)
    private FileObject fileObject;

    public int getRevision() {
        return revision;
    }

    public void setRevision(int revision) {
        this.revision = revision;
    }

    public FileObject getFileObject() {
        return fileObject;
    }

    public void setFileObject(FileObject fileObject) {
        this.fileObject = fileObject;
    }
}
