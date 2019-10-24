package com.awlsx.gridfsdemo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GridfsService {


    @Autowired
    private MongoDbFactory mongodbfactory;

    @Autowired
    private MongoTemplate mongoTemplate;

    GridFsTemplate gridFsTemplate = null;

    @PostConstruct
    public void init() {
        gridFsTemplate = new GridFsTemplate(mongodbfactory, mongoTemplate.getConverter());
    }


    /**
     * 插入文件
     *
     * @param file
     * @return
     */
    public GridFSInputFile save(MultipartFile file) {

        GridFS gridFS = new GridFS(mongodbfactory.getDb());

        try {

            InputStream in = file.getInputStream();

            String name = file.getOriginalFilename();

            GridFSInputFile gridFSInputFile = gridFS.createFile(in);

            gridFSInputFile.setFilename(name);

            gridFSInputFile.setContentType(file.getContentType());

            gridFSInputFile.save();
            return gridFSInputFile;
        } catch (Exception e) {
        }

        return null;

    }

    /**
     * 据id返回文件
     */
    public GridFSDBFile getById(ObjectId id) {

        GridFS gridFS = new GridFS(mongodbfactory.getDb());


        return gridFS.findOne(new BasicDBObject("_id", id));

    }


    /**
     * 删除
     *
     * @param id
     */
    public void remove(String id) {
        GridFS gridFS = new GridFS(mongodbfactory.getDb());
        gridFS.remove(new ObjectId(id));
    }

    public List<FileInfo> getFileInfoList() throws JsonProcessingException {
        List<GridFSDBFile> gridFSDBFiles = gridFsTemplate.find(null);

        List<FileInfo> collect = gridFSDBFiles.stream().map(i -> {
            FileInfo fileInfo = new FileInfo();
            fileInfo.setFilename(i.getFilename());

            fileInfo.setId((ObjectId) i.getId());
            fileInfo.setLength(i.getLength());
            fileInfo.setUploadData(i.getUploadDate());
            return fileInfo;
        }).collect(Collectors.toList());

        return collect;
    }

}
