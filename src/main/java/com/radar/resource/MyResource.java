package com.radar.resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.radar.Data4Json;
import com.radar.FileParser;
import com.radar.RadarFile;


import javax.servlet.ServletContext;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.MalformedURLException;

@Path("/r")
public class MyResource {

    @GET
    @Path("/files/list")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public String getFileList(@Context ServletContext context) throws MalformedURLException, JsonProcessingException {

        String filePath = context.getResource("WEB-INF\\classes\\dataFiles").getPath();
        File f=new File(filePath);
        File[] list = f.listFiles(new FileFilter() {
            public boolean accept(File pathname) {
//                if(){
                    return true;
//                }else{
//                    return false;
//                }
            }
        });
        String[] array=new String[list.length];
        int i=0;
        for(File e:list){
            array[i++]=e.getName();
        }
        ObjectMapper mapper = new ObjectMapper();
        String j = mapper.writeValueAsString(array);
        return j;
    }


    @GET
    @Path("/files/{fileName}")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public String getFileInfo(@PathParam("fileName")String file , @Context ServletContext context) throws MalformedURLException {
        FileParser p = new FileParser();
        try {
//            double ts = System.currentTimeMillis();
            //KFWD_SDUS64_NCZGRK_201208150212,KEWX_SDUS54_N0VEWX_201707270600,SATE_L2_F2G_VISSR_MWB_LBT_SEC_LCN-IR2-20170527-0100.AWX
            String filePath = Thread.currentThread().getContextClassLoader().getResource("dataFiles\\"+file).toString();
            String imagePath = context.getResource("img").getPath();

            RadarFile rf = p.readWithImg(filePath, imagePath);

//            double te = System.currentTimeMillis();
//            System.out.println(te - ts + "ms");
            ObjectMapper mapper = new ObjectMapper();
            String j = mapper.writeValueAsString(rf);
            return j;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    @GET
    @Path("/filesD/{fileName}")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public String getData(@PathParam("fileName")String file , @Context ServletContext context) throws MalformedURLException {
        FileParser p = new FileParser();
        try {
            String filePath = Thread.currentThread().getContextClassLoader().getResource("dataFiles\\"+file).toString();

            Data4Json rf = p.readGridData(filePath);

            ObjectMapper mapper = new ObjectMapper();
            String j = mapper.writeValueAsString(rf);
            return j;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
