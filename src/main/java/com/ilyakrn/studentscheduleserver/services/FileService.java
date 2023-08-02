package com.ilyakrn.studentscheduleserver.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class FileService {

    @Value("${file.root}")
    private String root;

    public File get(String url) {
        long id = Long.parseLong(url.replace("http://localhost:8080/api/files/", ""));
        return new File(root + "/" + id);
    }

    public String post(MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            try {
                if(!new File(root).exists())
                    new File(root).mkdir();
                File[] listOfFiles = new File(root).listFiles();

                long id = -1;
                do{
                    id = Math.round(Math.random()*1000000000);
                    for (int i = 0; i < listOfFiles.length; i++) {
                        File f = listOfFiles[i];
                        if (f.isFile()) {
                            if(f.getName().equals(String.valueOf(id)))
                                id = -1;
                        }
                    }
                } while (id == -1);
                byte[] bytes = file.getBytes();
                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(root + "/" + id)));
                stream.write(bytes);
                stream.close();
                return "http://localhost:8080/api/files/" + id;
            } catch (Exception e) {
                throw new IOException(e);
            }
        } else {
            throw new IOException();
        }
    }
    public void delete(String  url){
        long id = Long.parseLong(url.replace("http://localhost:8080/api/files/", ""));
        File f = new File(root + "/" + id);
        if (f.exists())
            f.delete();
    }
}
