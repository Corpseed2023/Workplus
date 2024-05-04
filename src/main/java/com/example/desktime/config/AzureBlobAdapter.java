package com.example.desktime.config;


import java.io.IOException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.azure.storage.blob.BlobClientBuilder;

@Component
public class AzureBlobAdapter {
    //
    @Autowired
    BlobClientBuilder client;
    //
    public String uploadv2(MultipartFile file, long prefixName) {

        String fileName=null;
        if(file != null && file.getSize() > 0) {
            try {
                //implement your own file name logic.̥
                if(prefixName!=0)
                    fileName = prefixName+file.getOriginalFilename().replace(" ", "_");

                else fileName=file.getOriginalFilename().replace(" ", "_");


                long date = new Date().getTime();
                String path=""+date;
                fileName=path+fileName;
                boolean fileExist = isFileExist(fileName);
                if(!fileExist)
                    client.blobName(fileName).buildClient().upload(file.getInputStream(),file.getSize());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return fileName;
    }


    public boolean isFileExist(String name) {
        BlobClientBuilder client=new BlobClientBuilder();

        boolean flag=false;
        try {
            if(client.blobName(name).buildClient().exists())
                flag=true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }
}
