package com.radar.resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.radar.Data4Json;
import com.radar.FileParser;
import com.radar.RadarFile;
import com.radar.util.DfUtil;
import com.radar.util.SrchConst;
import org.xml.sax.SAXException;


import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Path("/r")
public class MyResource {
    @GET
    @Path("/files/allList")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public String getFileAllList(@Context ServletContext context){
        try{
            String path=context.getResource("WEB-INF\\classes\\fileList.xml").getPath();
            File xmlFile = new File(path);
            DfUtil util = new DfUtil();
            String r=util.getFileNameList(xmlFile);

            return  r;
        }catch (TransformerConfigurationException | SAXException | ParserConfigurationException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    @GET
    @Path("/files/allList/{type}")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public String getSingleList(@PathParam("type")String type,@Context ServletContext context){
        try{
            String path=context.getResource("WEB-INF\\classes\\fileList.xml").getPath();
            File xmlFile = new File(path);
            DfUtil util = new DfUtil();
            SrchConst sc= new SrchConst();
            sc.setDataType(type);
            String r=util.search(xmlFile,sc);

            return  r;
        }catch (TransformerConfigurationException | SAXException | ParserConfigurationException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }
//    @GET
//    @Path("/files/list")
//    @Produces(MediaType.TEXT_PLAIN)
//    @Consumes(MediaType.APPLICATION_JSON)
//    public String getFileList(@Context ServletContext context) throws MalformedURLException, JsonProcessingException {
//        String filePath = context.getResource("WEB-INF\\classes\\dataFiles").getPath();
//        File f=new File(filePath);
//        FileFilter docFilter=new FileFilter(){
//            public boolean accept(File pathname) {
//                if(pathname.isDirectory()){
//                    return true;
//                }else{
//                    return false;
//                }
//            }
//        };
//        File[] docList = f.listFiles(docFilter);
//        FileFilter fileFilter=new FileFilter(){
//            public boolean accept(File pathname) {
//                if(pathname.isFile()){
//                    return true;
//                }else{
//                    return false;
//                }
//            }
//        };
//        Map<String,List<String>> map=new HashMap<>();
//        for(int i=0;i<docList.length;i++){
//            String[] files=filesToStrings(docList[i].listFiles(fileFilter));
//            map.put(docList[i].getName(),Arrays.asList(files));
//        }
//        ObjectMapper mapper = new ObjectMapper();
//        String json = mapper.writeValueAsString(map);
//        return json;
//    }
//    private String[] filesToStrings(File[] list){
//        String[] array=new String[list.length];
//        int i=0;
//        for(File e:list){
//            array[i++]=e.getName();
//        }
//        return array;
//    }

    @GET
    @Path("/files/{fileName}")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public String getFileInfo(@PathParam("fileName")String file , @Context ServletContext context) throws MalformedURLException {
        FileParser p = new FileParser();
        try {
//            double ts = System.currentTimeMillis();
            //KFWD_SDUS64_NCZGRK_201208150212,KEWX_SDUS54_N0VEWX_201707270600,SATE_L2_F2G_VISSR_MWB_LBT_SEC_LCN-IR2-20170527-0100.AWX
            String subName=file.substring(0,4);
            String filePath = Thread.currentThread().getContextClassLoader().getResource("dataFiles\\"+subName+"\\"+file).toString();
            String imagePath = context.getResource("img").getPath();
            System.out.println(imagePath);
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
        /*2018-2-27 更改思路：把3种文件分开放置，请求数据时添加参数，以表明类型*/
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

    @POST
    @Path("/files/search")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public String doDateSearch(String p,@Context ServletContext context) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            SearchParam param =mapper.readValue(p,SearchParam.class);
            System.out.println(param.getTimeStart());
            String timeStart = param.getTimeStart();
            String timeEnd = param.getTimeEnd();

            String path=context.getResource("WEB-INF\\classes\\fileList.xml").getPath();
            File xmlFile = new File(path);
            DfUtil util = new DfUtil();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            Date ds= format.parse(timeStart);
            Date de= format.parse(timeEnd);
            Date[] d = {ds,de};
            SrchConst sc= new SrchConst();
            sc.setPeriod(d);
            sc.setDataType(param.getDataType());

            String r=util.search(xmlFile,sc);

            return  r;
        } catch (TransformerConfigurationException | SAXException | ParserConfigurationException | IOException | ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    @POST
    @Path("/files/geosearch")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public String doGeoSearch(String p, @Context ServletContext context) {
        try {

            System.out.println(p);
            ObjectMapper mapper = new ObjectMapper();
            GeoParam param =mapper.readValue(p,GeoParam.class);
            System.out.println(param);
//            String timeEnd = param.getTimeEnd();
//
//            String path=context.getResource("WEB-INF\\classes\\fileList.xml").getPath();
//            File xmlFile = new File(path);
//            DfUtil util = new DfUtil();
//            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
//            Date ds= format.parse(timeStart);
//            Date de= format.parse(timeEnd);
//            Date[] d = {ds,de};
//            SrchConst sc= new SrchConst();
//            sc.setPeriod(d);
//            sc.setDataType(param.getDataType());
//
//            String r=util.search(xmlFile,sc);
//
//            return  r;
        } catch (/*TransformerConfigurationException | SAXException | ParserConfigurationException |*/ IOException /*| ParseException*/ e) {
            e.printStackTrace();
        }
        return null;
    }
}
