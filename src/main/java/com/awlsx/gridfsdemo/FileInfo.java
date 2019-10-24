package com.awlsx.gridfsdemo;

import com.awlsx.gridfsdemo.support.ObjectIdDeserializer;
import com.awlsx.gridfsdemo.support.ObjectIdSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.Date;

public class FileInfo implements Serializable {

    @Id
    @JsonSerialize(using = ObjectIdSerializer.class)
    @JsonDeserialize(using = ObjectIdDeserializer.class)
    private ObjectId id;

    private String filename;

    private Long length;

    private Date uploadData;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Long getLength() {
        return length;
    }

    public void setLength(Long length) {
        this.length = length;
    }

    public Date getUploadData() {
        return uploadData;
    }

    public void setUploadData(Date uploadData) {
        this.uploadData = uploadData;
    }
}
