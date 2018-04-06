package com.radar.util;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.radar.FileParser;
import com.radar.RadarHeadfile;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.StringWriter;
import java.text.ParseException;
import java.util.*;

public class DfUtil {
    private FileParser fp=new FileParser();
    private SAXTransformerFactory stf=(SAXTransformerFactory) SAXTransformerFactory.newInstance();
    private TransformerHandler handler;

    private SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
//    private XMLReader reader = parser.getXMLReader();

    public DfUtil() throws TransformerConfigurationException, SAXException, ParserConfigurationException {
        handler=stf.newTransformerHandler();
        Transformer tf=handler.getTransformer();
        tf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        tf.setOutputProperty(OutputKeys.INDENT, "yes");
    }
    public void writeXML(FileDom dom,File xmlFile) throws TransformerConfigurationException, SAXException {
        Result result = new StreamResult(xmlFile);
//        handler = stf.newTransformerHandler();
        handler.setResult(result);
        handler.startDocument();
        handler.startElement("", "", "AllFiles", null);
        writeXML(dom);
        handler.endElement("","","AllFiles");
        handler.endDocument();
    }
    private void writeXML(FileDom dom) throws SAXException {
        String name=dom.getName();
        AttributesImpl attr = new AttributesImpl();
        attr.clear();
        attr.addAttribute("", "", "name", "", name);
        handler.startElement("", "","doc", attr);
        List<RadarHeadfile> list = dom.getInnerFiles();
        if(list!=null){
            writeFilesToXML(list);
        }
        List<FileDom> children=dom.getChildren();
        if(children != null){
            for(FileDom d : children){
                writeXML(d);
            }
        }
        handler.endElement("","","doc");
    }
    private void writeFilesToXML(List<RadarHeadfile> list) throws SAXException {
        AttributesImpl attr = new AttributesImpl();
        for(RadarHeadfile file:list){
            attr.clear();
            attr.addAttribute("", "", "time", "", file.getTime());
            attr.addAttribute("","","name","",file.getName());
            handler.startElement("", "", "file", attr);
            handler.endElement("","","file");
        }
    }


   /*只获取文件夹*/
    private FileFilter docFilter = new FileFilter(){
        public boolean accept(File pathname) {
            if(pathname.isDirectory()){
                return true;
            }else{
                return false;
            }
        }
    };
    /*只获取文件*/
    private FileFilter fileFilter=new FileFilter(){
        public boolean accept(File pathname) {
            if(pathname.isFile()){
                return true;
            }else{
                return false;
            }
        }
    };
    /*找寻目录下所有文件*/
    public FileDom getAllInnerFiles(File file) throws IOException {
        FileDom rootDom = new FileDom();
        File[] fileList = file.listFiles(fileFilter);
        File[] docList = file.listFiles(docFilter);

        rootDom.setName(file.getName());
        if(fileList!=null && fileList.length > 0){
            rootDom.setInnerFiles(readInnerFiles(fileList));
        }
        if(docList!=null && docList.length>0){
            List<FileDom> dList = new ArrayList<>();
            for(File f : docList){
                dList.add(getAllInnerFiles(f));
            }
            rootDom.setChildren(dList);
        }
        return rootDom;
    }
    private List<RadarHeadfile> readInnerFiles(File[] list) throws IOException {
        List<RadarHeadfile> array=new ArrayList<>();
        for(File e:list){
            array.add(fp.readBrief4XML(e.getPath()));
        }
        return array;
    }
//Date[] dateRange,
    public String search(File xmlFile,SrchConst sc) throws IOException, SAXException {
        ReaderHandler rh = new ReaderHandler(sc);
        parser.parse(xmlFile,rh);

        return rh.getResultSw().toString();
    }
    private class ReaderHandler extends DefaultHandler{
        private ObjectMapper mapper=new ObjectMapper();
        private StringWriter sw=new StringWriter();
        private JsonGenerator generator;
        private SrchConst srchConst;
        private boolean foundFlag = false;

        private ReaderHandler(SrchConst sc) throws IOException {
            this.srchConst = sc;
            JsonFactory jsonFactory= mapper.getFactory();
            generator = jsonFactory.createGenerator(sw);
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            super.startElement(uri, localName, qName, attributes);
            if(qName.equals("AllFiles")){
                try {
                    generator.writeStartObject();
                    generator.writeFieldName(qName);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(qName.equals("doc") && srchConst.inDataType(attributes.getValue(0))){
                try {
                    generator.writeStartObject();
                    generator.writeStringField("doc",attributes.getValue(0));
                    generator.writeFieldName("files");
                    generator.writeStartArray();
                    foundFlag = true;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(qName.equals("file")){
                try {
                    String attrTime = attributes.getValue(0);
                    if(srchConst.inTimeRange(attrTime)){
                        generator.writeString(attributes.getValue(1));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            super.endElement(uri, localName, qName);
            if(qName.equals("AllFiles")){
                try {
                    generator.writeEndObject();
                    generator.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(qName.equals("doc") && foundFlag){
                try {
                    generator.writeEndArray();
                    generator.writeEndObject();
                    foundFlag = false;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(qName.equals("file")){

            }

        }

        public StringWriter getResultSw() {
            return sw;
        }

        @Override
        public void endDocument() throws SAXException {
            super.endDocument();
        }
    }
}
