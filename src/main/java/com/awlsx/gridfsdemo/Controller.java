package com.awlsx.gridfsdemo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class Controller {

    @Autowired
    private GridfsService gridfsService;

    @RequestMapping(value = "/file/upload")
    public Object uploadData(@RequestParam(value = "file") MultipartFile file) {

        GridFSInputFile inputFile = gridfsService.save(file);

        if (inputFile == null) {
            return "upload fail";
        }
        String id = inputFile.getId().toString();
        String md5 = inputFile.getMD5();
        String name = inputFile.getFilename();
        long length = inputFile.getLength();

        Map<String, Object> dt = new HashMap<String, Object>();
        dt.put("id", id);
        dt.put("md5", md5);
        dt.put("name", name);
        dt.put("length", length);

        return dt;
    }


    /**
     * 删除
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/file/delete", method = RequestMethod.POST)
    public Object deleteFile(@RequestParam(value = "id") String id) {

//        删除文件
        gridfsService.remove(id);
        return "delete ok";
    }


    /**
     * 下载文件
     *
     * @param id
     * @param response
     */
    @RequestMapping(value = "/file/{id}", method = RequestMethod.GET)
    public void getFile(@PathVariable String id, HttpServletResponse response) {
        GridFSDBFile file = gridfsService.getById(new ObjectId(id));

        if (file == null) {
            responseFail("404 not found", response);
            return;
        }

        OutputStream os = null;

        try {
            os = response.getOutputStream();
            response.addHeader("Content-Disposition", "attachment;filename=" + file.getFilename());
            response.addHeader("Content-Length", "" + file.getLength());
            response.setContentType("application/octet-stream");
            file.writeTo(os);
            os.flush();
            os.close();

        } catch (Exception e) {
            try {
                if (os != null) {
                    os.close();
                }
            } catch (Exception e2) {
            }
            e.printStackTrace();
        }

    }


    @RequestMapping(value = "/files", method = RequestMethod.GET)
    public ResponseEntity listAll() throws JsonProcessingException {
        return ResponseEntity.ok(gridfsService.getFileInfoList());
    }

    @RequestMapping(value = "/file/view/{id}", method = RequestMethod.GET)
    public void viewFile(@PathVariable String id, HttpServletResponse response) {

        GridFSDBFile file = gridfsService.getById(new ObjectId(id));

        if (file == null) {
            responseFail("404 not found", response);
            return;
        }

        OutputStream os = null;

        try {
            os = response.getOutputStream();
            response.addHeader("Content-Disposition", "attachment;filename=" + file.getFilename());
            response.addHeader("Content-Length", "" + file.getLength());
            response.setContentType(file.getContentType());
            file.writeTo(os);
            os.flush();
            os.close();

        } catch (Exception e) {
            try {
                if (os != null) {
                    os.close();
                }
            } catch (Exception e2) {
            }
            e.printStackTrace();
        }

    }

    private void responseFail(String msg, HttpServletResponse response) {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter out = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            String res = mapper.writeValueAsString(msg);
            out = response.getWriter();
            out.append(res);
        } catch (Exception e) {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (Exception e2) {
            }
        }
    }


}
